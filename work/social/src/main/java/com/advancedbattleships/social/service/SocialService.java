package com.advancedbattleships.social.service;

import static com.advancedbattleships.common.lang.Suppliers.nullSafeSupplier;
import static java.util.Collections.synchronizedSet;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import com.advancedbattleships.social.dataservice.model.Party;
import com.advancedbattleships.social.dataservice.model.UserFriend;
import com.advancedbattleships.social.dataservice.model.UserParty;
import com.advancedbattleships.social.exception.AdvancedBattleshipsFriendNotFoundSocialException;
import com.advancedbattleships.social.exception.AdvancedBattleshipsSocialException;
import com.advancedbattleships.social.service.model.Friend;
import com.advancedbattleships.userstatustracker.service.UserStatusTrackerService;
import com.advancedbattleships.utilityservices.UniqueTokenProviderService;

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

	@Autowired
	private UserStatusTrackerService userStatusTracker;

	@Autowired
	private UniqueTokenProviderService uniqueTokenProvider;

	private static FriendStatus FRIEND_STATUS_ACCEPTED;
	private static FriendStatus FRIEND_STATUS_UNATTENDED;

	private static final String FRIEND_PRESENCE_ONLINE = "online";
	private static final String FRIEND_PRESENCE_OFFLINE = "offline";
	private static final String FRIEND_PRESENCE_INGAME = "ingame"; // TODO: handle in computeFriendPresence
	private static final String FRIEND_PRESENCE_UNKNOWN = "unknown";

	private final Map<String, Set<UserFriend>> userFriendTokens = new ConcurrentHashMap<>();

	// TODO: do something about having multiple instances of this thing in multiple containers
	private final Set<String> validPartyTokens = synchronizedSet(new HashSet<>());

	@PostConstruct
	private void init() {
		dataService.findAllFriendStatuses()
			.forEach(fStatus -> {
				if ("accepted"  .equals(fStatus.getName())) FRIEND_STATUS_ACCEPTED   = fStatus;
				if ("unattended".equals(fStatus.getName())) FRIEND_STATUS_UNATTENDED = fStatus;
			});

		validPartyTokens.addAll(
				dataService.findAllParties().stream().map(Party::getPartyUniqueToken).collect(toList())
			);
		
	}

	/**
	 * If the provided token is not found in the locally maintained set of validPartyTokens,
	 * it means the token is not valid.
	 */
	// TODO: change this in case of switching to a micro-services-based architecture
	public synchronized boolean isPartyUniqueTokenValid(String partyUniqueToken) {
		return validPartyTokens.contains(partyUniqueToken);
	}

	/**
	 * Returns a list of the user's friends, even if they haven't accepted
	 * the invitation. They will be rendered in a distinct way.
	 */
	public Set<Friend> getUserFriends(String userUniqueToken) {
		return getUserFriendDetails(findUserFriends(userUniqueToken));
	}

	private Set<UserFriend> findUserFriends(String userUniqueToken) {
		// Get the friends
		Set<UserFriend> userFriends
			= dataService.getUserFriends(userUniqueToken, Arrays.asList("accepted", "unattended"));

		// Cache the user friend details for use by getUserFriendStatuses,
		// which is expected to be called quite frequently
		userFriendTokens.put(userUniqueToken, userFriends);

		return userFriends;
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

		// Get the list of friends who are online according to user status tracker.
		// This will be used when calling computeFriendPresence().
		Set<String> onlineFriendTokens = userStatusTracker.getUserTokensStillAlive(userFriendUniqueTokens);

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
					computeFriendPresence(userFriend, onlineFriendTokens),
					friendUser.getNickName(),
					friendConfig.getCurrentLogoName(),
					userFriend.getPrivatePartyUniqueToken()
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

		// Accept the request and create a new UserFriend for the target user.
		// Also create a party and add both users to the party.
		// Do it all inside a transaction, to be able to roll back in case
		// something crashes
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

			// Create the party for the two users and add it to the cache
			Party party = dataService.newParty();
			party.setPartyUniqueToken(uniqueTokenProvider.provide());
			party.setName("Private party [" + fromUserUniqueToken + "]<->[" + toUserUniqueToken + "]");
			party = dataService.saveParty(party);
			validPartyTokens.add(party.getPartyUniqueToken());

			// Add the users to the party
			UserParty fromUserParty = createUserParty(fromUserUniqueToken, party);
			UserParty toUserParty = createUserParty(toUserUniqueToken, party);
			dataService.saveUserParties(Arrays.asList(fromUserParty, toUserParty));

			// Update the private party of both the friend request and the reciprocal user friend
			friendRequest.setPrivatePartyUniqueToken(party.getPartyUniqueToken());
			reciprocalUserFriend.setPrivatePartyUniqueToken(party.getPartyUniqueToken());

			// Save the friend request and the reciprocal user friend
			dataService.saveUserFriends(List.of(friendRequest, reciprocalUserFriend));

			// Send the acceptance message back to the requester
			messaging.sendFriendRequest(
				toUserUniqueToken,
				fromUserUniqueToken,
				"New friend: " + toUser.getNickName(),
				toUser.getNickName() + " has accepted your friend request"
			);
		});
	}

	private UserParty createUserParty(String userUniqueToken, Party party) {
		return createUserParty(userUniqueToken, party, FRIEND_STATUS_ACCEPTED);
	}

	private UserParty createUserParty(String userUniqueToken, Party party, FriendStatus status) {
		UserParty ret = dataService.newUserParty();
		ret.setParty(party);
		ret.setUserUniqueToken(userUniqueToken);
		ret.setStatus(status);

		return ret;
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

	public Map<String, String> getUserFriendStatuses(String userUniqueToken) {
		// Get the unique tokens of the user's friends
		Set<UserFriend> userFriends = resolveUserFriends(userUniqueToken);

		// If not found, then return an empty map
		if (userFriends == null || userFriends.size() == 0) {
			return new HashMap<>();
		}

		// Extract the set of friend tokens which are still alive
		Set<String> loggedInFriendTokens
			= userStatusTracker.getUserTokensStillAlive(
					userFriends.stream().map(UserFriend::getFriendUserUniqueToken).collect(toSet())
				);

		return userFriends.stream()
			.collect(toMap(
				UserFriend::getFriendUserUniqueToken,
				k -> computeFriendPresence(k, loggedInFriendTokens)
			));
	}

	private static String computeFriendPresence(UserFriend friend, Set<String> onlineFriendTokens) {
		// See if the friend's token can be found in the list of online friend tokens
		String onlineFriendToken = onlineFriendTokens.stream()
			.filter(f -> f.equals(friend.getFriendUserUniqueToken()))
			.findFirst().orElse(null);

		// If the friend token is not in the list of online friend tokens,
		// return OFFLIE
		if (onlineFriendToken == null) {
			return FRIEND_PRESENCE_OFFLINE;
		}

		// If the friend is online, then check if the status is ACCEPTED
		// and, if so, then return ONLINE
		if (friend.getStatus().equals(FRIEND_STATUS_ACCEPTED)) {
			// TODO: handle FRIEND_PRESENCE_INGAME
			return FRIEND_PRESENCE_ONLINE;
		}

		// If none of the above checks yielded any result, return UNKNOWN
		return FRIEND_PRESENCE_UNKNOWN;
	}

	private Set<UserFriend> resolveUserFriends(String userUniqueToken) {
		// Try to get the friend tokens from the local cache
		Set<UserFriend> friendUniqueTokens = userFriendTokens.get(userUniqueToken);

		// If there's nothing cached, then call the getUserFriends() method,
		// which will cache the friend tokens
		if (friendUniqueTokens == null) {
			findUserFriends(userUniqueToken);
			friendUniqueTokens = userFriendTokens.get(userUniqueToken);
		}

		// Finally, return the cached tokens
		return friendUniqueTokens;
	}

	public boolean userBelongsToParty(String userUniqueToken, String partyUniqueToken) {
		Collection<UserParty> userParties = dataService.getUserPartyRecords(userUniqueToken);

		if (userParties == null) {
			return false;
		}

		return
			userParties.stream()
				.anyMatch(up ->
						nullSafeSupplier(() ->
							   up.getParty().getPartyUniqueToken().equals(partyUniqueToken)
							&& up.getStatus().equals(FRIEND_STATUS_ACCEPTED)
						, false)
					);
	}
}
