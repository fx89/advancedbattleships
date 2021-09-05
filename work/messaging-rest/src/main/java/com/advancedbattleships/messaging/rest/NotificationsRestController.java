package com.advancedbattleships.messaging.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.messaging.dataservice.model.PersistentMessage;
import com.advancedbattleships.messaging.service.MessagingService;
import com.advancedbattleships.security.service.SecurityService;



@RestController
@RequestMapping("/messaging/")
public class NotificationsRestController {
	@Autowired
	private MessagingService messaging;

	@Autowired
	private SecurityService security;

	@GetMapping(value="getUserNotifications")
	public List<PersistentMessage> getUserNotifications() {
		return messaging.getUserNotifications(security.getCurrentUser().getUniqueToken());
	}
}
