package com.advancedbattleships.userprofile.rest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

//	@PreAuthorize("hasAuthority('TEST_AUTHORITY')")
	@GetMapping("handling")
	public Object handling() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
