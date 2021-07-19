package com.advancedbattleships.inventory.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsInventorySecurityException extends AdvancedBattleshipsInventoryException {
	public AdvancedBattleshipsInventorySecurityException() {
		super();
	}

	public AdvancedBattleshipsInventorySecurityException(String message) {
		super(message);
	}

	public AdvancedBattleshipsInventorySecurityException(String message, Throwable cause) {
		super(message, cause);
	}
}
