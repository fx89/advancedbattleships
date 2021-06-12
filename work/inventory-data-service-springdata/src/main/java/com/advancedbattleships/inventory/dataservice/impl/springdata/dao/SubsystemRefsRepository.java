package com.advancedbattleships.inventory.dataservice.impl.springdata.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.inventory.dataservice.impl.springdata.model.SubsystemRefImpl;

public interface SubsystemRefsRepository extends PagingAndSortingRepository<SubsystemRefImpl, Long> {

	List<SubsystemRefImpl> findAll();

}
