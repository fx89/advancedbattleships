package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.PersistentMessageImpl;

public interface PersistentMessagesRepository extends PagingAndSortingRepository<PersistentMessageImpl, Long> {

	List<PersistentMessageImpl> findAllByUserUniqueTokenAndIsRead(String userUniqueToken, Boolean isRead);

	List<PersistentMessageImpl> findAllByUserUniqueTokenAndIsReadAndChannelName(String userUniqueToken, Boolean isRead,
			String channelName);

	List<PersistentMessageImpl> findAllByUserUniqueTokenAndIsReadAndChannelId(String userUniqueToken, Boolean isRead,
			Long channelId);
}
