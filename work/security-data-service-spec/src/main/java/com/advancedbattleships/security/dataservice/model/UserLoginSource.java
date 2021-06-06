package com.advancedbattleships.security.dataservice.model;

public interface UserLoginSource {

	LoginSource getLoginSource();

	void setLoginSource(LoginSource loginSource);

	String getLoginToken();

	void setLoginToken(String loginToken);

	User getUser();

	void setUser(User user);
}
