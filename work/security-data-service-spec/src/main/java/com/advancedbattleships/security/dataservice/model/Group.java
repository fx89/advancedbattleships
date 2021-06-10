package com.advancedbattleships.security.dataservice.model;

import java.io.Serializable;
import java.util.Set;

public interface Group extends Serializable {
	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

	Set<Authority> getAuthorities();

	void setAuthorities(Set<Authority> authorities);
}
