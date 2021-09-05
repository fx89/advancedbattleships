package com.advancedbattleships.messaging.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsMessagingException extends RuntimeException {

	public AdvancedBattleshipsMessagingException() {
		super();
	}

	public AdvancedBattleshipsMessagingException(String message) {
		super(message);
	}

	public AdvancedBattleshipsMessagingException(String message, Throwable cause) {
		super(message, cause);
	}
}
