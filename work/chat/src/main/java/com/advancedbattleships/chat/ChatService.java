package com.advancedbattleships.chat;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.chat.bus.ChatChannelBus;
import com.advancedbattleships.chat.model.ChatMessage;

@Service
public class ChatService {

	@Autowired
	private CacheableChatChannelBusStore chatChannelBusStore;

	public Collection<ChatMessage> getChatMessages(String userUniqueToken, String partyUniqueToken) {
		return chatChannelBusStore.getChatChannelBus(userUniqueToken, partyUniqueToken).getMessages();
	}

	public Collection<ChatMessage> getNewChatMessages(String userUniqueToken, String partyUniqueToken, Long lastRegisteredMessageId) {
		return chatChannelBusStore.getChatChannelBus(userUniqueToken, partyUniqueToken).getNewMessages(lastRegisteredMessageId);
	}

	public void addChatMessage(String userUniqueToken, String partyUniqueToken, String message) {
		ChatChannelBus chatChannelBus = chatChannelBusStore.getChatChannelBus(userUniqueToken, partyUniqueToken);
		chatChannelBus.addMessage(userUniqueToken, message);
	}
}
