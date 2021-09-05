package com.advancedbattleships.social.service;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.content.dataservice.model.UserUiConfig;
import com.advancedbattleships.content.service.ContentService;
import com.advancedbattleships.messaging.service.MessagingService;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.service.SecurityService;
import com.advancedbattleships.social.dataservice.SocialDataService;
import com.advancedbattleships.social.dataservice.model.FriendStatus;
import com.advancedbattleships.social.dataservice.model.UserFriend;
import com.advancedbattleships.social.exception.AdvancedBattleshipsFriendNotFoundSocialException;
import com.advancedbattleships.social.exception.AdvancedBattleshipsSocialException;
import com.advancedbattleships.social.service.model.Friend;

@Service
public class SocialService {

	@Autowired
	private SocialDataService dataService;

	@Autowired
	private SecurityService security;

	@Autowired
	private ContentService content;

	@Autowired
	private MessagingService messaging;

	private static FriendStatus FRIEND_STATUS_ACCEPTED;
	private static FriendStatus FRIEND_STATUS_UNATTENDED;

	@PostConstruct
	private void init() {
		dataService.findAllFriendStatuses()
			.forEach(fStatus -> {
				if ("accepted"  .equals(fStatus.getName())) FRIEND_STATUS_ACCEPTED   = fStatus;
				if ("unattended".equals(fStatus.getName())) FRIEND_STATUS_UNATTENDED = fStatus;
			});
	}

	/**
	 * Returns a list of the user's friends, even if they haven't accepted
	 * the invitation. They will be rendered in a distinct way.
	 */
	public Set<Friend> getUserFriends(String userUniqueToken) {
		// Get the friends
		Set<UserFriend> userFriends
			= dataService.getUserFriends(userUniqueToken, Arrays.asList("accepted", "unattended"));

		// Get the details of the user's friends
		return getUserFriendDetails(userFriends);
	}

	private Set<Friend> getUserFriendDetails(Set<UserFriend> userFriends) {
		// Return an empty set if the given friends set is null or empty
		if (userFriends == null || userFriends.isEmpty()) {
			return Collections.emptySet();
		}

		// Get the unique tokens of the friends, for use in the following calls
		// to the other service provisioning content for this method
		Set<String> userFriendUniqueTokens
			= userFriends.stream()
				.map(userFriend -> userFriend.getFriendUserUniqueToken())
				.collect(toSet());

		// Get the friends from the security service,
		// using the unique tokens extracted above
		Set<User> friends = security.getUsers(userFriendUniqueTokens);

		// Get the friends UI configurations from the content service,
		// using the unique tokens extracted above
		Set<UserUiConfig> friendUiConfigs = content.getUsersConfig(userFriendUniqueTokens);

		// Create and return the collection of objects of the composite data type
		return userFriends.stream()
			.map(userFriend -> {
				// Get the friend's unique token, to shorten the next few lines a little bit
				String friendToken = userFriend.getFriendUserUniqueToken();

				// Get the user from the security service response
				User friendUser = friends.stream()
					.filter(u -> u.getUniqueToken().equals(friendToken))
					.findFirst()
					.orElseThrow(() -> new AdvancedBattleshipsSocialException(
							"Unable to find user with the following unique token [" + friendToken + "]"
						));

				// Get the user's UI configuration from the content service response
				UserUiConfig friendConfig = friendUiConfigs.stream()
					.filter(c -> c.getUserUniqueToken().equals(friendToken))
					.findFirst()
					.orElseThrow(() -> new AdvancedBattleshipsSocialException(
							"Unable to find config for user with the following unique token [" + friendToken + "]"
						));

				// Compile the data into the composite data type
				return new Friend(
					userFriend.getFriendUserUniqueToken(),
					userFriend.getStatus().getName(),
					userFriend.getStatus().getName().equals("accepted") ? friendUser.isOnline() : false, // no status unless accepted
					friendUser.getNickName(),
					friendConfig.getCurrentLogoName()
				);
			})
			.collect(toSet());
	}

	/**
	 * Create a friend request and send it to the target user
	 */
	public UserFriend createFriendRequest(User fromUser, String toUserUniqueToken) {
		// Try to find an already existing request with the same users
		UserFriend alreadyExisting
			= dataService.findUserFriendByUserUniqueTokenAndFriendUniqueToken(toUserUniqueToken, fromUser.getUniqueToken());

		// If found, then let the user know the request was already sent
		if (alreadyExisting != null) {
			throw new AdvancedBattleshipsSocialException("This friend request was already sent");
		}

		// If all is well, then create the new request
		final UserFriend newRequest = dataService.newUserFriend();
		newRequest.setStatus(FRIEND_STATUS_UNATTENDED);
		newRequest.setUserUniqueToken(toUserUniqueToken);
		newRequest.setFriendUserUniqueToken(fromUser.getUniqueToken());

		// Save the new request, after sending the message to the target user
		// Roll back the transaction if the messaging service failed
		dataService.executeTransaction(() -> {
			messaging.sendFriendRequest(
					fromUser.getUniqueToken(),
					toUserUniqueToken, 
					"Friend Request: " + fromUser.getNickName(),
					fromUser.getNickName() + " has sent you a friend request. Click this message to view the request."
				);

			dataService.saveUserFriend(newRequest);
		});

		// Finally, return the new request to let the user know everything went OK
		return newRequest;
	}

	/**
	 * Accept the request and update the friend's friend list
	 */
	public void acceptFriendRequest(String fromUserUniqueToken, User toUser) {

		String toUserUniqueToken = toUser.getUniqueToken();

		UserFriend friendRequest = findUserFriendRequestOrCrash(fromUserUniqueToken, toUserUniqueToken);

		// If the request was already accepted, it means there was an error
		if (friendRequest.getStatus().equals(FRIEND_STATUS_ACCEPTED)) {
			throw new AdvancedBattleshipsSocialException("Friend request was already accepted");
		}

		// Accept the request and create a new UserFriend for the target user
		// Do it inside a transaction, to be able to roll back in case something
		// crashes
		dataService.executeTransaction(() -> {
			// Accept and save the original request
			friendRequest.setStatus(FRIEND_STATUS_ACCEPTED);
			dataService.saveUserFriend(friendRequest);

			// Create an already accepted reciprocal request,
			// to update the target users' friend list
			UserFriend reciprocalUserFriend = dataService.newUserFriend();
			reciprocalUserFriend.setStatus(FRIEND_STATUS_ACCEPTED);
			reciprocalUserFriend.setUserUniqueToken(fromUserUniqueToken);
			reciprocalUserFriend.setFriendUserUniqueToken(toUserUniqueToken);
			dataService.saveUserFriend(reciprocalUserFriend);

			// Send the acceptance message back to the requester
			messaging.sendFriendRequest(
				toUserUniqueToken,
				fromUserUniqueToken,
				"New friend: " + toUser.getNickName(),
				toUser.getNickName() + " has accepted your friend request"
			);
		});
	}

	/**
	 * Rejects the friend request from the user having the given fromUserUniqueToken
	 * to the referenced toUser
	 */
	public void rejectFriendRequest(String fromUserUniqueToken, User toUser) {
		// Extract the toUserUniqueToken, for easier reference
		String toUserUniqueToken = toUser.getUniqueToken();

		// Run the operations in a transaction, which is rolled bac
		// in case something goes wrong
		dataService.executeTransaction(() -> {
			// Get the record and delete it
			UserFriend friendRequest = findUserFriendRequestOrCrash(fromUserUniqueToken, toUserUniqueToken);
			dataService.deleteUserFriend(friendRequest);

			// Notify the requester that the request has been rejected
			messaging.sendFriendRequest(
				toUserUniqueToken,
				fromUserUniqueToken,
				"Friend request rejected: " + toUser.getNickName(),
				toUser.getNickName() + " has rejected your friend request"
			);
		});
	}

	private UserFriend findUserFriendRequestOrCrash(String fromUserUniqueToken, String toUserUniqueToken) {
		// Get the request
		UserFriend friendRequest
			= dataService.findUserFriendByUserUniqueTokenAndFriendUniqueToken(
					toUserUniqueToken,
					fromUserUniqueToken
				);

		// If the request was not found, it means there was an error, or a hack attempt
		if (friendRequest == null) {
			throw new AdvancedBattleshipsSocialException(
					"Friend request not found"
				);
		}

		return friendRequest;
	}

	public void inviteFriend(User user, String friendNickName) {
		// Find the user having the specified nick name
		User friend = security.findUserByNickName(friendNickName);

		// If not found, throw exception
		if (friend == null) {
			throw new AdvancedBattleshipsFriendNotFoundSocialException(
				"No user found having the nick name [" + friendNickName + "]"
			);
		}

		// Users are not allowed to befriend themselves
		if (friend.getUniqueToken().equals(user.getUniqueToken())) {
			throw new AdvancedBattleshipsFriendNotFoundSocialException(
				"You are not allowed to send friend requests to yourself"
			);
		}

		// Create the friend request
		try {
			createFriendRequest(user, friend.getUniqueToken());
		} catch (AdvancedBattleshipsSocialException exc) {
			throw new AdvancedBattleshipsFriendNotFoundSocialException(exc.getMessage(), exc);
		}
	}

	public Set<Friend> getUnattendedUserFriendInvitations(User forUser) {
		return getUserFriendDetails(
				dataService.getUserFriends(forUser.getUniqueToken(), Arrays.asList("unattended"))
			);
	}
}
