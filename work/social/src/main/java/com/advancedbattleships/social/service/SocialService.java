package com.advancedbattleships.social.service;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.content.dataservice.model.UserUiConfig;
import com.advancedbattleships.content.service.ContentService;
import com.advancedbattleships.messaging.service.MessagingService;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.service.SecurityService;
import com.advancedbattleships.social.dataservice.SocialDataService;
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

	/**
	 * Returns a list of the user's friends, even if they haven't accepted
	 * the invitation. They will be rendered in a distinct way.
	 */
	public Set<Friend> getUserFriends(String userUniqueToken) {
		// Get the friends
		Set<UserFriend> userFriends = dataService.getUserFriends(userUniqueToken);

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
					userFriend.getAccepted(),
					userFriend.getAccepted() ? friendUser.isOnline() : false, // no status unless accepted
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
			= dataService.findUserFriendByUserUniqueTokenAndFriendUniqueToken(fromUser.getUniqueToken(), toUserUniqueToken);

		// If found, then let the user know the request was already sent
		if (alreadyExisting != null) {
			throw new AdvancedBattleshipsSocialException("This friend request was already sent");
		}

		// If all is well, then create the new request
		final UserFriend newRequest = dataService.newUserFriend();
		newRequest.setAccepted(false);
		newRequest.setUserUniqueToken(fromUser.getUniqueToken());
		newRequest.setFriendUserUniqueToken(toUserUniqueToken);

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
	public void acceptFriendRequest(String fromUserUniqueToken, String toUserUniqueToken) {

		// Get the request
		UserFriend friendRequest
			= dataService.findUserFriendByUserUniqueTokenAndFriendUniqueToken(
					fromUserUniqueToken,
					toUserUniqueToken
				);

		// If the request was not found, it means there was an error, or a hack attempt
		if (friendRequest == null) {
			throw new AdvancedBattleshipsSocialException(
					"Friend request not found"
				);
		}

		// If the request was already accepted, it means there was an error
		if (friendRequest.getAccepted()) {
			throw new AdvancedBattleshipsSocialException(
					"Friend request was already accepted"
				);
		}

		// Accept the request and create a new UserFriend for the target user
		// Do it inside a transaction, to be able to roll back in case something
		// crashes
		dataService.executeTransaction(() -> {
			// Accept and save the original request
			friendRequest.setAccepted(true);
			dataService.saveUserFriend(friendRequest);

			// Create an already accepted reciprocal request,
			// to update the target users' friend list
			UserFriend reciprocalUserFriend = dataService.newUserFriend();
			reciprocalUserFriend.setAccepted(true);
			reciprocalUserFriend.setUserUniqueToken(toUserUniqueToken);
			reciprocalUserFriend.setFriendUserUniqueToken(fromUserUniqueToken);
			dataService.saveUserFriend(reciprocalUserFriend);
		});
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
}
