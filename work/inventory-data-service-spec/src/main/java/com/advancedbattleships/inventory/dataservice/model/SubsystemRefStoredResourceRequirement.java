package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;

public interface SubsystemRefStoredResourceRequirement extends Serializable {

	StoredResourceType getStoredResource();

	void setStoredResource(StoredResourceType storedResource);

	double getRequiredAmountPerShot();

	void setRequiredAmountPerShot(double requiredAmountPerShot);
}
