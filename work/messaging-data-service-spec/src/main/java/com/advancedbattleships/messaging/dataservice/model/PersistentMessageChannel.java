package com.advancedbattleships.messaging.dataservice.model;

import java.io.Serializable;

public interface PersistentMessageChannel extends Serializable {
	String getName();

	void setName(String name);

	PersistentMessageType getMessageType();

	void setMessageType(PersistentMessageType messageType);
}
