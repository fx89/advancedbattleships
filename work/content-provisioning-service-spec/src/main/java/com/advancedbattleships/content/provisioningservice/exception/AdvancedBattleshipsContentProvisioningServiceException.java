package com.advancedbattleships.content.provisioningservice.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsContentProvisioningServiceException extends RuntimeException {
	public AdvancedBattleshipsContentProvisioningServiceException() {
		super();
	}

	public AdvancedBattleshipsContentProvisioningServiceException(String message) {
		super(message);
	}

	public AdvancedBattleshipsContentProvisioningServiceException(String message, Throwable source) {
		super(message, source);
	}
}
