package com.advancedbattleships.security.dataservice.impl.springdata.model;

import static com.advancedbattleships.common.lang.Multicast.multicastSet;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.advancedbattleships.security.dataservice.model.Authority;
import com.advancedbattleships.security.dataservice.model.Group;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "UGROUP")
@AllArgsConstructor
@NoArgsConstructor
public class GroupImpl implements Group {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "GROUP_AUTHORITY", joinColumns = @JoinColumn(name = "GROUP_ID"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID"))
	private Set<AuthorityImpl> authorities;

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

	@Override
	public Set<Authority> getAuthorities() {
		return multicastSet(authorities);
	}

	@Override
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = multicastSet(authorities, s -> new AuthorityImpl(s));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GroupImpl(Group src) {
		this.name = src.getName();
		this.description = src.getDescription();
		this.setAuthorities(src.getAuthorities());

		if (src instanceof GroupImpl) {
			this.id = ((GroupImpl) src).getId();
		}
	}
}
