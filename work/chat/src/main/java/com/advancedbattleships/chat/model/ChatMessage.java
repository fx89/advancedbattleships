package com.advancedbattleships.chat.model;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessage {

	private Long id;

	private String fromUserUniqueToken;

	private Date timestamp;

	private String message;

	public ChatMessage(Long id, String fromUserUniqueToken, Date timestamp, String message) {
		this.id = id;
		this.fromUserUniqueToken = fromUserUniqueToken;
		this.timestamp = timestamp;
		this.message = message;
	}
}
