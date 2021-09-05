package com.advancedbattleships.messaging.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.messaging.dataservice.impl.springdata.model.PersistentMessageSourceTypeImpl;

public interface PersistentMessageSourceTypesRepository
		extends PagingAndSortingRepository<PersistentMessageSourceTypeImpl, Long> {

	Set<PersistentMessageSourceTypeImpl> findAll();

}
