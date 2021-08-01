package com.advancedbattleships.social.dataservice.model;

import java.io.Serializable;

public interface UserFriend extends Serializable {
	String getUserUniqueToken();

	void setUserUniqueToken(String userUniqueToken);

	String getFriendUserUniqueToken();

	void setFriendUserUniqueToken(String friendUserUniqueToken);
}
