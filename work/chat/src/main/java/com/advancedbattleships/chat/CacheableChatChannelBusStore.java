package com.advancedbattleships.chat;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.chat.bus.ChatChannelBus;
import com.advancedbattleships.chat.bus.SynchronizedCachingChatChannelBus;
import com.advancedbattleships.chat.dataservice.ChatDataService;
import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.exception.ChatChannelBusAuthorizationException;
import com.advancedbattleships.chat.exception.ChatChannelBusInitializationException;
import com.advancedbattleships.common.lang.constants.BooleanConstants;
import com.advancedbattleships.social.service.SocialService;
import com.advancedbattleships.system.SystemService;

@Service
public class CacheableChatChannelBusStore implements BooleanConstants {

	@Autowired
	private SystemService system;

	@Autowired
	private SocialService social;

	@Autowired
	private ChatDataService dataService;

	/**
	 * Caches channel buses by party unique tokens
	 */
	private final Map<String, ChatChannelBus> busStore = new ConcurrentHashMap<>();

	/**
	 * Caches the results of the authorization process by a hash between the user unique token and party unique token
	 */
	private final Map<String, Boolean> requestAuthorization = new ConcurrentHashMap<>();

	/**
	 * Returns a chat channel bus for the given combination of user unique token and
	 * party unique token. If the user does not have access to the party, or if the
	 * user has a ban on the referenced party, the returned chat channel bus will
	 * throw exception from each operation. 
	 */
	public synchronized ChatChannelBus getChatChannelBus(String userUniqueToken, String partyUniqueToken) {
		// Check authorization. Do not continue if not authorized. Use a caching mechanism to avoid high usage.
		verifyUnauthorizedRequest(userUniqueToken, partyUniqueToken);

		// If authorization was successful, then resolve the bus for the party channel.
		return resolveChatChannelBus(partyUniqueToken);
	}

	/**
	 * Authorization cache must be cleared every once in a while, to release user bans
	 */
	public synchronized void expungeAuthorizationCache() {
		requestAuthorization.clear();
	}

	/**
	 * Throws exceptions if the user is banned or does not have access to the party channel
	 */
	private synchronized void verifyUnauthorizedRequest(String userUniqueToken, String partyUniqueToken) {
		// Check the validity of the party unique token, to avoid filling up the cache with bogus requests (which could be caused by some kind of attack)
		if (false == social.isPartyUniqueTokenValid(partyUniqueToken)) {
			throw new ChatChannelBusAuthorizationException("Wrong party unique token");
		}

		// Compile the request hash
		String requestHash = userUniqueToken + partyUniqueToken;

		// See if the request was previously processed
		Boolean authorized = requestAuthorization.get(requestHash);

		// If the request was previously processed and was not authorized, then the request ends here
		if (BOOL_TRUE == authorized) {
			throw new ChatChannelBusInitializationException("Unauthorized");
		}

		// If the request was not previously processed...
		if (null == authorized) {
			try {
				// Verify authorization
				verifyUserBelongsToPartyChannel(userUniqueToken, partyUniqueToken);
				verifyUserNotBannedOnPartyChannel(userUniqueToken, partyUniqueToken);
	
				// Register authorization
				requestAuthorization.put(requestHash, BOOL_TRUE);
			}
			// In case of authorization issues, cache the failed authorization for the next call and throw the exception
			catch (ChatChannelBusAuthorizationException e) {
				 requestAuthorization.put(requestHash, BOOL_FALSE);
				 throw e;
			}
		}
	}

	private synchronized ChatChannelBus resolveChatChannelBus(String partyUniqueToken) {
		// See if the channel is cached.
		ChatChannelBus ret = busStore.get(partyUniqueToken);
	
		// If the channel is not cached, it must be created and cached before it can be returned.
		if (ret == null) {
			ret = createChatChannelBus(partyUniqueToken);
			busStore.put(partyUniqueToken, ret);
		}

		// Return the channel reference.
		return ret;
	}

	/**
	 * Creates a channel bus for the party channel
	 */
	private ChatChannelBus createChatChannelBus(String partyUniqueToken) {
		// Resolve channel
		ChatChannel chatChannel = resolvePartyChannel(partyUniqueToken);

		// Create the bus
		return new SynchronizedCachingChatChannelBus(
					chatChannel,
					system.getDataService().getIntParameter("CHAT.DEFAULT_WINDOW_SIZE")
				);
	}

	private void verifyUserBelongsToPartyChannel(String userUniqueToken, String partyUniqueToken) {
		if (! social.userBelongsToParty(userUniqueToken, partyUniqueToken)) {
			throw new ChatChannelBusAuthorizationException("User does not belong to party");
		}
	}

	private void verifyUserNotBannedOnPartyChannel(String userUniqueToken, String partyUniqueToken) {
		if (userHasBansOnPartyChannel(userUniqueToken, partyUniqueToken)) {
			throw new ChatChannelBusAuthorizationException("User is banned");
		}
	}

	private ChatChannel resolvePartyChannel(String partyUniqueToken) {
		// See if there's a channel already defined for the given user/party combination
		ChatChannel chatChannel = 
			dataService.findChatChannelsForParties(Arrays.asList(partyUniqueToken))
				.stream().findFirst().orElse(null);

		// If there's no chat channel defined, then create it
		if (chatChannel == null) {
			chatChannel = dataService.saveChatChannel(
								createPrivateChatChannel(partyUniqueToken)
							);
		}

		return chatChannel;
	}

	
	private boolean userHasBansOnPartyChannel(String userUniqueToken, String partyUniqueToken) {
		return
			dataService.countAllChatChannelBansByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(
					userUniqueToken, partyUniqueToken, new Date()
				) > 0;
	}

	private ChatChannel createPrivateChatChannel(String partyUniqueToken) {
		return createChatChannel(partyUniqueToken, true);
	}

	private ChatChannel createChatChannel(String partyUniqueToken, boolean isPrivate) {
		ChatChannel ret = dataService.newChatChannel();

		ret.setName("Chat channel for party [" + partyUniqueToken + "]");
		ret.setPartyUniqueToken(partyUniqueToken);
		ret.setPrivate(isPrivate);

		return ret;
	}
	
}
