package com.advancedbattleships.social.dataservice.impl.springdata.model;

import java.util.Date;

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

import com.advancedbattleships.messaging.dataservice.model.PersistentMessage;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageChannel;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageSourceType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "PERSISTENT_MESSAGE")
@AllArgsConstructor
@NoArgsConstructor
public class PersistentMessageImpl implements PersistentMessage {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CHANNEL_ID")
	@Fetch(FetchMode.JOIN)
	private PersistentMessageChannelImpl channel;

	@Column(name = "USER_UNIQUE_TOKEN")
	private String userUniqueToken;

	@Column(name = "SOURCE_UNIQUE_TOKEN")
	private String sourceUniqueToken;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SOURCE_TYPE_ID")
	@Fetch(FetchMode.JOIN)
	private PersistentMessageSourceTypeImpl sourceType;

	@Column(name = "MESSAGE_TIME")
	private Date messageTime;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "BODY")
	private String body;

	@Column(name = "IS_IMPORTANT")
	private Boolean isImportant;

	@Column(name = "IS_READ")
	private Boolean isRead;

	@Override
	public PersistentMessageChannel getChannel() {
		return channel;
	}

	@Override
	public void setChannel(PersistentMessageChannel channel) {
		this.channel = new PersistentMessageChannelImpl(channel);
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
	public String getSourceUniqueToken() {
		return sourceUniqueToken;
	}

	@Override
	public void setSourceUniqueToken(String sourceUniqueToken) {
		this.sourceUniqueToken = sourceUniqueToken;
	}

	@Override
	public PersistentMessageSourceType getSourceType() {
		return sourceType;
	}

	@Override
	public void setSourceType(PersistentMessageSourceType sourceType) {
		this.sourceType = new PersistentMessageSourceTypeImpl(sourceType);
	}

	@Override
	public Date getMessageTime() {
		return messageTime;
	}

	@Override
	public void setMessageTime(Date messageTime) {
		this.messageTime = messageTime;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public Boolean isImportant() {
		return isImportant;
	}

	@Override
	public void setImportant(Boolean isImportant) {
		this.isImportant = isImportant;
	}

	@Override
	public Boolean isRead() {
		return isRead;
	}

	@Override
	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public PersistentMessageImpl(PersistentMessage source) {
		this.setBody(source.getBody());
		this.setChannel(source.getChannel());
		this.setImportant(source.isImportant());
		this.setMessageTime(source.getMessageTime());
		this.setRead(source.isRead());
		this.setSourceType(source.getSourceType());
		this.setSourceUniqueToken(source.getSourceUniqueToken());
		this.setTitle(source.getTitle());
		this.setUserUniqueToken(source.getUserUniqueToken());

		if (source instanceof PersistentMessageImpl) {
			this.id = ((PersistentMessageImpl) source).id;
		}
	}
}
