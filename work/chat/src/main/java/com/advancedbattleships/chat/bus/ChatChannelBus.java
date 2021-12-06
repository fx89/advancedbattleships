package com.advancedbattleships.chat.bus;

import java.util.Collection;

import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.model.ChatMessage;

public interface ChatChannelBus {

	/**
	 * Returns the chat channel for which the bus has been created
	 */
	ChatChannel getChatChannel();

	/**
	 * Returns the last X messages set on the channel, where X is the
	 * default window size configured in the system parameters
	 */
	Collection<ChatMessage> getMessages();

	/**
	 * Returns the last X messages set on the channel, where X is the
	 * given windowSize
	 */
	Collection<ChatMessage> getMessages(int windowSize);

	/**
	 * Returns all the messages that come after the given message id,
	 * unless they have been discarded from memory
	 */
	Collection<ChatMessage> getNewMessages(Long lastRegisteredMessageId);

	/**
	 * Adds a new chat message from the user referenced by the given
	 * userUniqueToken
	 */
	void addMessage(String userUniqueToken, String message);
}
