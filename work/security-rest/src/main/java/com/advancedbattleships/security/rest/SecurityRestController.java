package com.advancedbattleships.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.service.SecurityService;

@RestController
@RequestMapping("/security")
public class SecurityRestController {

	@Autowired
	SecurityService security;

//	@PreAuthorize("hasAuthority('TEST_AUTHORITY')")

	@GetMapping("getCurrentUser")
	public User getCurrentUser() {
		return security.getCurrentUser();
	}

	@PostMapping("setCurrentUserNickName")
	public User setCurrentUserNickName(@RequestParam() String userNickName) {
		return security.setCurrentUserNickName(userNickName);
	}
}
