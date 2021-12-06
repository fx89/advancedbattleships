package com.advancedbattleships.chat.exception;

@SuppressWarnings("serial")
public class ChatChannelBusInitializationException extends ChatChannelBusException {

	public ChatChannelBusInitializationException() {
		super();
	}

	public ChatChannelBusInitializationException(String message) {
		super(message);
	}

	public ChatChannelBusInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
