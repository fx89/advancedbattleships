package com.advancedbattleships.inventory.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.inventory.dataservice.InventoryDataService;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.Point2I;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;
import com.advancedbattleships.inventory.exception.AdvancedBattleshipsInventoryValidationException;
import com.advancedbattleships.system.SystemService;
import com.advancedbattleships.utilityservices.UniqueTokenProviderService;

@Service
public class InventoryService {
	@Autowired
	private UniqueTokenProviderService uniqueTokenProvider;

	@Autowired
	private InventoryDataService dataService;

	@Autowired
	private SystemService system;

	// Spring cache doesn't work on methods of the same class
	// Don't want to cache the data service, since it's supposed to be interchangeable and there's a risk of forgetting about the cache when writing new implementations
	private final Map<String, SubsystemRef> subsystemRefsCache = new ConcurrentHashMap<>();

	public List<BattleshipTemplate> getUserBattleshipTemplates(String userUniqueToken) {
		return dataService.getUserBattleshipTemplates(userUniqueToken);
	}

	public BattleshipTemplate createNewBattleshipTemplate(String userUniqueToken, String templateName, int width, int height) {
		validateBattleshipHullSize(width, height);
		return dataService.createEmptyBattleshipTemplate(
				uniqueTokenProvider.provide(),
				userUniqueToken, templateName, width, height
			);
	}

	/**
	 * Returns a list of the subsystems defined by the battleship template
	 * identified by the given unique token, belonging to the user with the given
	 * unique token. If the tokens don't match, an exception is thrown.
	 */
	public List<BattleshipTemplateSubsystem> getBattleshipTemplateSubsystems(
			String userUniqueToken,
			String battleshipTemplateUniqueToken
	) {
		BattleshipTemplate battleshipTemplate
			= getUserBattleshipTemplate(userUniqueToken, battleshipTemplateUniqueToken);

		return dataService.getBattleshipTemplateSubsystems(battleshipTemplate);
	}

	public BattleshipTemplateSubsystem getBattleshipTemplateSubsystem(
		String userUniqueToken,
		String subsystemUniqueToken
	) {
		BattleshipTemplateSubsystem subsystem
			= dataService.getBattleshipTemplateSubsystemByUniqueToken(subsystemUniqueToken);

		if (subsystem == null) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Subsystem with token [" + subsystemUniqueToken + "] does not exist"
			);
		}

		if (! subsystem.getBattleshipTemplate().getUniqueToken().equals(userUniqueToken)) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Cannot fetch battleship template subsystem. Invalid tokens provided."
			);
		}

		return subsystem;
	}

	public BattleshipTemplateSubsystem addBattleshipTemplateSubsystem(
		String userUniqueToken,
		String battleshipTemplateUniqueToken,
		String subsystemRefUniqueToken,
		int posX,
		int posY
	) {
		BattleshipTemplate battleshipTemplate
			= getUserBattleshipTemplate(userUniqueToken, battleshipTemplateUniqueToken);

		List<BattleshipTemplateSubsystem> existingSubsystems
			= getBattleshipTemplateSubsystems(userUniqueToken, battleshipTemplateUniqueToken);

		validateSubsystemPlacementOnHull(battleshipTemplate, posX, posY);
		validateSubsystemPlacementInRelationToOtherSubsystems(posX, posY, existingSubsystems);

		SubsystemRef subsystemRef = getSubsystemRef(subsystemRefUniqueToken);

		return dataService.addBattleshipTemplateSubsystem(
				uniqueTokenProvider.provide(),
				battleshipTemplate, subsystemRef, posX, posY
		);
	}

	public BattleshipTemplateSubsystem updateBattleshipTemplateSubsystemPosition(
		String userUniqueToken,
		String subsystemUniqueToken,
		int posX, int posY
	) {
		BattleshipTemplateSubsystem subsystem
			= getBattleshipTemplateSubsystem(userUniqueToken, subsystemUniqueToken);

		subsystem.setPosition(new Point2I(posX, posY));

		return dataService.updateBattleshipTemplateSubsystem(subsystem);
	}

	public void deleteBattleshipTemplateSubsystem(
		String userUniqueToken,
		String subsystemUniqueToken
	) {
		BattleshipTemplateSubsystem subsystem
			= getBattleshipTemplateSubsystem(userUniqueToken, subsystemUniqueToken);

		dataService.deleteBattleshipTemplateSubsystem(subsystem);
	}

	public List<SubsystemRef> getSubsystemRefs() {
		return dataService.getSubsystemRefs();
	}

	public void validateBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		validateBattleshipTemplateNotNull(battleshipTemplate);

		Point2I hullSize = battleshipTemplate.getHullSize();
		validateBattleshipHullSize(hullSize.x, hullSize.y);

		List<BattleshipTemplateSubsystem> subsystems
			= dataService.getBattleshipTemplateSubsystems(battleshipTemplate);

		subsystems.forEach(subsystem -> {
			validateSubsystem(battleshipTemplate, subsystem, subsystems);
		});
	}

	private void validateBattleshipTemplateNotNull(BattleshipTemplate battleshipTemplate) {
		if (battleshipTemplate == null) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Null reference provided for validation of battleship template"
			);
		}
	}

	private void validateSubsystem(
		BattleshipTemplate battleshipTemplate,
		BattleshipTemplateSubsystem subsystem,
		List<BattleshipTemplateSubsystem> allSubsystems
	) {
		Point2I pos = subsystem.getPosition();
		validateSubsystemPlacementOnHull(battleshipTemplate, pos.x, pos.y);
		validateSubsystemPlacementInRelationToOtherSubsystems(pos.x, pos.y, allSubsystems);
	}

	private void validateSubsystemPlacementOnHull(BattleshipTemplate battleshipTemplate, int posX, int posY)  {
		if (battleshipTemplate == null) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"The subsystem does not reference a battleship template"
			);
		}

		if (posX < 0 || posY < 0) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Position cannot be negative"
			); 
		}

		Point2I hullSize = battleshipTemplate.getHullSize();

		if (posX >= hullSize.x || posY >= hullSize.y) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Position cannot exceed the bounds of the battleship"
			);
		}

		int minDistanceFromHull = system.getDataService().getIntParameter("SUBSYSTEM.DISTANCE_FROM_HULL");

		int x1 = posX - minDistanceFromHull; if (x1 < 0) x1 = 0;
		int y1 = posY - minDistanceFromHull; if (y1 < 0) y1 = 0;
		int x2 = posX + minDistanceFromHull; if (x2 >= hullSize.x) x2 = hullSize.x - 1;
		int y2 = posY + minDistanceFromHull; if (y2 >= hullSize.y) y2 = hullSize.y - 1;

		boolean[][] hull = battleshipTemplate.getHull();

		for (int x = x1 ; x <= x2 ; x++) {
			for (int y = y1 ; y <= y2 ; y++) {
				if (false == hull[y][x]) {
					throw new AdvancedBattleshipsInventoryValidationException(
						"Subsystem must be placed on the hull and must not be closer than [" + minDistanceFromHull + "] cells to the margin of the hull"
					);
				}
			}
		}
	}

	private void validateSubsystemPlacementInRelationToOtherSubsystems(int posX, int posY, List<BattleshipTemplateSubsystem> allSubsystems) {
		int minDistanceFromSubsystem
			= system.getDataService().getIntParameter("SUBSYSTEM.DISTANCE_FROM_OTHERS");

		for (BattleshipTemplateSubsystem subsystem : allSubsystems) {
			Point2I subPos = subsystem.getPosition();
			if (subPos.x != posX || subPos.y != posY) {
				if (Math.abs(subPos.x - posX) < minDistanceFromSubsystem
				 || Math.abs(subPos.y - posY) < minDistanceFromSubsystem) {
					throw new AdvancedBattleshipsInventoryValidationException(
						"Subsystem must be placed no closer than [" + minDistanceFromSubsystem + "] cells to any other subsystem"
					);
				}
			}
		}
	}

	private void validateBattleshipHullSize(int width, int height) {
		int maxCells = system.getDataService().getIntParameter("BATTLESHIP.MAX_SIZE");

		if (width * height > maxCells) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Maximum hull size exceeded. Cannot have more than [" + maxCells + "] cells."
			);
		}
	}

	private BattleshipTemplate getUserBattleshipTemplate(String userUniqueToken, String battleshipTemplateUniqueToken) {
		BattleshipTemplate bsTemplate
			= dataService.getBattleshipTemplateByUniqueToken(battleshipTemplateUniqueToken);

		if (   bsTemplate == null
			|| bsTemplate.getUserUniqueToken() == null
			|| (! bsTemplate.getUserUniqueToken().equals(userUniqueToken) )
		) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Cannot fetch battleship template. Invalid tokens provided."
			);
		}

		return bsTemplate;
	}

	private SubsystemRef getSubsystemRef(String uniqueToken) {
		if (uniqueToken == null) {
			return null;
		}

		SubsystemRef ret = subsystemRefsCache.get(uniqueToken);

		if (ret == null) {
			ret = dataService.getSubsystemRefByUniqueToken(uniqueToken);
			subsystemRefsCache.put(uniqueToken, ret); // even if RET is NULL - won't query the database forever if it isn't found
		}

		return ret;
	}
}
