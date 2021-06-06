package com.advancedbattleships.security.dataservice.impl.springdata.model;

import com.advancedbattleships.security.dataservice.model.Authority;
import com.advancedbattleships.security.dataservice.model.Group;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class GroupImpl implements Group {
	private String name;

	private String description;

	private Iterable<Authority> authorities;

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

	@Override
	public Iterable<Authority> getAuthorities() {
		return authorities;
	}

	@Override
	public void setAuthorities(Iterable<Authority> authorities) {
		this.authorities = authorities;
	}

}
