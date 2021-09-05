package com.advancedbattleships.messaging.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.messaging.dataservice.model.PersistentMessageType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "PERSISTENT_MESSAGE_TYPE")
@AllArgsConstructor
@NoArgsConstructor
public class PersistentMessageTypeImpl implements PersistentMessageType {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Override
	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public PersistentMessageTypeImpl(PersistentMessageType source) {
		this.setName(source.getName());

		if (source instanceof PersistentMessageTypeImpl) {
			this.id = ((PersistentMessageTypeImpl) source).id;
		}
	}
}
