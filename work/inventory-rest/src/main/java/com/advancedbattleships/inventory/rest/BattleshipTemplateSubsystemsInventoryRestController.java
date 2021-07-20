package com.advancedbattleships.inventory.rest;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;
import com.advancedbattleships.inventory.dataservice.model.SubsystemType;
import com.advancedbattleships.inventory.service.InventoryService;
import com.advancedbattleships.security.service.SecurityService;

@RestController
@RequestMapping("/inventory/battlesipTemplateSubsystems")
public class BattleshipTemplateSubsystemsInventoryRestController {

	@Autowired
	private SecurityService security;

	@Autowired
	private InventoryService inventory;

	@GetMapping("getSubsystemTypes")
	public Iterable<SubsystemType> getSubsystemTypes() {
		return inventory.getSubsystemTypes();
	}

	@GetMapping("getSubsystemsByTypeName")
	public Set<SubsystemRef> getSubsystemsByTypeName(@RequestParam() String subsystemTypeName) {
		return inventory.getSubsystemsByTypeName(subsystemTypeName);
	}

	@GetMapping("getSubsystemRefs")
	public List<SubsystemRef> getSubsystems() {
		return inventory.getSubsystemRefs();
	}

	@GetMapping("getBattleshipTemplateSubsystems")
	public List<BattleshipTemplateSubsystem> getBattleshipTemplateSubsystems(
		@RequestParam() String battleshipTemplateUniqueToken
	) {
		return inventory.getBattleshipTemplateSubsystems(
			security.getCurrentUser().getUniqueToken(),
			battleshipTemplateUniqueToken
		);
	}

	@PostMapping("addBattleshipTemplateSubsystem")
	public BattleshipTemplateSubsystem addBattleshipTemplateSubsystem(
		@RequestParam() String battleshipTemplateUniqueToken,
		@RequestParam() String subsystemRefUniqueToken,
		@RequestParam() Integer posX,
		@RequestParam() Integer posY
	) {
		return inventory.addBattleshipTemplateSubsystem(
				security.getCurrentUser().getUniqueToken(),
				battleshipTemplateUniqueToken,
				subsystemRefUniqueToken,
				posX, posY
			);
	}

	@DeleteMapping("deleteBattleshipTemplateSubsystem")
	public BattleshipTemplate deleteBattleshipTemplateSubsystem(@RequestParam() String subsystemUniqueToken) {
		return inventory.deleteBattleshipTemplateSubsystem(
				security.getCurrentUser().getUniqueToken(),
				subsystemUniqueToken
			);
	}
}
