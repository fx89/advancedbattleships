package com.advancedbattleships.security.dataservice.impl.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.security.dataservice.impl.springdata.model.UserGroup;

public interface UserGroupsRepository extends PagingAndSortingRepository<UserGroup, Long> {

}
