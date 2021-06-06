package com.advancedbattleships.security.dataservice.impl.springdata.model;

import com.advancedbattleships.security.dataservice.model.Authority;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthorityImpl implements Authority {

	private String name;

	private String description;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
