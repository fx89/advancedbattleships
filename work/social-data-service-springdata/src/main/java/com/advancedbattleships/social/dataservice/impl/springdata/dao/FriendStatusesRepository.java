package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.FriendStatusImpl;

public interface FriendStatusesRepository extends PagingAndSortingRepository<FriendStatusImpl, Long> {

}
