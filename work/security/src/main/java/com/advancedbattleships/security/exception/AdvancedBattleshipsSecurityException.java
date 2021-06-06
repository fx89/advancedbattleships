package com.advancedbattleships.security.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsSecurityException extends RuntimeException {
	public AdvancedBattleshipsSecurityException() {
		super();
	}

	public AdvancedBattleshipsSecurityException(String message) {
		super(message);
	}

	public AdvancedBattleshipsSecurityException(String message, Throwable source) {
		super(message, source);
	}
}
