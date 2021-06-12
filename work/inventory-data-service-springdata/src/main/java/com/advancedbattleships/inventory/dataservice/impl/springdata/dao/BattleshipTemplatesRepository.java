package com.advancedbattleships.inventory.dataservice.impl.springdata.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.inventory.dataservice.impl.springdata.model.BattleshipTemplateImpl;

public interface BattleshipTemplatesRepository extends PagingAndSortingRepository<BattleshipTemplateImpl, Long> {

	public List<BattleshipTemplateImpl> findAllByUserUniqueToken(String userUniqueToken);

	public BattleshipTemplateImpl findFirstByUniqueToken(String uniqueToken);

}
