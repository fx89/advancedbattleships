package com.advancedbattleships.inventory.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.service.InventoryService;
import com.advancedbattleships.security.service.SecurityService;

@RestController
@RequestMapping("/inventory/battlesipTemplates")
public class BattleshipTemplatesInventoryRestController {

	@Autowired
	private SecurityService security;

	@Autowired
	private InventoryService inventory;

	@PostMapping("createNewBattleshipTemplate")
	public BattleshipTemplate createNewBattleshipTemplate(@RequestParam() String templateName, @RequestParam() int width, @RequestParam() int height) {
		return inventory.createNewBattleshipTemplate(
					security.getCurrentUser().getUniqueToken(),
					templateName, width, height
				);
	}

	@GetMapping("getUserBattleshipTemplates")
	public Iterable<BattleshipTemplate> getUserBattleshipTemplates() {
		return inventory.getUserBattleshipTemplates(security.getCurrentUser().getUniqueToken());
	}

	@PostMapping("setBattleshipTemplateHullCellValue")
	public void setBattleshipTemplateHullCellValue(
			@RequestParam() String battleshipTemplateUniqueToken,
			@RequestParam() Integer x,
			@RequestParam() Integer y,
			@RequestParam() Boolean value
	) {
		inventory.setBattleshipTemplateHullCellValue(
						security.getCurrentUser().getUniqueToken(),
						battleshipTemplateUniqueToken,
						x, y, value
					);
	}

	@PostMapping("setBattleshipTemplateHull")
	public void setBattleshipTemplateHull(
			@RequestParam() String battleshipTemplateUniqueToken,
			@RequestBody() boolean[][] hull
	) {
		inventory.setBattleshipTemplateHull(
						security.getCurrentUser().getUniqueToken(),
						battleshipTemplateUniqueToken,
						hull
					);
	}

	@DeleteMapping("deleteBattleshipTemplate")
	public void deleteBattleshipTemplate(@RequestBody() String uniqueToken) {
		inventory.deleteBattleshipTemplate(security.getCurrentUser().getUniqueToken(), uniqueToken);
	}
}
