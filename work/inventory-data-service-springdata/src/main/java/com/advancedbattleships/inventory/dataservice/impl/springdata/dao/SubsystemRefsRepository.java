package com.advancedbattleships.inventory.dataservice.impl.springdata.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.inventory.dataservice.impl.springdata.model.SubsystemRefImpl;

public interface SubsystemRefsRepository extends PagingAndSortingRepository<SubsystemRefImpl, Long> {

	List<SubsystemRefImpl> findAll();

	SubsystemRefImpl findFirstByUniqueToken(String uniqueToken);

	Set<SubsystemRefImpl> findAllByTypeName(String typeName);
}
