package com.advancedbattleships.messaging.dataservice.impl.springdata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.messaging.dataservice.impl.springdata.model.PersistentMessageImpl;

public interface PersistentMessagesRepository extends PagingAndSortingRepository<PersistentMessageImpl, Long> {

	@EntityGraph("PersistentMessageImpl.AllEager")
	List<PersistentMessageImpl> findAllByUserUniqueTokenAndIsRead(String userUniqueToken, Boolean isRead);

	@EntityGraph("PersistentMessageImpl.AllEager")
	List<PersistentMessageImpl> findAllByUserUniqueTokenAndIsReadAndChannelName(String userUniqueToken, Boolean isRead,
			String channelName);

	@EntityGraph("PersistentMessageImpl.AllEager")
	List<PersistentMessageImpl> findAllByUserUniqueTokenAndIsReadAndChannelId(String userUniqueToken, Boolean isRead,
			Long channelId);

	@EntityGraph("PersistentMessageImpl.AllEager")
	List<PersistentMessageImpl> findAllByUserUniqueTokenAndIsUserNotified(String userUniqueToken, Boolean isUserNotified);
}
