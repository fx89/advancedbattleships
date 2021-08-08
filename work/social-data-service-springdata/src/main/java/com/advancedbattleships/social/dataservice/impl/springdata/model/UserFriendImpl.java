package com.advancedbattleships.social.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.social.dataservice.model.UserFriend;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_FRIEND")
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendImpl implements UserFriend {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USER_UNIQUE_TOKEN")
	private String userUniqueToken;

	@Column(name = "FRIEND_USER_UNIQUE_TOKEN")
	private String friendUserUniqueToken;

	@Column(name = "ACCEPTED")
	private Boolean accepted;

	@Override
	public String getUserUniqueToken() {
		return userUniqueToken;
	}

	@Override
	public void setUserUniqueToken(String userUniqueToken) {
		this.userUniqueToken = userUniqueToken;
	}

	@Override
	public String getFriendUserUniqueToken() {
		return friendUserUniqueToken;
	}

	@Override
	public void setFriendUserUniqueToken(String friendUserUniqueToken) {
		this.friendUserUniqueToken = friendUserUniqueToken;
	}

	@Override
	public Boolean getAccepted() {
		return accepted;
	}

	@Override
	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public UserFriendImpl(UserFriend source) {
		this.setFriendUserUniqueToken(source.getFriendUserUniqueToken());
		this.setUserUniqueToken(source.getFriendUserUniqueToken());
		this.setAccepted(source.getAccepted());

		if (source instanceof UserFriendImpl) {
			this.id = ((UserFriendImpl) source).id;
		}
	}
}
