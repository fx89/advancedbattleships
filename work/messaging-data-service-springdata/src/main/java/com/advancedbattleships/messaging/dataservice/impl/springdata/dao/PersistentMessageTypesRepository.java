package com.advancedbattleships.messaging.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.messaging.dataservice.impl.springdata.model.PersistentMessageTypeImpl;

public interface PersistentMessageTypesRepository extends PagingAndSortingRepository<PersistentMessageTypeImpl, Long> {
	Set<PersistentMessageTypeImpl> findAll();
}
