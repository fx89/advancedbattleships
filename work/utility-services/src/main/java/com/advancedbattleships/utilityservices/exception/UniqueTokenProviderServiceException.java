package com.advancedbattleships.utilityservices.exception;

public class UniqueTokenProviderServiceException extends AdvancedBattleshipsUtilityServiceException {

	private static final long serialVersionUID = 6615765068537815624L;

	public UniqueTokenProviderServiceException() {
		super();
	}

	public UniqueTokenProviderServiceException(String message) {
		super(message);
	}

	public UniqueTokenProviderServiceException(String message, Throwable source) {
		super(message, source);
	}
}
