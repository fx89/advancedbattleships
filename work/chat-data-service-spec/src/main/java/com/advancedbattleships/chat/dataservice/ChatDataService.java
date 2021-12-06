package com.advancedbattleships.chat.dataservice;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.dataservice.model.ChatChannelBan;

public interface ChatDataService {

	Set<ChatChannel> findAllChatChannels();

	Set<ChatChannel> findAllPublicChatChannels();

	Set<ChatChannel> findChatChannelsForParties(Collection<String> partyUniqueTokens);

	ChatChannel newChatChannel();

	ChatChannel saveChatChannel(ChatChannel chatChannel);

	void saveChatChannels(Collection<ChatChannel> chatChannels);

	Collection<ChatChannelBan> findAllChatChannelBans();

	Collection<ChatChannelBan> findAllChatChannelBansByChatChannel(ChatChannel chatChannel);

	Collection<ChatChannelBan> findAllChatChannelBansByChatChannelName(String chatChannelName);

	Collection<ChatChannelBan> findAllChatChannelBansByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(String userUniqueToken, String partyUniqueToken, Date timeWhenLifted);

	int countAllChatChannelBansByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(String userUniqueToken, String partyUniqueToken, Date timeWhenLifted);

	ChatChannelBan newChatChannelBan();

	ChatChannelBan saveChatChannelBan(ChatChannelBan chatChannelBan);

	void saveChatChannelBans(Collection<ChatChannelBan> chatChannelBans);

	void deleteChatChannelBan(ChatChannelBan chatChannelBan);

	void deleteChatChannelBans(Collection<ChatChannelBan> chatChannelBans);
}
