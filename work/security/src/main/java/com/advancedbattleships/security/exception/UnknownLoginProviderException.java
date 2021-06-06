package com.advancedbattleships.security.exception;

@SuppressWarnings("serial")
public class UnknownLoginProviderException extends AdvancedBattleshipsSecurityException {
	public UnknownLoginProviderException() {
		super();
	}

	public UnknownLoginProviderException(String message) {
		super(message);
	}

	public UnknownLoginProviderException(String message, Throwable source) {
		super(message, source);
	}
}
