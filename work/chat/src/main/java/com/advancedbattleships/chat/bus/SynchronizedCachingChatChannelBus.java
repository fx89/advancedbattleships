package com.advancedbattleships.chat.bus;

import static java.util.Collections.synchronizedList;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.model.ChatMessage;

public class SynchronizedCachingChatChannelBus implements ChatChannelBus {

	private final ChatChannel chatChannel;

	private final List<ChatMessage> rollingWindow;

	private final int maxWindowSize;

	private Long lastMessageId = 0L;

	public SynchronizedCachingChatChannelBus(ChatChannel chatChannel, int windowSize) {
		this.chatChannel = chatChannel;
		this.maxWindowSize = windowSize;
		this.rollingWindow = synchronizedList(new LinkedList<>());
	}

	@Override
	public ChatChannel getChatChannel() {
		return chatChannel;
	}

	@Override
	public Collection<ChatMessage> getMessages() {
		return getMessages(maxWindowSize);
	}

	@Override
	public Collection<ChatMessage> getMessages(int windowSize) {
		// No need to create a sub-list if the given window size covers the entire list
		if (windowSize >= rollingWindow.size()) {
			return rollingWindow;
		}

		// If the given window size is smaller than the available window,
		// then a subset of the available window must be returned, containing
		// the end of the available window.
		return rollingWindow.subList(rollingWindow.size() - windowSize, windowSize);
	}

	@Override
	// TODO: find a way to make sure there are no messages lost to the front-end if there are more messages typed in than the window size 2 between consecutive read requests
	public Collection<ChatMessage> getNewMessages(Long lastRegisteredMessageId) {
		return
			rollingWindow.stream()
				.filter(msg -> msg.getId() > lastRegisteredMessageId)
				.collect(toList());
	}

	@Override
	public synchronized void addMessage(String userUniqueToken, String message) {
		// Add the message
		rollingWindow.add(new ChatMessage(lastMessageId, userUniqueToken, new Date(), message));
		lastMessageId++;

		// Roll the window
		if (rollingWindow.size() == maxWindowSize) {
			rollingWindow.remove(0);
		}
	}
}
