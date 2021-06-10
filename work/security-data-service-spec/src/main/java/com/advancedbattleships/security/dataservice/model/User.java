package com.advancedbattleships.security.dataservice.model;

import java.io.Serializable;
import java.util.Set;

public interface User extends Serializable {

	String getUniqueToken();

	void setUniqueToken(String uniqueToken);

	String getName();

	void setName(String name);

	String getPicutreUrl();

	void setPictureUrl(String pictureUrl);

	String getPrimaryEmailAddress();

	void setPrimaryEmailAddress(String primaryEmailAddress);

	Set<Group> getGroups();

	void setGroups(Set<Group> groups);
}
