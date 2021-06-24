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

import com.advancedbattleships.content.dataservice.model.UserAccessibleIconTheme;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_ACCESSIBLE_ICON_THEME")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessibleIconThemeImpl implements UserAccessibleIconTheme {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_UI_CONFIG_ID")
	private UserUiConfigImpl user;

	@Column(name = "ICON_THEME_NAME")
	private String iconThemeName;

	@Override
	public UserUiConfig getUser() {
		return user;
	}

	@Override
	public void setUser(UserUiConfig user) {
		this.user = new UserUiConfigImpl(user);
	}

	@Override
	public String getIconThemeName() {
		return iconThemeName;
	}

	@Override
	public void setIconThemeName(String iconThemeName) {
		this.iconThemeName = iconThemeName;
	}

	public UserAccessibleIconThemeImpl(UserAccessibleIconTheme source) {
		this.setIconThemeName(source.getIconThemeName());
		this.setUser(source.getUser());

		if (source instanceof UserAccessibleIconThemeImpl) {
			this.id = ((UserAccessibleIconThemeImpl) source).id;
		}
	}
}
