package com.advancedbattleships.social.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.messaging.dataservice.model.PersistentMessageSourceType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "PERSISTENT_MESSAGE_SOURCE_TYPE")
@AllArgsConstructor
@NoArgsConstructor
public class PersistentMessageSourceTypeImpl implements PersistentMessageSourceType {

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

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public PersistentMessageSourceTypeImpl(PersistentMessageSourceType source) {
		setName(source.getName());

		if (source instanceof PersistentMessageSourceTypeImpl) {
			this.id = ((PersistentMessageSourceTypeImpl) source).id;
		}
	}
}
