package com.advancedbattleships.system.dataservice.impl.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.system.dataservice.impl.springdata.model.Parameter;

public interface ParametersRepository extends PagingAndSortingRepository<Parameter, Long> {

	Parameter findFirstByName(String name);

}
