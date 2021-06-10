package com.advancedbattleships.security.dataservice.impl.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.security.dataservice.impl.springdata.model.UserImpl;

public interface UsersRepository extends PagingAndSortingRepository<UserImpl, Long> {
	
}
