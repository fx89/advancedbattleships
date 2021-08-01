package com.advancedbattleships.chat.dataservice.model;

import java.io.Serializable;

public interface ChatChannelBan extends Serializable {

	ChatChannel getChatChannel();

	void setChatChannel(ChatChannel chatChannel);

	String getUserUniqueToken();

	void setUserUniqueToken(String userUniqueToken);

	Boolean isPermanent();

	void setPermanent(Boolean isPermanent);

	Long getTimeTillLiftedMins();

	void setTimeTillLiftedMins(Long timeTillLiftedMins);
}
