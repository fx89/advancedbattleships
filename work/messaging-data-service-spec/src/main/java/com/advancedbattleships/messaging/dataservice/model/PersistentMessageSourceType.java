package com.advancedbattleships.messaging.dataservice.model;

import java.io.Serializable;

public interface PersistentMessageSourceType extends Serializable {
	String getName();

	void setName(String name);
}
