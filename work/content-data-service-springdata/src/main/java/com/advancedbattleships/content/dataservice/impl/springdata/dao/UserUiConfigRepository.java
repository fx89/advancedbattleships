package com.advancedbattleships.content.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.content.dataservice.impl.springdata.model.UserUiConfigImpl;

public interface UserUiConfigRepository extends PagingAndSortingRepository<UserUiConfigImpl, Long> {

	UserUiConfigImpl findOneByUserUniqueToken(String userUniqueToken);

	Set<UserUiConfigImpl> findAllByUserUniqueTokenIn(Set<String> userUniqueTokens);
}
