package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;
import java.util.Set;

public interface SubsystemRef extends Serializable {

	String getName();

	void setName(String name);

	SubsystemType getType();

	void setType(SubsystemType type);

	double getCost();

	void setCost(double cost);

	Set<SubbsystemRefStoredResourceRequirement> getStoredResourceRequirements();

	void setStoredResourceRequirements(Set<SubbsystemRefStoredResourceRequirement> storedResourceRequirements);

	Set<SubsystemRefStorage> getStorage();

	void setStorage(Set<SubsystemRefStorage> storage);

	Set<SubbsystemRefGeneratedResourceRequirement> getGeneratedResourceRequirements();

	void setGeneratedResourceRequirements(Set<SubbsystemRefGeneratedResourceRequirement> generatedResourceRequirements);

	Set<SubsystemRefGeneratedResourceSpec> getGeneratedResources();

	void setGeneratedResources(Set<SubsystemRefGeneratedResourceSpec> generatedResources);
}
