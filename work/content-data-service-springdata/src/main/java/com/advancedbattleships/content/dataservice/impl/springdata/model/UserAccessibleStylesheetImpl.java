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

import com.advancedbattleships.content.dataservice.model.UserAccessibleStylesheet;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_ACCESSIBLE_STYLESHEET")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessibleStylesheetImpl implements UserAccessibleStylesheet {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_UI_CONFIG_ID")
	private UserUiConfigImpl user;

	@Column(name = "STYLESHEET_NAME")
	private String stylesheetName;

	@Override
	public UserUiConfig getUser() {
		return user;
	}

	@Override
	public void setUser(UserUiConfig user) {
		this.user = new UserUiConfigImpl(user);
	}

	@Override
	public String getStylesheetName() {
		return stylesheetName;
	}

	@Override
	public void setStylesheetName(String stylesheetName) {
		this.stylesheetName = stylesheetName;
	}

	public UserAccessibleStylesheetImpl(UserAccessibleStylesheet source) {
		this.setStylesheetName(source.getStylesheetName());
		this.setUser(source.getUser());

		if (source instanceof UserAccessibleStylesheetImpl) {
			this.id = ((UserAccessibleStylesheetImpl) source).id;
		}
	}
}
