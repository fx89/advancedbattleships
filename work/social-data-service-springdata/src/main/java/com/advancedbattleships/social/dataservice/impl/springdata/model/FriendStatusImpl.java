package com.advancedbattleships.social.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.social.dataservice.model.FriendStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "FRIEND_STATUS")
@AllArgsConstructor
@NoArgsConstructor
public class FriendStatusImpl implements FriendStatus {

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

	public FriendStatusImpl(FriendStatus source) {
		this.setName(source.getName());

		if (source instanceof FriendStatusImpl) {
			this.id = ((FriendStatusImpl) source).id;
		}
	}

	@Override
	public boolean equals(FriendStatus other) {
		if (other == null) {
			return false;
		}

		return this.name.equals(other.getName());
	}
}
