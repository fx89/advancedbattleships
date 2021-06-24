package com.advancedbattleships.content.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleLogoImpl;

public interface UserAccessibleLogosRepository extends PagingAndSortingRepository<UserAccessibleLogoImpl, Long> {
	Set<UserAccessibleLogoImpl> findAllByUserUserUniqueToken(String userUniqueToken);
}
