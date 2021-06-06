package com.advancedbattleships.security.dataservice.model;

public interface Group {
	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

	Iterable<Authority> getAuthorities();

	void setAuthorities(Iterable<Authority> authorities);
}
