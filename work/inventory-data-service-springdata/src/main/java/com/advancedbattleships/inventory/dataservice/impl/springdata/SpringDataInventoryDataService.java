package com.advancedbattleships.inventory.dataservice.impl.springdata;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advancedbattleships.inventory.dataservice.InventoryDataService;
import com.advancedbattleships.inventory.dataservice.impl.springdata.dao.BattleshipTemplateSubsystemsRepository;
import com.advancedbattleships.inventory.dataservice.impl.springdata.dao.BattleshipTemplatesRepository;
import com.advancedbattleships.inventory.dataservice.impl.springdata.dao.SubsystemRefsRepository;
import com.advancedbattleships.inventory.dataservice.impl.springdata.dao.SubsystemTypesRepository;
import com.advancedbattleships.inventory.dataservice.impl.springdata.exception.AdvancedBattleshipsSpringDataInventoryDataServiceException;
import com.advancedbattleships.inventory.dataservice.impl.springdata.model.BattleshipTemplateImpl;
import com.advancedbattleships.inventory.dataservice.impl.springdata.model.BattleshipTemplateSubsystemImpl;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.GameWorld;
import com.advancedbattleships.inventory.dataservice.model.GameWorldCellType;
import com.advancedbattleships.inventory.dataservice.model.Point2I;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;
import com.advancedbattleships.inventory.dataservice.model.SubsystemType;

@Service
public class SpringDataInventoryDataService implements InventoryDataService {

	@Autowired
	private BattleshipTemplatesRepository battleshipTemplatesRepository;

	@Autowired
	private BattleshipTemplateSubsystemsRepository battleshipTemplateSubsystemsRepository;

	@Autowired
	private SubsystemRefsRepository subsystemRefsRepository;

	@Autowired
	private SubsystemTypesRepository subsystemTypesRepository;

	@Override
	public BattleshipTemplate createEmptyBattleshipTemplate(
		String uniqueToken,
		String userUniqueToken,
		String templateName,
		int hullSizeX,
		int hullSizeY
	) {
		BattleshipTemplateImpl ret = new BattleshipTemplateImpl();
		ret.setHullSize(hullSizeX, hullSizeY);
		ret.initEmptyHullArray();
		ret.setName(templateName);
		ret.setOfficialTemplate(false);
		ret.setPublic(false);
		ret.setVisibleInLists(false);
		ret.setUserUniqueToken(userUniqueToken);
		ret.setUniqueToken(uniqueToken);

		return battleshipTemplatesRepository.save(ret);
	}

	@Override
	public void deleteBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		battleshipTemplatesRepository.delete(castBattleshipTemplate(battleshipTemplate));
	}

	@Override
	public List<? extends BattleshipTemplate> getUserBattleshipTemplates(String userUniqueToken) {
		return battleshipTemplatesRepository.findAllByUserUniqueToken(userUniqueToken);
	}

	@Override
	public BattleshipTemplate getBattleshipTemplateByUniqueToken(String uniqueToken) {
		return battleshipTemplatesRepository.findFirstByUniqueToken(uniqueToken);
	}

	@Override
	public List<? extends BattleshipTemplateSubsystem> getBattleshipTemplateSubsystems(BattleshipTemplate battleshipTemplate) {
		BattleshipTemplateImpl t = castBattleshipTemplate(battleshipTemplate);
		return battleshipTemplateSubsystemsRepository.findAllByBattleshipTemplateId(t.getId());
	}

	@Override
	public BattleshipTemplateSubsystem addBattleshipTemplateSubsystem(
			String uniqueToken,
			BattleshipTemplate battleshipTemplate,
			SubsystemRef subsystemRef,
			int posX, int posY
	) {
		BattleshipTemplateImpl t = castBattleshipTemplate(battleshipTemplate);

		BattleshipTemplateSubsystemImpl ret = new BattleshipTemplateSubsystemImpl();
		ret.setBattleshipTemplate(t);
		ret.setPosition(new Point2I(posX, posY));
		ret.setSubsystemRef(subsystemRef);
		ret.setUniqueToken(uniqueToken);

		return battleshipTemplateSubsystemsRepository.save(ret);
	}

	@Override
	public BattleshipTemplateSubsystem getBattleshipTemplateSubsystemByUniqueToken(String uniqueToken) {
		return battleshipTemplateSubsystemsRepository.findFirstByUniqueToken(uniqueToken);
	}

	@Override
	public BattleshipTemplateSubsystem updateBattleshipTemplateSubsystem(BattleshipTemplateSubsystem subsystem) {
		return battleshipTemplateSubsystemsRepository.save(castBattleshipTemplateSubsystem(subsystem));
	}

	@Override
	public void deleteBattleshipTemplateSubsystem(BattleshipTemplateSubsystem subsystem) {
		battleshipTemplateSubsystemsRepository.delete(castBattleshipTemplateSubsystem(subsystem));
	}

	@Override
	public List<? extends SubsystemRef> getSubsystemRefs() {
		return subsystemRefsRepository.findAll();
	}

	private static BattleshipTemplateImpl castBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		if (battleshipTemplate instanceof BattleshipTemplateImpl) {
			return (BattleshipTemplateImpl) battleshipTemplate;
		} else {
			throw new AdvancedBattleshipsSpringDataInventoryDataServiceException(
					"Cannot handle the referenced BattleshipTemplate implementation"
				);
		}
	}

	private static BattleshipTemplateSubsystemImpl castBattleshipTemplateSubsystem(BattleshipTemplateSubsystem sys) {
		if (sys instanceof BattleshipTemplateSubsystemImpl) {
			return (BattleshipTemplateSubsystemImpl) sys;
		} else {
			throw new AdvancedBattleshipsSpringDataInventoryDataServiceException(
					"Cannot handle the referenced BattleshipTemplateSubsystem implementation"
				);
		}
	}

	@Override
	public SubsystemRef getSubsystemRefByUniqueToken(String uniqueToken) {
		return subsystemRefsRepository.findFirstByUniqueToken(uniqueToken);
	}

	@Override
	public void saveBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		battleshipTemplatesRepository.save(new BattleshipTemplateImpl(battleshipTemplate));
	}

	@Override
	public Iterable<? extends SubsystemType> getSubsystemTypes() {
		return subsystemTypesRepository.findAll();
	}

	@Override
	public Set<? extends SubsystemRef> getSubsystemsByTypeName(String subsystemTypeName) {
		return subsystemRefsRepository.findAllByTypeName(subsystemTypeName);
	}

	@Override
	public void deleteBattleshipTemplateSubsystems(String battleshipTemplateUniqueToken) {
		battleshipTemplateSubsystemsRepository
			.deleteByBattleshipTemplateUniqueToken(battleshipTemplateUniqueToken);
	}

	@Override
	@Transactional(transactionManager = "absInventoryTransactionManager")
	public void executeTransaction(Runnable transaction) {
		transaction.run();
	}

	@Override
	public Iterable<? extends GameWorld> getGameWorlds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameWorld saveGameWorld(GameWorld gameWorld) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends GameWorldCellType> getGameWorldSquareTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
