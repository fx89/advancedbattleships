package com.advancedbattleships.security.dataservice.model;

public interface User {

	String getName();

	void setName(String name);

	String getPicutreUrl();

	void setPictureUrl(String pictureUrl);

	String getPrimaryEmailAddress();

	void setPrimaryEmailAddress(String primaryEmailAddress);

	Iterable<UserLoginSource> getLoginSources();

	void setLoginSources(Iterable<UserLoginSource> loginSources);

	Iterable<Group> getGroups();

	void setGroups(Iterable<Group> groups);
}
