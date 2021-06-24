package com.advancedbattleships.content.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleStylesheetImpl;

public interface UserAccessibleStylesheetsRepository
		extends PagingAndSortingRepository<UserAccessibleStylesheetImpl, Long> {

	Set<UserAccessibleStylesheetImpl> findAllByUserUserUniqueToken(String userUniqueToken);
}
