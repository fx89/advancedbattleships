package com.advancedbattleships.content.dataservice.model;

import java.io.Serializable;

public interface UserAccessibleStylesheet extends Serializable {

	UserUiConfig getUser();

	void setUser(UserUiConfig user);

	String getStylesheetName();

	void setStylesheetName(String stylesheetName);
}
