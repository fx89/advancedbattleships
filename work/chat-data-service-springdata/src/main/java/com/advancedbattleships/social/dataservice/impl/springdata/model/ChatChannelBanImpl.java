package com.advancedbattleships.social.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.dataservice.model.ChatChannelBan;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "CHAT_CHANNEL_BAN")
@AllArgsConstructor
@NoArgsConstructor
public class ChatChannelBanImpl implements ChatChannelBan {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USER_UNIQUE_TOKEN")
	private String userUniqueToken;

	@Column(name = "IS_PERMANENT")
	private Boolean isPermanent;

	@Column(name = "TIME_TILL_LIFTED_MINS")
	private Long timeTillLiftedMins;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CHAT_CHANNEL_ID")
	@Fetch(FetchMode.JOIN)
	private ChatChannelImpl chatChannel;

	@Override
	public ChatChannel getChatChannel() {
		return chatChannel;
	}

	@Override
	public void setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = new ChatChannelImpl(chatChannel);
	}

	@Override
	public String getUserUniqueToken() {
		return userUniqueToken;
	}

	@Override
	public void setUserUniqueToken(String userUniqueToken) {
		this.userUniqueToken = userUniqueToken;
	}

	@Override
	public Boolean isPermanent() {
		return isPermanent;
	}

	@Override
	public void setPermanent(Boolean isPermanent) {
		this.isPermanent = isPermanent;
	}

	@Override
	public Long getTimeTillLiftedMins() {
		return timeTillLiftedMins;
	}

	@Override
	public void setTimeTillLiftedMins(Long timeTillLiftedMins) {
		this.timeTillLiftedMins = timeTillLiftedMins;
	}

	public ChatChannelBanImpl(ChatChannelBan source) {
		this.setChatChannel(source.getChatChannel());
		this.setPermanent(source.isPermanent());
		this.setTimeTillLiftedMins(source.getTimeTillLiftedMins());
		this.setUserUniqueToken(source.getUserUniqueToken());

		if (source instanceof ChatChannelBanImpl) {
			this.id = ((ChatChannelBanImpl) source).id;
		}
	}
}
