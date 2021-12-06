package com.advancedbattleships.chat.dataservice.impl.springdata.dao;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.chat.dataservice.impl.springdata.model.ChatChannelBanImpl;

public interface ChatChannelBansRepository extends PagingAndSortingRepository<ChatChannelBanImpl, Long> {

	Collection<ChatChannelBanImpl> findAll();

	Collection<ChatChannelBanImpl> findAllByChatChannelId(Long chatChannelId);

	Collection<ChatChannelBanImpl> findAllByChatChannelName(String chatChannelName);

	Collection<ChatChannelBanImpl> findAllByUserUniqueToken(String userUniqueToken);

	Collection<ChatChannelBanImpl> findAllByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(
			String userUniqueToken, String partyUniqueToken, Date timeWhenLifted);

	int countAllByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(
			String userUniqueToken, String partyUniqueToken, Date timeWhenLifted);
}
