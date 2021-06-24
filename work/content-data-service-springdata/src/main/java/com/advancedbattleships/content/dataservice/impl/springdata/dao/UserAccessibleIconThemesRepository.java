package com.advancedbattleships.content.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleIconThemeImpl;

public interface UserAccessibleIconThemesRepository
		extends PagingAndSortingRepository<UserAccessibleIconThemeImpl, Long> {

	Set<UserAccessibleIconThemeImpl> findAllByUserUserUniqueToken(String userUniqueToken);
}
