package com.advancedbattleships.messaging.dataservice.model;

import java.io.Serializable;

public interface PersistentMessageType extends Serializable {
	String getName();

	void setName(String name);
}
