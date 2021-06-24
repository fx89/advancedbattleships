package com.advancedbattleships.content.service.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsContentServiceException extends RuntimeException {

	public AdvancedBattleshipsContentServiceException() {
		super();
	}

	public AdvancedBattleshipsContentServiceException(String message) {
		super(message);
	}

	public AdvancedBattleshipsContentServiceException(String message, Throwable source) {
		super(message, source);
	}

}
