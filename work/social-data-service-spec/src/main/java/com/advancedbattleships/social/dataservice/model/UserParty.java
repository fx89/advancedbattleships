package com.advancedbattleships.social.dataservice.model;

import java.io.Serializable;

public interface UserParty extends Serializable {
	String getUserUniqueToken();

	void setUserUniqueToken(String userUniqueToken);

	Party getParty();

	void setParty(Party party);

	FriendStatus getStatus();

	void setStatus(FriendStatus status);
}
