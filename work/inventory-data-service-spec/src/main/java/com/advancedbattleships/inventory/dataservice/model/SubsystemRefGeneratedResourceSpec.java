package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;

public interface SubsystemRefGeneratedResourceSpec extends Serializable {

	GeneratedResourceType getResourceType();

	void setResourceType(GeneratedResourceType resourceType);

	double getAmount();

	void setAmount(double amount);
}
