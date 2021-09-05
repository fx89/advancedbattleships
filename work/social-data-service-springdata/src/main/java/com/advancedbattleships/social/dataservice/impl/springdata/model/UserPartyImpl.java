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

import com.advancedbattleships.social.dataservice.model.FriendStatus;
import com.advancedbattleships.social.dataservice.model.Party;
import com.advancedbattleships.social.dataservice.model.UserParty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_PARTY")
@AllArgsConstructor
@NoArgsConstructor
public class UserPartyImpl implements UserParty {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USER_UNIQUE_TOKEN")
	private String userUniqueToken;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TYPE_ID")
	@Fetch(FetchMode.JOIN)
	private PartyImpl party;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STATUS_ID")
	@Fetch(FetchMode.JOIN)
	private FriendStatusImpl status;

	@Override
	public String getUserUniqueToken() {
		return userUniqueToken;
	}

	@Override
	public void setUserUniqueToken(String userUniqueToken) {
		this.userUniqueToken = userUniqueToken;
	}

	@Override
	public Party getParty() {
		return party;
	}

	@Override
	public void setParty(Party party) {
		this.party = new PartyImpl(party);
	}

	@Override
	public FriendStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(FriendStatus status) {
		this.status = new FriendStatusImpl(status);
	}

	public UserPartyImpl(UserParty source) {
		this.setParty(source.getParty());
		this.setUserUniqueToken(source.getUserUniqueToken());

		if (source instanceof UserPartyImpl) {
			this.id = ((UserPartyImpl) source).id;
		}
	}
}
