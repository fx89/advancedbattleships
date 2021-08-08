package com.advancedbattleships.social.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsSocialException extends RuntimeException {
	public AdvancedBattleshipsSocialException() {
		super();
	}

	public AdvancedBattleshipsSocialException(String message) {
		super(message);
	}

	public AdvancedBattleshipsSocialException(String message, Throwable cause) {
		super(message, cause);
	}
}
