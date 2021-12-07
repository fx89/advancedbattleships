package com.advancedbattleships.messaging.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.messaging.dataservice.MessagingDataService;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessage;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageChannel;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageSourceType;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageType;
import com.advancedbattleships.messaging.exception.AdvancedBattleshipsMessagingInitializationException;

@Service
public class MessagingService {

	private static final String MSG_TYPE_FRIEND_REQUEST = "friend request";
	private static final String MSG_TYPE_PARTY_REQUEST = "party request";
	private static final String MSG_TYPE_GAME_INVITATION = "game invitation";
	private static final String MSG_TYPE_SYSTEM_MESSAGE = "system message";
	private static final String MSG_TYPE_NEWS = "news";

	private static final String CH_FRIEND_REQUESTS = "friend requests";
	private static final String CH_PARTY_REQUESTS = "party requests";
	private static final String CH_GAME_INVITATIONS = "game invitations";
	private static final String CH_SYSTEM_MESSAGES = "system messages";
	private static final String CH_GLOBAL_NEWS = "global news";

	private static final String SRC_USER = "user";
	private static final String SRC_SYSTEM = "system";

	private PersistentMessageType msgTypeFriendRequest;
	private PersistentMessageType msgTypePartyRequest;
	private PersistentMessageType msgTypeGameInvitaiton;
	private PersistentMessageType msgTypeSystemMessage;
	private PersistentMessageType msgTypeNews;

	private PersistentMessageChannel chFriendRequests;
	private PersistentMessageChannel chPartyRequests;
	private PersistentMessageChannel chGamesInvitations;
	private PersistentMessageChannel chSystemMessages;
	private PersistentMessageChannel chGlobalNews;

	private PersistentMessageSourceType srcTypeUser;
	private PersistentMessageSourceType srcTypeSystem;

	@Autowired
	private MessagingDataService messagingDataService;

	@PostConstruct
	public void init() {
		initMessageTypes();
		initChannels();
		initSourceTypes();
	}

	private void initMessageTypes() {
		@SuppressWarnings("unchecked")
		Set<PersistentMessageType> messageTypes
			= (Set<PersistentMessageType>) messagingDataService.findAllPersistentMessageTypes();

		msgTypeFriendRequest  = extractMessageType(MSG_TYPE_FRIEND_REQUEST , messageTypes);
		msgTypePartyRequest   = extractMessageType(MSG_TYPE_PARTY_REQUEST  , messageTypes);
		msgTypeGameInvitaiton = extractMessageType(MSG_TYPE_GAME_INVITATION, messageTypes);
		msgTypeSystemMessage  = extractMessageType(MSG_TYPE_SYSTEM_MESSAGE , messageTypes);
		msgTypeNews           = extractMessageType(MSG_TYPE_NEWS           , messageTypes);		
	}

	private PersistentMessageType extractMessageType(String name, Set<PersistentMessageType> messageTypes) {
		return messageTypes.stream()
			.filter(msgType -> msgType.getName().equals(name))
			.findFirst()
			.orElseThrow(() -> new AdvancedBattleshipsMessagingInitializationException(
					"Cannot find message type [" + name + "]"
				));
	}

	private void initChannels() {
		@SuppressWarnings("unchecked")
		Set<PersistentMessageChannel> channels
			= (Set<PersistentMessageChannel>) messagingDataService.findAllPersistentMessageChannels();

		chFriendRequests   = extractMessageChannel(CH_FRIEND_REQUESTS , channels);
		chPartyRequests    = extractMessageChannel(CH_PARTY_REQUESTS  , channels);
		chGamesInvitations = extractMessageChannel(CH_GAME_INVITATIONS, channels);
		chSystemMessages   = extractMessageChannel(CH_SYSTEM_MESSAGES , channels);
		chGlobalNews       = extractMessageChannel(CH_GLOBAL_NEWS     , channels);
	}

	private PersistentMessageChannel extractMessageChannel(String name, Set<PersistentMessageChannel> channels) {
		return channels.stream()
			.filter(ch -> ch.getName().equals(name))
			.findFirst()
			.orElseThrow(() -> new AdvancedBattleshipsMessagingInitializationException(
					"Cannot find messaging channel [" + name + "]"
				));
	}

	private void initSourceTypes() {
		@SuppressWarnings("unchecked")
		Set<PersistentMessageSourceType> sourceTypes
			= (Set<PersistentMessageSourceType>) messagingDataService.findAllPersistentMessageSourceTypes();

		srcTypeUser   = extractSourceType(SRC_USER  , sourceTypes);
		srcTypeSystem = extractSourceType(SRC_SYSTEM, sourceTypes);
	}

	private PersistentMessageSourceType extractSourceType(String name, Set<PersistentMessageSourceType> sourceTypes) {
		return sourceTypes.stream()
			.filter(st -> st.getName().equals(name))
			.findFirst()
			.orElseThrow(() -> new AdvancedBattleshipsMessagingInitializationException(
					"Cannot find source type [" + name + "]"
				));
	}

	/**
	 * Get unread messages for the referenced users on the given channel
	 */
	public List<? extends PersistentMessage> getUnreadPersistentMessagesForUserOnChannel(String userUniqueToken, PersistentMessageChannel channel) {
		return
			messagingDataService
				.findPersistentMessagesByUserUniqueTokenAndReadAndChannel(
					userUniqueToken, false, channel);
	}

	/**
	 * Get unread friend requests for the referenced user
	 */
	public List<? extends PersistentMessage> getUnresolvedFriendRequests(String userUniqueToken) {
		return getUnreadPersistentMessagesForUserOnChannel(userUniqueToken, chFriendRequests);
	}

	public void sendUserMessage(
		String fromUserUniqueToken,
		String toUserUniqueToken,
		String title,
		String body,
		PersistentMessageType messageType,
		PersistentMessageChannel channel,
		Boolean important
	) {
		PersistentMessage msg = messagingDataService.newPersistentMessage();

		msg.setMessageType(messageType);
		msg.setChannel(channel);
		msg.setSourceType(srcTypeUser);
		msg.setSourceUniqueToken(fromUserUniqueToken);
		msg.setUserUniqueToken(toUserUniqueToken);
		msg.setTitle(title);
		msg.setBody(body);
		msg.setImportant(important);
		msg.setRead(false);
		msg.setMessageTime(new Date());
		msg.setUserNotified(false);

		msg = messagingDataService.savePersistentMessage(msg);
	}

	/**
	 * Create a new friend request from one user to another
	 */
	public void sendFriendRequest(
		String fromUserUniqueToken,
		String toUserUniqueToken,
		String title,
		String body
	) {
		sendUserMessage(
			fromUserUniqueToken,
			toUserUniqueToken,
			title,
			body,
			msgTypeFriendRequest,
			chFriendRequests,
			false
		);
	}

	/**
	 * Fetches a list of persistent messages that require the attention of the
	 * user having the given unique token while also marking them as notified. 
	 */
	@SuppressWarnings("unchecked")
	public List<PersistentMessage> getUserNotifications(String userUniqueToken) {
		List<PersistentMessage> messages
			= (List<PersistentMessage>)
				messagingDataService
					.findPersistentMessagesByUserUniqueTokenAndIsUserNotified(userUniqueToken, false);

		if (messages.size() > 0) {
			messages.forEach(msg -> msg.setUserNotified(true));
			messagingDataService.savePersistentMessages(messages);
		}

		return messages;
	}
}
