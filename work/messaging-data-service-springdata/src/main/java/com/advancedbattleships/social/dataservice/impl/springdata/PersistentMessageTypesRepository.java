package com.advancedbattleships.social.dataservice.impl.springdata;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.PersistentMessageTypeImpl;

public interface PersistentMessageTypesRepository extends PagingAndSortingRepository<PersistentMessageTypeImpl, Long> {
	Set<PersistentMessageTypeImpl> findAll();
}
