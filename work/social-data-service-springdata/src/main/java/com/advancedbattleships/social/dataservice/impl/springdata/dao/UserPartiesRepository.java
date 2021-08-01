package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.UserPartyImpl;

public interface UserPartiesRepository extends PagingAndSortingRepository<UserPartyImpl, Long> {
	Set<UserPartyImpl> findAllByUserUniqueToken(String userUniqueToken);

	Set<UserPartyImpl> findAllByPartyPartyUniqueToken(String partyUniqueToken);
}
