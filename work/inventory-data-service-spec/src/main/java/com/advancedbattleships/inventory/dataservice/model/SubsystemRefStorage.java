package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;

public interface SubsystemRefStorage extends Serializable {

	StoredResourceType getStoredResource();

	void setStoredResource(StoredResourceType storedResource);

	double getInitialAmount();

	void setInitialAmount(double initialAmount);
}
