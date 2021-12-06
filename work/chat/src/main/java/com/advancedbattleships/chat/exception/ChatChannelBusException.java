package com.advancedbattleships.chat.exception;

@SuppressWarnings("serial")
public class ChatChannelBusException extends ChatServiceException {

	public ChatChannelBusException() {
		super();
	}

	public ChatChannelBusException(String message) {
		super(message);
	}

	public ChatChannelBusException(String message, Throwable cause) {
		super(message, cause);
	}

}
