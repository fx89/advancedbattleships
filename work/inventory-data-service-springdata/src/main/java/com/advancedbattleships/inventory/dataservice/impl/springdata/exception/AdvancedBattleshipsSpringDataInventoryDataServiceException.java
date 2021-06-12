package com.advancedbattleships.inventory.dataservice.impl.springdata.exception;

@SuppressWarnings("serial")
public class AdvancedBattleshipsSpringDataInventoryDataServiceException extends RuntimeException {

	public AdvancedBattleshipsSpringDataInventoryDataServiceException() {
		super();
	}

	public AdvancedBattleshipsSpringDataInventoryDataServiceException(String message) {
		super(message);
	}

	public AdvancedBattleshipsSpringDataInventoryDataServiceException(String message, Throwable source) {
		super(message, source);
	}

}
