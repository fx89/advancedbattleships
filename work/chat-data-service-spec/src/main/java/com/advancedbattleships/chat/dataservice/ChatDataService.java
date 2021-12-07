package com.advancedbattleships.chat.dataservice;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.dataservice.model.ChatChannelBan;

public interface ChatDataService {

	Set<? extends ChatChannel> findAllChatChannels();

	Set<? extends ChatChannel> findAllPublicChatChannels();

	Set<? extends ChatChannel> findChatChannelsForParties(Collection<String> partyUniqueTokens);

	ChatChannel newChatChannel();

	ChatChannel saveChatChannel(ChatChannel chatChannel);

	void saveChatChannels(Collection<? extends ChatChannel> chatChannels);

	Collection<? extends ChatChannelBan> findAllChatChannelBans();

	Collection<? extends ChatChannelBan> findAllChatChannelBansByChatChannel(ChatChannel chatChannel);

	Collection<? extends ChatChannelBan> findAllChatChannelBansByChatChannelName(String chatChannelName);

	Collection<? extends ChatChannelBan> findAllChatChannelBansByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(String userUniqueToken, String partyUniqueToken, Date timeWhenLifted);

	int countAllChatChannelBansByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(String userUniqueToken, String partyUniqueToken, Date timeWhenLifted);

	ChatChannelBan newChatChannelBan();

	ChatChannelBan saveChatChannelBan(ChatChannelBan chatChannelBan);

	void saveChatChannelBans(Collection<? extends ChatChannelBan> chatChannelBans);

	void deleteChatChannelBan(ChatChannelBan chatChannelBan);

	void deleteChatChannelBans(Collection<? extends ChatChannelBan> chatChannelBans);
}
