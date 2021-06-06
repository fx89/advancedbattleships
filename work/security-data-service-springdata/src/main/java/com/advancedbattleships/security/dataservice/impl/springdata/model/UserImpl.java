package com.advancedbattleships.security.dataservice.impl.springdata.model;

import com.advancedbattleships.security.dataservice.model.Group;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserImpl implements User {
	private String name;

	private String pictureUrl;

	private String primaryEmailAddress;

	private Iterable<UserLoginSource> loginSources;

	private Iterable<Group> groups;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getPicutreUrl() {
		return pictureUrl;
	}

	@Override
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	@Override
	public String getPrimaryEmailAddress() {
		return primaryEmailAddress;
	}

	@Override
	public void setPrimaryEmailAddress(String primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}

	@Override
	public Iterable<UserLoginSource> getLoginSources() {
		return loginSources;
	}

	@Override
	public void setLoginSources(Iterable<UserLoginSource> loginSources) {
		this.loginSources = loginSources;
	}

	@Override
	public Iterable<Group> getGroups() {
		return groups;
	}

	@Override
	public void setGroups(Iterable<Group> groups) {
		this.groups = groups;
	}

}
