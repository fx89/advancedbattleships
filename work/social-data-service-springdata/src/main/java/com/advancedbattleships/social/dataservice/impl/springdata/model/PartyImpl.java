package com.advancedbattleships.social.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.social.dataservice.model.Party;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "PARTY")
@AllArgsConstructor
@NoArgsConstructor
public class PartyImpl implements Party {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "PARTY_UNIQUE_TOKEN")
	private String partyUniqueToken;

	@Column(name = "NAME")
	private String name;

	@Override
	public String getPartyUniqueToken() {
		return partyUniqueToken;
	}

	@Override
	public void setPartyUniqueToken(String partyUniqueToken) {
		this.partyUniqueToken = partyUniqueToken;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public PartyImpl(Party source) {
		this.setName(source.getName());
		this.setPartyUniqueToken(source.getPartyUniqueToken());

		if (source instanceof PartyImpl) {
			this.id = ((PartyImpl) source).id;
		}
	}
}
