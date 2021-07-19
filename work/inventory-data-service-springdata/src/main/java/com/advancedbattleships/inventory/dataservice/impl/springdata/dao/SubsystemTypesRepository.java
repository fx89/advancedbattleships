package com.advancedbattleships.inventory.dataservice.impl.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.inventory.dataservice.impl.springdata.model.SubsystemTypeImpl;

public interface SubsystemTypesRepository extends PagingAndSortingRepository<SubsystemTypeImpl, Long> {

}
