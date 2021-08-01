package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.PartyImpl;

public interface PartiesRepository extends PagingAndSortingRepository<PartyImpl, Long> {

	Set<PartyImpl> findAllByNameLike(String name);

}
