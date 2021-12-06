package com.advancedbattleships.chat.exception;

@SuppressWarnings("serial")
public class ChatServiceException extends RuntimeException {

	public ChatServiceException() {
		super();
	}

	public ChatServiceException(String message) {
		super(message);
	}

	public ChatServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
