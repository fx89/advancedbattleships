package com.advancedbattleships.inventory.service;

import static com.advancedbattleships.common.lang.Suppliers.nullSafeSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.inventory.dataservice.InventoryDataService;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.Point2I;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefGeneratedResourceRequirement;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefGeneratedResourceSpec;
import com.advancedbattleships.inventory.dataservice.model.SubsystemType;
import com.advancedbattleships.inventory.exception.AdvancedBattleshipsInventorySecurityException;
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

	@SuppressWarnings("unchecked")
	public List<BattleshipTemplate> getUserBattleshipTemplates(String userUniqueToken) {
		return (List<BattleshipTemplate>) dataService.getUserBattleshipTemplates(userUniqueToken);
	}

	public BattleshipTemplate createNewBattleshipTemplate(String userUniqueToken, String templateName, int width, int height) {
		validateBattleshipHullSize(width, height);
		validateTemplateame(templateName);
		return dataService.createEmptyBattleshipTemplate(
				uniqueTokenProvider.provide(),
				userUniqueToken, templateName, width, height
			);
	}

	public void deleteBattleshipTemplate(String userUniqueToken, String uniqueToken) {
		BattleshipTemplate bsTemplate = dataService.getBattleshipTemplateByUniqueToken(uniqueToken);
		validateBattleshipTemplateOwnership(bsTemplate, userUniqueToken);
		dataService.deleteBattleshipTemplate(bsTemplate);
	}

	/**
	 * Returns a list of the subsystems defined by the battleship template
	 * identified by the given unique token, belonging to the user with the given
	 * unique token. If the tokens don't match, an exception is thrown.
	 */
	@SuppressWarnings("unchecked")
	public List<BattleshipTemplateSubsystem> getBattleshipTemplateSubsystems(
		String userUniqueToken,
		String battleshipTemplateUniqueToken
	) {
		BattleshipTemplate battleshipTemplate
			= getUserBattleshipTemplate(userUniqueToken, battleshipTemplateUniqueToken);

		return (List<BattleshipTemplateSubsystem>) dataService.getBattleshipTemplateSubsystems(battleshipTemplate);
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

		if (! subsystem.getBattleshipTemplate().getUserUniqueToken().equals(userUniqueToken)) {
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
		// Get the battleship template while also making sure it belongs to the current user
		BattleshipTemplate battleshipTemplate
			= getUserBattleshipTemplate(userUniqueToken, battleshipTemplateUniqueToken);

		// Get subsystems
		List<BattleshipTemplateSubsystem> existingSubsystems
			= getBattleshipTemplateSubsystems(userUniqueToken, battleshipTemplateUniqueToken);

		// Validate subsystems placement
		validateSubsystemPlacementOnHull(battleshipTemplate, posX, posY);
		validateSubsystemPlacementInRelationToOtherSubsystems(null, posX, posY, existingSubsystems);

		// Get subsystem reference for the subsystem to be added
		SubsystemRef subsystemRef = getSubsystemRef(subsystemRefUniqueToken);

		// The following operations must be executed within a data source-specific transaction.
		// The result of the transaction is held in the newSubsystemWorkaround variable.
		final List<BattleshipTemplateSubsystem> newSubsystemWorkaround = new ArrayList<>();
		dataService.executeTransaction(() -> {
			// Add the new subsystem
			newSubsystemWorkaround.add(
				dataService.addBattleshipTemplateSubsystem(
					uniqueTokenProvider.provide(),
					battleshipTemplate, subsystemRef, posX, posY
				)
			);

			// Re-compute the cost
			existingSubsystems.add(newSubsystemWorkaround.get(0));
			computeBattleshipTemplateStats(battleshipTemplate, existingSubsystems);
			
			// Save the battleship template
			dataService.saveBattleshipTemplate(battleshipTemplate);
		});

		// Set the battleship template with the re-computed statistics
		// to the newly added subsystem, so that the calling service
		// gets accurate data.
		BattleshipTemplateSubsystem ret = newSubsystemWorkaround.get(0);
		ret.setBattleshipTemplate(battleshipTemplate);

		// return the newly added subsystem data object
		return ret;
	}

	public BattleshipTemplateSubsystem updateBattleshipTemplateSubsystemPosition(
		String userUniqueToken,
		String subsystemUniqueToken,
		int posX, int posY
	) {
		// Get the subsystem
		BattleshipTemplateSubsystem subsystem
			= getBattleshipTemplateSubsystem(userUniqueToken, subsystemUniqueToken);

		// Set the new subsystem position
		subsystem.setPosition(new Point2I(posX, posY));

		// Get all the subsystems
		@SuppressWarnings("unchecked")
		List<BattleshipTemplateSubsystem> allSubsystems
			= (List<BattleshipTemplateSubsystem>) dataService.getBattleshipTemplateSubsystems(subsystem.getBattleshipTemplate());

		// Validate subsystems placement
		validateSubsystemPlacementOnHull(subsystem.getBattleshipTemplate(), posX, posY);
		validateSubsystemPlacementInRelationToOtherSubsystems(subsystemUniqueToken, posX, posY, allSubsystems);

		// Save the subsystem and return a reference
		return dataService.updateBattleshipTemplateSubsystem(subsystem);
	}

	public BattleshipTemplate deleteBattleshipTemplateSubsystem(
		String userUniqueToken,
		String subsystemUniqueToken
	) {
		// Get the subsystem
		BattleshipTemplateSubsystem subsystem
			= getBattleshipTemplateSubsystem(userUniqueToken, subsystemUniqueToken);

		// The following operations need to be executed within a transaction
		dataService.executeTransaction(() -> {
			// Delete the subsystem
			dataService.deleteBattleshipTemplateSubsystem(subsystem);

			// Get all subsystems
			@SuppressWarnings("unchecked")
			List<BattleshipTemplateSubsystem> allSubsystems
				= (List<BattleshipTemplateSubsystem>) dataService.getBattleshipTemplateSubsystems(subsystem.getBattleshipTemplate());

			// Re-compute the cost
			computeBattleshipTemplateStats(subsystem.getBattleshipTemplate(), allSubsystems);

			// Save the battleship template
			dataService.saveBattleshipTemplate(subsystem.getBattleshipTemplate());
		});

		// Return the battleship template with the updated statistics
		return subsystem.getBattleshipTemplate();
	}

	@SuppressWarnings("unchecked")
	public List<SubsystemRef> getSubsystemRefs() {
		return (List<SubsystemRef>) dataService.getSubsystemRefs();
	}

	public void validateBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		validateBattleshipTemplateNotNull(battleshipTemplate);

		Point2I hullSize = battleshipTemplate.getHullSize();
		validateBattleshipHullSize(hullSize.x, hullSize.y);

		@SuppressWarnings("unchecked")
		List<BattleshipTemplateSubsystem> subsystems
			= (List<BattleshipTemplateSubsystem>) dataService.getBattleshipTemplateSubsystems(battleshipTemplate);

		validateSubsystems(battleshipTemplate, subsystems);
	}

	private void validateBattleshipTemplateNotNull(BattleshipTemplate battleshipTemplate) {
		if (battleshipTemplate == null) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Null reference provided for validation of battleship template"
			);
		}
	}

	private void validateSubsystems(
		BattleshipTemplate battleshipTemplate,
		List<BattleshipTemplateSubsystem> subsystems
	) {
		subsystems.forEach(subsystem -> {
			validateSubsystem(battleshipTemplate, subsystem, subsystems);
		});
	}

	private void validateSubsystem(
		BattleshipTemplate battleshipTemplate,
		BattleshipTemplateSubsystem subsystem,
		List<BattleshipTemplateSubsystem> allSubsystems
	) {
		Point2I pos = subsystem.getPosition();
		validateSubsystemPlacementOnHull(battleshipTemplate, pos.x, pos.y);
		validateSubsystemPlacementInRelationToOtherSubsystems(subsystem.getUniqueToken(), pos.x, pos.y, allSubsystems);
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

	private void validateSubsystemPlacementInRelationToOtherSubsystems(
		String battleshipTemplateUniqueToken,
		int posX, int posY,
		List<BattleshipTemplateSubsystem> allSubsystems
	) {
		int minDistanceFromSubsystem
			= system.getDataService().getIntParameter("SUBSYSTEM.DISTANCE_FROM_OTHERS");

		for (BattleshipTemplateSubsystem subsystem : allSubsystems) {
			if (battleshipTemplateUniqueToken == null || !(battleshipTemplateUniqueToken.equals(subsystem.getUniqueToken()))) {
				Point2I subPos = subsystem.getPosition();
				if (
				    (subPos.x == posX && subPos.y == posY)
				 || Math.sqrt(Math.pow(subPos.x - posX, 2) + Math.pow(subPos.y - posY, 2)) < minDistanceFromSubsystem
				) {
					throw new AdvancedBattleshipsInventoryValidationException(
						"Subsystem must be placed no closer than [" + minDistanceFromSubsystem + "] cells to any other subsystem"
					);
				}
			}
		}
	}

	private void validateBattleshipHullSize(int width, int height) {
		int maxCells = system.getDataService().getIntParameter("BATTLESHIP.MAX_SIZE");
		int minLength = system.getDataService().getIntParameter("BATTLESHIP.MIN_LENGTH_SIZE");

		if (width < minLength || height < minLength) {
			throw new AdvancedBattleshipsInventoryValidationException(
					"The minimum length on any axis is [" + minLength + "] cells."
				);
		}

		if (width * height > maxCells) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"Maximum hull size exceeded. Cannot have more than [" + maxCells + "] cells."
			);
		}
	}

	private void validateTemplateame(String templateName) {
		if (templateName == null || templateName.length() < 5) {
			throw new AdvancedBattleshipsInventoryValidationException(
				"The template name must be at least 5 characters long"
			);
		}
	}

	private void validateBattleshipTemplateOwnership(BattleshipTemplate bsTemplate, String userUniqueToken) {
		if (false == nullSafeSupplier(() -> bsTemplate.getUserUniqueToken().equals(userUniqueToken) , false) ) {
			throw new AdvancedBattleshipsInventorySecurityException("Cannot find the referenced battleship template");
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

	public BattleshipTemplate setBattleshipTemplateHullCellValue(String userUniqueToken, String battleshipTemplateUniqueToken, Integer x, Integer y, Boolean value) {
		// Find the battleship template (throw exception if not matched with the current user)
		BattleshipTemplate bsTemplate = getUserBattleshipTemplate(userUniqueToken, battleshipTemplateUniqueToken);

		// Set the cell value
		bsTemplate.getHull()[y][x] = value;

		// Get subsystems
		@SuppressWarnings("unchecked")
		List<BattleshipTemplateSubsystem> subsystems
			= (List<BattleshipTemplateSubsystem>) dataService.getBattleshipTemplateSubsystems(bsTemplate);

		// Check if the subsystems placement is still valid (if not, an exception will be thrown)
		validateSubsystems(bsTemplate, subsystems);

		// Compute the statistics
		computeBattleshipTemplateStats(bsTemplate, subsystems);

		// If all went well, save the battleship template and its new hull value
		dataService.saveBattleshipTemplate(bsTemplate);

		// Return the battleship template;
		return bsTemplate;
	}

	public BattleshipTemplate setBattleshipTemplateHull(String userUniqueToken, String battleshipTemplateUniqueToken, boolean[][] hull) {
		// Find the battleship template (throw exception if not matched with the current user)
		BattleshipTemplate bsTemplate = getUserBattleshipTemplate(userUniqueToken, battleshipTemplateUniqueToken);

		// Validate the hull size, to make sure all the values are there
		if (   nullSafeSupplier(() -> hull.length, -1) != bsTemplate.getHullSize().y
			|| nullSafeSupplier(() -> hull[0].length, -1) != bsTemplate.getHullSize().x
		) {
			throw new AdvancedBattleshipsInventoryValidationException(
					"The provided hull matrix is not consistent with the hull dimensions registered for this battleshpip template"
				);
		}

		// Set the hull
		bsTemplate.setHull(hull);

		// Get subsystems
		@SuppressWarnings("unchecked")
		List<BattleshipTemplateSubsystem> subsystems
			= (List<BattleshipTemplateSubsystem>) dataService.getBattleshipTemplateSubsystems(bsTemplate);

		// Check if the subsystems placement is still valid (if not, an exception will be thrown)
		validateSubsystems(bsTemplate, subsystems);

		// Re-compute the cost
		computeBattleshipTemplateStats(bsTemplate, subsystems);

		// If all went well, save the battleship template and its new hull value
		dataService.saveBattleshipTemplate(bsTemplate);

		// Return the battleship template
		return bsTemplate;
	}

	@SuppressWarnings("unchecked")
	public Iterable<SubsystemType> getSubsystemTypes() {
		return (Iterable<SubsystemType>) dataService.getSubsystemTypes();
	}

	@SuppressWarnings("unchecked")
	public Set<SubsystemRef> getSubsystemsByTypeName(String subsystemTypeName) {
		return (Set<SubsystemRef>) dataService.getSubsystemsByTypeName(subsystemTypeName);
	}

	private void computeBattleshipTemplateStats(
		BattleshipTemplate bsTemplate,
		Iterable<BattleshipTemplateSubsystem> subsystems
	) {
		// Initialize the statistics
		double cost = 0;
		double energy = 0;
		double firepower = 0;

		// Collect statistics from all subsystems
		for (BattleshipTemplateSubsystem subsystem : subsystems) {
			// Get the subsystemRef for shorter lines of code
			SubsystemRef subsystemRef = subsystem.getSubsystemRef();
			
			// The cost is collected from all subsystems
			cost += subsystemRef.getCost();

			// The energy is collected only from power systems and is computed
			// as the total power generated by all power systems
			if (subsystemRef.getType().getName().equals("power systems")) {
				Set<SubsystemRefGeneratedResourceSpec> generatedResources
					= subsystemRef.getGeneratedResources();

				if (generatedResources != null) {
					energy +=
						generatedResources.stream()
							.filter(gr -> gr.getResourceType().getName().equals("power"))
							.map(gr -> gr.getAmount())
							.reduce(0d, Double::sum);
				}
			}

			// The fire power is collected only from weapons systems and is computed
			// as the total energy consumed by all weapons systems
			if (subsystemRef.getType().getName().equals("weapons systems")) {
				Set<SubsystemRefGeneratedResourceRequirement> requiredGeneratedResources
					= subsystemRef.getGeneratedResourceRequirements();

				if (requiredGeneratedResources != null) {
					firepower +=
						requiredGeneratedResources.stream()
							.filter(rr -> rr.getResourceType().getName().equals("power"))
							.map(rr -> rr.getRequiredAmount())
							.reduce(0d, Double::sum);
				}
			}
		}

		// Compute the cost of the hull
		Double hullCellCost = system.getDataService().getDoubleParameter("BATTLESHIP.HULL_CELL_COST");
		for(boolean[] lines : bsTemplate.getHull()) {
			for (boolean cell : lines) {
				if (cell) {
					cost += hullCellCost;
				}
			}
		}

		// Apply the statistics to the battleship template
		bsTemplate.setCost(cost);
		bsTemplate.setEnergy(energy);
		bsTemplate.setFirepower(firepower);
	}
}
