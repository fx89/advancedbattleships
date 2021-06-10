package com.advancedbattleships.utilityservices.exception;

public class AdvancedBattleshipsUtilityServiceException extends RuntimeException {

	private static final long serialVersionUID = 3699084935936395859L;

	public AdvancedBattleshipsUtilityServiceException() {
		super();
	}

	public AdvancedBattleshipsUtilityServiceException(String message) {
		super(message);
	}

	public AdvancedBattleshipsUtilityServiceException(String message, Throwable source) {
		super(message, source);
	}
}
