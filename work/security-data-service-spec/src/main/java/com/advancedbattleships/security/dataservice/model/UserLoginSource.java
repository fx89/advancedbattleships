package com.advancedbattleships.security.dataservice.model;

import java.io.Serializable;

public interface UserLoginSource extends Serializable {

	LoginSource getLoginSource();

	void setLoginSource(LoginSource loginSource);

	String getLoginToken();

	void setLoginToken(String loginToken);

	User getUser();

	void setUser(User user);
}
