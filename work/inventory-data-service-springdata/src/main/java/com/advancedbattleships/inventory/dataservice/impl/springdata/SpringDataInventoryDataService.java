package com.advancedbattleships.inventory.dataservice.impl.springdata;

import static com.advancedbattleships.common.lang.Multicast.multicastList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.inventory.dataservice.InventoryDataService;
import com.advancedbattleships.inventory.dataservice.impl.springdata.dao.BattleshipTemplateSubsystemsRepository;
import com.advancedbattleships.inventory.dataservice.impl.springdata.dao.BattleshipTemplatesRepository;
import com.advancedbattleships.inventory.dataservice.impl.springdata.dao.SubsystemRefsRepository;
import com.advancedbattleships.inventory.dataservice.impl.springdata.exception.AdvancedBattleshipsSpringDataInventoryDataServiceException;
import com.advancedbattleships.inventory.dataservice.impl.springdata.model.BattleshipTemplateImpl;
import com.advancedbattleships.inventory.dataservice.impl.springdata.model.BattleshipTemplateSubsystemImpl;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.Point2I;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;
import com.advancedbattleships.utilityservices.UniqueTokenProviderService;

@Service
public class SpringDataInventoryDataService implements InventoryDataService {

	@Autowired
	private UniqueTokenProviderService uniqueTokenProvider;

	@Autowired
	private BattleshipTemplatesRepository battleshipTemplatesRepository;

	@Autowired
	private BattleshipTemplateSubsystemsRepository battleshipTemplateSubsystemsRepository;

	@Autowired
	private SubsystemRefsRepository subsystemRefsRepository;

	@Override
	public BattleshipTemplate createEmptyBattleshipTemplate(
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
		ret.setUniqueToken(uniqueTokenProvider.provide());

		return battleshipTemplatesRepository.save(ret);
	}

	@Override
	public void deleteBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		battleshipTemplatesRepository.delete(castBattleshipTemplate(battleshipTemplate));
	}

	@Override
	public List<BattleshipTemplate> getUserBattleshipTemplates(String userUniqueToken) {
		return multicastList(battleshipTemplatesRepository.findAllByUserUniqueToken(userUniqueToken));
	}

	@Override
	public BattleshipTemplate getBattleshipTemplateByUniqueToken(String uniqueToken) {
		return battleshipTemplatesRepository.findFirstByUniqueToken(uniqueToken);
	}

	@Override
	public List<BattleshipTemplateSubsystem> getBattleshipTemplateSubsystems(BattleshipTemplate battleshipTemplate) {
		BattleshipTemplateImpl t = castBattleshipTemplate(battleshipTemplate);
		return multicastList(battleshipTemplateSubsystemsRepository.findAllByBattleshipTemplateId(t.getId()));
	}

	@Override
	public BattleshipTemplateSubsystem addBattleshipTemplateSubsystem(
			BattleshipTemplate battleshipTemplate,
			SubsystemRef subsystemRef,
			int posX, int posY
	) {
		BattleshipTemplateImpl t = castBattleshipTemplate(battleshipTemplate);

		BattleshipTemplateSubsystemImpl ret = new BattleshipTemplateSubsystemImpl();
		ret.setBattleshipTemplate(t);
		ret.setPosition(new Point2I(posX, posY));
		ret.setSubsystemRef(subsystemRef);
		ret.setUniqueToken(uniqueTokenProvider.provide());

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
	public List<SubsystemRef> getSubsystemRefs() {
		return multicastList(subsystemRefsRepository.findAll());
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
}
