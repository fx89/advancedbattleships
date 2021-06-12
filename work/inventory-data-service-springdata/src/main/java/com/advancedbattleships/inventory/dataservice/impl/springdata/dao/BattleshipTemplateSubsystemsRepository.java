package com.advancedbattleships.inventory.dataservice.impl.springdata.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.inventory.dataservice.impl.springdata.model.BattleshipTemplateSubsystemImpl;

public interface BattleshipTemplateSubsystemsRepository extends PagingAndSortingRepository<BattleshipTemplateSubsystemImpl, Long> {

	List<BattleshipTemplateSubsystemImpl> findAllByBattleshipTemplateId(Long battleshipTemplateId);

	BattleshipTemplateSubsystemImpl findFirstByUniqueToken(String uniqueToken);
}
