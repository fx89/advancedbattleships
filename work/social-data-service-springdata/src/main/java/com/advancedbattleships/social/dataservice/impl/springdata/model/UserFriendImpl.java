package com.advancedbattleships.social.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.advancedbattleships.social.dataservice.model.FriendStatus;
import com.advancedbattleships.social.dataservice.model.UserFriend;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_FRIEND")
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraphs(value = {
	@NamedEntityGraph(
		name = "UserFriendImpl.AllEager",
		attributeNodes = {
			@NamedAttributeNode(value = "status"),
		}
	)
})
public class UserFriendImpl implements UserFriend {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USER_UNIQUE_TOKEN")
	private String userUniqueToken;

	@Column(name = "FRIEND_USER_UNIQUE_TOKEN")
	private String friendUserUniqueToken;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STATUS_ID")
	@Fetch(FetchMode.JOIN)
	private FriendStatusImpl status;

	public Long getId() {
		return id;
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
	public String getFriendUserUniqueToken() {
		return friendUserUniqueToken;
	}

	@Override
	public void setFriendUserUniqueToken(String friendUserUniqueToken) {
		this.friendUserUniqueToken = friendUserUniqueToken;
	}

	@Override
	public FriendStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(FriendStatus status) {
		this.status = new FriendStatusImpl(status);
	}

	public UserFriendImpl(UserFriend source) {
		this.setFriendUserUniqueToken(source.getFriendUserUniqueToken());
		this.setUserUniqueToken(source.getUserUniqueToken());
		this.setStatus(source.getStatus());

		if (source instanceof UserFriendImpl) {
			this.id = ((UserFriendImpl) source).id;
		}
	}
}
