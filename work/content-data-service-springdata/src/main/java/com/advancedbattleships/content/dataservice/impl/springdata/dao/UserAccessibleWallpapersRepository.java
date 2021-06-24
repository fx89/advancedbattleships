package com.advancedbattleships.content.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleWallpaperImpl;

public interface UserAccessibleWallpapersRepository
		extends PagingAndSortingRepository<UserAccessibleWallpaperImpl, Long> {

	Set<UserAccessibleWallpaperImpl> findAllByUserUserUniqueToken(String userUniqueToken);
}
