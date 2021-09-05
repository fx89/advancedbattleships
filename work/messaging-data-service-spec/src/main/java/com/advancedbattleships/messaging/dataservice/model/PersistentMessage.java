package com.advancedbattleships.messaging.dataservice.model;

import java.io.Serializable;
import java.util.Date;

public interface PersistentMessage extends Serializable {
	PersistentMessageChannel getChannel();

	void setChannel(PersistentMessageChannel channel);

	String getUserUniqueToken();

	void setUserUniqueToken(String userUniqueToken);

	String getSourceUniqueToken();

	void setSourceUniqueToken(String sourceUniqueToken);

	PersistentMessageSourceType getSourceType();

	void setSourceType(PersistentMessageSourceType sourceType);

	Date getMessageTime();

	void setMessageTime(Date messageTime);

	String getTitle();

	void setTitle(String title);

	String getBody();

	void setBody(String body);

	Boolean isImportant();

	void setImportant(Boolean isImportant);

	Boolean isRead();

	void setRead(Boolean isRead);

	PersistentMessageType getMessageType();

	void setMessageType(PersistentMessageType messageType);

	Boolean isUserNotified();

	void setUserNotified(Boolean isUserNotified);
}
