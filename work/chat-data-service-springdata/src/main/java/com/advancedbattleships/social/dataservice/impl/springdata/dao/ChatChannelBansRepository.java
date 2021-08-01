package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.ChatChannelBanImpl;

public interface ChatChannelBansRepository extends PagingAndSortingRepository<ChatChannelBanImpl, Long> {

	Collection<ChatChannelBanImpl> findAll();

	Collection<ChatChannelBanImpl> findAllByChatChannelId(Long chatChannelId);

	Collection<ChatChannelBanImpl> findAllByChatChannelName(String chatChannelName);

	Collection<ChatChannelBanImpl> findAllByUserUniqueToken(String userUniqueToken);

	Collection<ChatChannelBanImpl> findAllByUserUniqueTokenAndTimeTillLiftedMinsGreaterThan(String userUniqueToken,
			Long timeTillLiftedMins);

	Collection<ChatChannelBanImpl> findAllByUserUniqueTokenAndChatChannelIdAndTimeTillLiftedMinsGreaterThan(
			String userUniqueToken, Long chatChannelId, Long timeTillLiftedMins);

	Collection<ChatChannelBanImpl> findAllByUserUniqueTokenAndChatChannelNameAndTimeTillLiftedMinsGreaterThan(
			String userUniqueToken, String chatChannelName, Long timeTillLiftedMins);
}
