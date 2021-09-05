package com.advancedbattleships.messaging.dataservice.impl.springdata.model;

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

import com.advancedbattleships.messaging.dataservice.model.PersistentMessageChannel;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "PERSISTENT_MESSAGE_CHANNEL")
@AllArgsConstructor
@NoArgsConstructor
public class PersistentMessageChannelImpl implements PersistentMessageChannel {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MESSAGE_TYPE_ID")
	@Fetch(FetchMode.JOIN)
	private PersistentMessageTypeImpl messageType;

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
	public PersistentMessageType getMessageType() {
		return messageType;
	}

	@Override
	public void setMessageType(PersistentMessageType messageType) {
		this.messageType = new PersistentMessageTypeImpl(messageType);
	}

	public PersistentMessageChannelImpl(PersistentMessageChannel source) {
		this.setMessageType(source.getMessageType());
		this.setName(source.getName());

		if (source instanceof PersistentMessageChannelImpl) {
			this.id = ((PersistentMessageChannelImpl) source).id;
		}
	}
}
