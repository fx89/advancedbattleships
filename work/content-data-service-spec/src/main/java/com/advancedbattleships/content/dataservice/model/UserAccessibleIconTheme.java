package com.advancedbattleships.content.dataservice.model;

import java.io.Serializable;

public interface UserAccessibleIconTheme extends Serializable {

	UserUiConfig getUser();

	void setUser(UserUiConfig user);

	String getIconThemeName();

	void setIconThemeName(String iconThemeName);
}
