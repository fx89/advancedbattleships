package com.advancedbattleships.social.dataservice.model;

import java.io.Serializable;

public interface Party extends Serializable {
	String getPartyUniqueToken();

	void setPartyUniqueToken(String partyUniqueToken);

	String getName();

	void setName(String name);
}
