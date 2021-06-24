package com.advancedbattleships.content.dataservice.model;

import java.io.Serializable;

public interface UserAccessibleLogo extends Serializable {

	UserUiConfig getUser();

	void setUser(UserUiConfig user);

	String getLogoName();

	void setLogoName(String logoName);
}
