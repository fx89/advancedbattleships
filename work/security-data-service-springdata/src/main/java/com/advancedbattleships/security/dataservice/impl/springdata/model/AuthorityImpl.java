package com.advancedbattleships.security.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.security.dataservice.model.Authority;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="AUTHORITY")
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityImpl implements Authority {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="NAME")
	private String name;

	@Column(name="DESCRIPTION")
	private String description;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuthorityImpl(Authority src) {
		this.name = src.getName();
		this.description = src.getDescription();

		if (src instanceof AuthorityImpl) {
			this.id = ((AuthorityImpl) src).getId();
		}
	}
}
