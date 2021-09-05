package com.advancedbattleships.messaging.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.messaging.dataservice.impl.springdata.model.PersistentMessageChannelImpl;

public interface PersistentMessageChannelsRepository extends PagingAndSortingRepository<PersistentMessageChannelImpl, Long> {

	Set<PersistentMessageChannelImpl> findAll();

	Set<PersistentMessageChannelImpl> findAllByMessageTypeId(Long mssageTypeId);

	Set<PersistentMessageChannelImpl> findAllByMessageTypeName(String mssageTypeName);
}
