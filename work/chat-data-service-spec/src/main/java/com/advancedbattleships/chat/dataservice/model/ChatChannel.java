package com.advancedbattleships.chat.dataservice.model;

import java.io.Serializable;

public interface ChatChannel extends Serializable {

	String getName();

	void setName(String name);

	String getPartyUniqueToken();

	void setPartyUniqueToken(String partyUniqueToken);

	Boolean isPrivate();

	void setPrivate(Boolean isPrivate);

}
