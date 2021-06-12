package com.advancedbattleships.inventory.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsInventoryException extends RuntimeException {

	public AdvancedBattleshipsInventoryException() {
		super();
	}

	public AdvancedBattleshipsInventoryException(String message) {
		super(message);
	}

	public AdvancedBattleshipsInventoryException(String message, Throwable source) {
		super(message, source);
	}
}
