package com.advancedbattleships.userstatustracker.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.security.service.SecurityService;
import com.advancedbattleships.userstatustracker.service.UserStatusTrackerService;

@RestController
@RequestMapping("/userstatustracker")
public class UserStatusTrackerRestController {

	@Autowired
	private UserStatusTrackerService userStatusTracker;

	@Autowired
	private SecurityService security;

	@GetMapping("ping")
	public void ping() {
		if (security.isUserAuthenticated()) {
			userStatusTracker.pingUser(security.getCurrentUser().getUniqueToken());
		}
	}
}
