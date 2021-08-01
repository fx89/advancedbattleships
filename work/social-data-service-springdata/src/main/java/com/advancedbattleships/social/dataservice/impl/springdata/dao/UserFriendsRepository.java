package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.UserFriendImpl;

public interface UserFriendsRepository extends PagingAndSortingRepository<UserFriendImpl, Long> {

	Set<UserFriendImpl> findAllByUserUniqueToken(String userUniqueToken);

}
