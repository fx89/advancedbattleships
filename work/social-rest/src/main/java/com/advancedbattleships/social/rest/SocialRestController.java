package com.advancedbattleships.social.rest;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.security.service.SecurityService;
import com.advancedbattleships.social.service.SocialService;
import com.advancedbattleships.social.service.model.Friend;

@RestController
@RequestMapping("/social")
public class SocialRestController {
	@Autowired
	SocialService socialService;

	@Autowired
	SecurityService security;

	@GetMapping("/getCurrentUserFriends")
	public Set<Friend> getCurrentUserFriends() {
		return socialService.getUserFriends(security.getCurrentUser().getUniqueToken());
	}

	@PostMapping("/inviteFriend")
	public void inviteFriend(@RequestParam() String nickName) {
		socialService.inviteFriend(security.getCurrentUser(), nickName);
	}

	@PostMapping("/rejectFriendRequest")
	public void rejectFriendRequest(@RequestParam() String friendUserUniqueToken) {
		socialService.rejectFriendRequest(friendUserUniqueToken, security.getCurrentUser());
	}

	@PostMapping("/acceptFriendRequest")
	public void acceptFriendRequest(@RequestParam() String friendUserUniqueToken) {
		socialService.acceptFriendRequest(friendUserUniqueToken, security.getCurrentUser());
	}

	@GetMapping("/getUnattendedUserFriendInvitations")
	public Set<Friend> getUnattendedUserFriendInvitations() {
		return socialService.getUnattendedUserFriendInvitations(security.getCurrentUser());
	}
}
