package com.advancedbattleships.inventory.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsInventoryValidationException extends AdvancedBattleshipsInventoryException {
	public AdvancedBattleshipsInventoryValidationException() {
		super();
	}

	public AdvancedBattleshipsInventoryValidationException(String message) {
		super(message);
	}

	public AdvancedBattleshipsInventoryValidationException(String message, Throwable source) {
		super(message, source);
	}
}
