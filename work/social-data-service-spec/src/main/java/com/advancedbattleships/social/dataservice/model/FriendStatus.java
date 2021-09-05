package com.advancedbattleships.social.dataservice.model;

import java.io.Serializable;

public interface FriendStatus extends Serializable {

	String getName();

	void setName(String name);

	boolean equals(FriendStatus other);

}
