package com.advancedbattleships.security.dataservice.impl.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.security.dataservice.impl.springdata.model.GroupImpl;

public interface GroupsRepository extends PagingAndSortingRepository<GroupImpl, Long> {
	GroupImpl findFirstByName(String name);
}
