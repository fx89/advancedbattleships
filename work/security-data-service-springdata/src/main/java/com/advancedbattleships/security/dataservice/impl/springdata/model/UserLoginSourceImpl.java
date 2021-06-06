package com.advancedbattleships.security.dataservice.impl.springdata.model;

import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserLoginSourceImpl implements UserLoginSource {
	private LoginSource loginSource;

	private String loginToken;

	private User user;

	@Override
	public LoginSource getLoginSource() {
		return loginSource;
	}

	@Override
	public void setLoginSource(LoginSource loginSource) {
		this.loginSource = loginSource;
	}

	@Override
	public String getLoginToken() {
		return loginToken;
	}

	@Override
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

}
