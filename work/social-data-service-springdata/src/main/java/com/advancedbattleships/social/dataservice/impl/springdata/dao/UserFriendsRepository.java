package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.UserFriendImpl;

public interface UserFriendsRepository extends PagingAndSortingRepository<UserFriendImpl, Long> {

	@EntityGraph("UserFriendImpl.AllEager")
	Set<UserFriendImpl> findAllByUserUniqueToken(String userUniqueToken);

	@EntityGraph("UserFriendImpl.AllEager")
	Set<UserFriendImpl> findAllByUserUniqueTokenAndStatusName(String userUniqueToken, String statusName);

	@EntityGraph("UserFriendImpl.AllEager")
	Set<UserFriendImpl> findAllByUserUniqueTokenAndStatusNameIn(String userUniqueToken, List<String> statusNames);

	@EntityGraph("UserFriendImpl.AllEager")
	UserFriendImpl findOneByUserUniqueTokenAndFriendUserUniqueToken(String userUniqueToken, String friendUserUniqueToken);
}
