package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;
import java.util.Set;

public interface SubsystemRef extends Serializable {

	String getUniqueToken();

	void setUniqueToken(String uniqueToken);

	String getName();

	void setName(String name);

	SubsystemType getType();

	void setType(SubsystemType type);

	double getCost();

	void setCost(double cost);

	Set<SubsystemRefStoredResourceRequirement> getStoredResourceRequirements();

	void setStoredResourceRequirements(Set<SubsystemRefStoredResourceRequirement> storedResourceRequirements);

	Set<SubsystemRefStorage> getStorage();

	void setStorage(Set<SubsystemRefStorage> storage);

	Set<SubsystemRefGeneratedResourceRequirement> getGeneratedResourceRequirements();

	void setGeneratedResourceRequirements(Set<SubsystemRefGeneratedResourceRequirement> generatedResourceRequirements);

	Set<SubsystemRefGeneratedResourceSpec> getGeneratedResources();

	void setGeneratedResources(Set<SubsystemRefGeneratedResourceSpec> generatedResources);
}
