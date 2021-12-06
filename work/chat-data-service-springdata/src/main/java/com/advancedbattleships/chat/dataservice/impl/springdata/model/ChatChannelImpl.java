package com.advancedbattleships.chat.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.chat.dataservice.model.ChatChannel;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "CHAT_CHANNEL")
@AllArgsConstructor
@NoArgsConstructor
public class ChatChannelImpl implements ChatChannel {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PARTY_UNIQUE_TOKEN")
	private String partyUniqueToken;

	@Column(name = "IS_PRIVATE")
	private Boolean isPrivate;

	public Long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getPartyUniqueToken() {
		return partyUniqueToken;
	}

	@Override
	public void setPartyUniqueToken(String partyUniqueToken) {
		this.partyUniqueToken = partyUniqueToken;
	}

	@Override
	public Boolean isPrivate() {
		return isPrivate;
	}

	@Override
	public void setPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public ChatChannelImpl(ChatChannel source) {
		this.setName(source.getName());
		this.setPartyUniqueToken(source.getPartyUniqueToken());
		this.setPrivate(source.isPrivate());

		if (source instanceof ChatChannelImpl) {
			this.id = ((ChatChannelImpl) source).id;
		}
	}
}
