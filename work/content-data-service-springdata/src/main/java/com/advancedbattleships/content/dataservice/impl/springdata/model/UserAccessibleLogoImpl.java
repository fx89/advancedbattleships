package com.advancedbattleships.content.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.advancedbattleships.content.dataservice.model.UserAccessibleLogo;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_ACCESSIBLE_LOGO")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessibleLogoImpl implements UserAccessibleLogo {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_UI_CONFIG_ID")
	private UserUiConfigImpl user;

	@Column(name = "LOGO_NAME")
	private String logoName;

	@Override
	public UserUiConfig getUser() {
		return user;
	}

	@Override
	public void setUser(UserUiConfig user) {
		this.user = new UserUiConfigImpl(user);
	}

	@Override
	public String getLogoName() {
		return logoName;
	}

	@Override
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public UserAccessibleLogoImpl(UserAccessibleLogo source) {
		this.setLogoName(source.getLogoName());
		this.setUser(source.getUser());

		if (source instanceof UserAccessibleLogoImpl) {
			this.id = ((UserAccessibleLogoImpl) source).id;
		}
	}
}
