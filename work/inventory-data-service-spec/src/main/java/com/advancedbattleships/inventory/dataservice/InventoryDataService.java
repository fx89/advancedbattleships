package com.advancedbattleships.inventory.dataservice;

import java.util.List;

import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;

public interface InventoryDataService {

	/**
	 * Registers a new battleship template for the referenced user, with the given
	 * name and having all flags set to FALSE.<br />
	 * <br />
	 * The newly created battleship template will have a hull of the given size, but
	 * all the cells in the hull will be set to FALSE.<br />
	 * <br />
	 * This method does not validate the hull size. That's the job of the calling service.
	 */
	BattleshipTemplate createEmptyBattleshipTemplate(String uniqueToken, String userUniqueToken, String templateName, int hullSizeX, int hullSizeY);

	/**
	 * Removes the referenced battleship template and all of its subsystems.
	 */
	void deleteBattleshipTemplate(BattleshipTemplate battleshipTemplate);

	/**
	 * Returns a list of the battleship templates in the user's inventory
	 */
	List<BattleshipTemplate> getUserBattleshipTemplates(String userUniqueToken);

	/**
	 * Returns a reference to the battleship template having the given unique token
	 */
	BattleshipTemplate getBattleshipTemplateByUniqueToken(String uniqueToken);

	/**
	 * Returns a list of subsystems belonging to the referenced battleship template.
	 */
	List<BattleshipTemplateSubsystem> getBattleshipTemplateSubsystems(BattleshipTemplate battleshipTemplate);

	/**
	 * Adds a subsystem of the referenced type to the referenced battleship template
	 * at the specified position. This method does not validate the position. That's
	 * the job of the calling service.
	 */
	BattleshipTemplateSubsystem addBattleshipTemplateSubsystem(String uniqueToken, BattleshipTemplate battleshipTemplate, SubsystemRef subsystemRef, int posX, int posY);

	/**
	 * Returns the battleship template subsystem having the given unique token.
	 */
	BattleshipTemplateSubsystem getBattleshipTemplateSubsystemByUniqueToken(String uniqueToken);

	/**
	 * Saves the referenced subsystem. The subsystem must have already been added to
	 * a battleship template if the save is going to be successful. Otherwise, an
	 * exception is thrown.
	 */
	BattleshipTemplateSubsystem updateBattleshipTemplateSubsystem(BattleshipTemplateSubsystem subsystem);

	/**
	 * Deletes the referenced subsystem, removing it from the battleship template to
	 * which it was added.
	 */
	void deleteBattleshipTemplateSubsystem(BattleshipTemplateSubsystem subsystem);

	/**
	 * Returns a list of all available subsystem references to be used in a battleship
	 * template. These are defined by the admin and are not editable by users.
	 */
	List<SubsystemRef> getSubsystemRefs();

	/**
	 * Returns a the subsystem reference identified by the given unique token.
	 */
	SubsystemRef getSubsystemRefByUniqueToken(String uniqueToken);
}
