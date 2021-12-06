package com.advancedbattleships.chat.dataservice.model;

import java.io.Serializable;
import java.util.Date;

public interface ChatChannelBan extends Serializable {

	ChatChannel getChatChannel();

	void setChatChannel(ChatChannel chatChannel);

	String getUserUniqueToken();

	void setUserUniqueToken(String userUniqueToken);

	Boolean isPermanent();

	void setPermanent(Boolean isPermanent);

	Date getTimeWhenLifted();

	void setTimeWhenLifted(Date timeWhenLifted);
}
