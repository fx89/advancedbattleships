package com.advancedbattleships.security.dataservice.model;

import java.io.Serializable;

public interface Authority extends Serializable {
	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);
}
