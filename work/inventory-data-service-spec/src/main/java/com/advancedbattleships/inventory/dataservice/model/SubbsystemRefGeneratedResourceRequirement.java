package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;

public interface SubbsystemRefGeneratedResourceRequirement extends Serializable {

	GeneratedResourceType getResourceType();

	void setResourceType(GeneratedResourceType resourceType);

	double getRequiredAmount();

	void setRequiredAmount(double requiredAmount);
}
