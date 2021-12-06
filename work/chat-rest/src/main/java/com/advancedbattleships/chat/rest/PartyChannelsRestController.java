package com.advancedbattleships.chat.rest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.chat.ChatService;
import com.advancedbattleships.chat.model.ChatMessage;
import com.advancedbattleships.security.service.SecurityService;

@RestController
@RequestMapping("/chat")
public class PartyChannelsRestController {

	@Autowired
	private ChatService chat;

	@Autowired
	SecurityService security;

	@GetMapping("getPartyChannelMessages")
	public Collection<ChatMessage> getPartyChannelMessages(@RequestParam String partyUniqueToken) {
		return chat.getChatMessages(security.getCurrentUser().getUniqueToken(), partyUniqueToken);
	}

	@GetMapping("getNewPartyChannelMessages")
	public Collection<ChatMessage> getNewPartyChannelMessages(@RequestParam String partyUniqueToken, @RequestParam Long lastRegisteredMessageId) {
		return chat.getNewChatMessages(security.getCurrentUser().getUniqueToken(), partyUniqueToken, lastRegisteredMessageId);
	}

	@PostMapping("addChatMessageToPartyChannel")
	public void addChatMessageToPartyChannel(@RequestParam String partyUniqueToken, @RequestParam String message) {
		chat.addChatMessage(security.getCurrentUser().getUniqueToken(), partyUniqueToken, message);
	}
}
