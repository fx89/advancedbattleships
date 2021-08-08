package com.advancedbattleships.social.service;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.content.dataservice.model.UserUiConfig;
import com.advancedbattleships.content.service.ContentService;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.service.SecurityService;
import com.advancedbattleships.social.dataservice.SocialDataService;
import com.advancedbattleships.social.dataservice.model.UserFriend;
import com.advancedbattleships.social.exception.AdvancedBattleshipsSocialException;
import com.advancedbattleships.social.service.model.Friend;

@Service
public class SocialService {

	@Autowired
	private SocialDataService socialDataService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ContentService contentService;

	public Set<Friend> getUserFriends(String userUniqueToken) {
		// Get the friends
		Set<UserFriend> userFriends = socialDataService.getUserFriends(userUniqueToken);

		// Get the unique tokens of the friends, for use in the following calls
		// to the other service provisioning content for this method
		Set<String> userFriendUniqueTokens
			= userFriends.stream()
				.map(userFriend -> userFriend.getFriendUserUniqueToken())
				.collect(toSet());

		// Get the friends from the security service,
		// using the unique tokens extracted above
		Set<User> friends = securityService.getUsers(userFriendUniqueTokens);

		// Get the friends UI configurations from the content service,
		// using the unique tokens extracted above
		Set<UserUiConfig> friendUiConfigs = contentService.getUsersConfig(userFriendUniqueTokens);

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
					friendUser.isOnline(),
					friendUser.getNickName(),
					friendConfig.getCurrentLogoName()
				);
			})
			.collect(toSet());
	}
}
