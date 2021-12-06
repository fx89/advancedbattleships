package com.advancedbattleships.chat.exception;

@SuppressWarnings("serial")
public class ChatChannelBusAuthorizationException extends ChatChannelBusInitializationException {

	public ChatChannelBusAuthorizationException() {
		super();
	}

	public ChatChannelBusAuthorizationException(String message) {
		super(message);
	}

	public ChatChannelBusAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

}
