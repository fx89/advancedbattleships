package com.advancedbattleships.inventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.inventory.dataservice.InventoryDataService;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.Point2I;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;
import com.advancedbattleships.inventory.exception.AdvancedBattleshipsInventoryValidationException;

@Service
public class InventoryService {

	@Autowired
	private InventoryDataService dataService;

	public List<BattleshipTemplate> getUserBattleshipTemplates(String userUniqueToken) {
		return dataService.getUserBattleshipTemplates(userUniqueToken);
	}

	public BattleshipTemplate createNewBattleshipTemplate(String userUniqueToken, String templateName, int width, int height) {
		validateBattleshipHullSize(width, height);
		return dataService.createEmptyBattleshipTemplate(userUniqueToken, templateName, width, height);
	}

	public List<BattleshipTemplateSubsystem> getBattleshipTemplateSubsystems(BattleshipTemplate battleshipTemplate) {
		return dataService.getBattleshipTemplateSubsystems(battleshipTemplate);
	}

	public BattleshipTemplateSubsystem addBattleshipTemplateSubsyste(
		BattleshipTemplate battleshipTemplate,
		SubsystemRef subsystemRef,
		int posX,
		int posY
	) {
		// TODO: Don't forget to validate that the battleship template is in fact owned by the current user
		validateSubsystemPlacementOnHull(battleshipTemplate, posX, posY);
		return dataService.addBattleshipTemplateSubsystem(battleshipTemplate, subsystemRef, posX, posY);
	}

	public BattleshipTemplateSubsystem updateBattleshipTemplateSubsystem(BattleshipTemplateSubsystem subsystem) {
		BattleshipTemplate battleshipTemplate = subsystem.getBattleshipTemplate();
		validateBattleshipTemplateNotNull(battleshipTemplate);

		List<BattleshipTemplateSubsystem> allSubsystems = dataService.getBattleshipTemplateSubsystems(battleshipTemplate);

		validateSubsystem(subsystem, allSubsystems);

		return dataService.updateBattleshipTemplateSubsystem(subsystem);
	}

	public void deleteBattleshipTemplateSubsystem(BattleshipTemplateSubsystem subsystem) {
		dataService.deleteBattleshipTemplateSubsystem(subsystem);
	}

	public void validateBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		validateBattleshipTemplateNotNull(battleshipTemplate);

		Point2I hullSize = battleshipTemplate.getHullSize();
		validateBattleshipHullSize(hullSize.x, hullSize.y);

		List<BattleshipTemplateSubsystem> subsystems
			= dataService.getBattleshipTemplateSubsystems(battleshipTemplate);

		subsystems.forEach(subsystem -> {
			validateSubsystem(subsystem, subsystems);
		});
	}

	public List<SubsystemRef> getSubsystemRefs() {
		return dataService.getSubsystemRefs();
	}

	private void validateBattleshipTemplateNotNull(BattleshipTemplate battleshipTemplate) {
		if (battleshipTemplate == null) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Null reference provided for validation of battleship template"
			);
		}
	}

	private void validateSubsystem(BattleshipTemplateSubsystem subsystem, List<BattleshipTemplateSubsystem> allSubsystems) {
		Point2I pos = subsystem.getPosition();
		validateSubsystemPlacementOnHull(subsystem.getBattleshipTemplate(), pos.x, pos.y);
		validateSubsystemPlacementInRelationToOtherSubsystems(subsystem, allSubsystems);
	}

	private void validateSubsystemPlacementOnHull(BattleshipTemplate battleshipTemplate, int posX, int posY)  {
		if (battleshipTemplate == null) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"The subsystem does not reference a battleship template"
			);
		}

		// TODO: finish implementing
	}

	private void validateSubsystemPlacementInRelationToOtherSubsystems(BattleshipTemplateSubsystem subsystem, List<BattleshipTemplateSubsystem> allSubsystems) {
		// TODO: implement
	}

	private void validateBattleshipHullSize(int width, int height) {
		// TODO: implement
	}
}
