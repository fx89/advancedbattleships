package com.advancedbattleships.security.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.advancedbattleships.security.dataservice.impl.springdata.model.UserImpl;

public interface UsersRepository extends PagingAndSortingRepository<UserImpl, Long> {
	UserImpl findOneByUniqueToken(String uniqueToken);

	Set<UserImpl> findAllByUniqueTokenIn(Set<String> uniqueTokens);

	@Modifying
	@Query("UPDATE UserImpl u SET u.isOnline = :online WHERE u.uniqueToken IN (:userUniqueTokens)")
	void setOnlineFlagWhereUserUniqueTokenIn(
		@Param("userUniqueTokens") Set<String> userUniqueTokens,
		@Param("online") Boolean online
	);
}
