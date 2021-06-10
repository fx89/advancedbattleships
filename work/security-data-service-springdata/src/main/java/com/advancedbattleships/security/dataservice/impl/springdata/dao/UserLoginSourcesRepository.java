package com.advancedbattleships.security.dataservice.impl.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.security.dataservice.impl.springdata.model.UserLoginSourceImpl;
import com.advancedbattleships.security.dataservice.model.LoginSource;

public interface UserLoginSourcesRepository extends PagingAndSortingRepository<UserLoginSourceImpl, Long> {
	UserLoginSourceImpl findFirstByLoginSourceAndLoginToken(LoginSource loginSource, String loginToken);
}
