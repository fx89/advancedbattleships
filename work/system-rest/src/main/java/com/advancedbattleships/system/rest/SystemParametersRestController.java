package com.advancedbattleships.system.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.system.SystemService;
import com.advancedbattleships.system.model.MessagingProperties;

@RestController
@RequestMapping("/system/parameters")
public class SystemParametersRestController {

	@Autowired
	private SystemService system;

	@GetMapping("getBattleshipHullCellCost")
	public Double getBattleshipHullCellCost() {
		return system.getDataService().getDoubleParameter("BATTLESHIP.HULL_CELL_COST");
	}

	@GetMapping("getSessionPingSecs")
	public Integer getSessionPingSecs() {
		return system.getDataService().getIntParameter("SECURITY.SESSION_PING_SECS");
	}

	@GetMapping("getMessagingProperties")
	public MessagingProperties getMessagingProperties() {
		return system.getMessagingProperties();
	}
}
