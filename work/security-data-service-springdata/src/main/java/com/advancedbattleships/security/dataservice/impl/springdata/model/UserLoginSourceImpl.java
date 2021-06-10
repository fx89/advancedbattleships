package com.advancedbattleships.security.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="USER_LOGIN_SOURCE")
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginSourceImpl implements UserLoginSource {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="SOURCE_ID")
	private LoginSource loginSource;

	@Column(name="LOGIN_TOKEN")
	private String loginToken;

	@ManyToOne()
	@JoinColumn(name="USER_ID", nullable=false)
	private UserImpl user;

	@Override
	public LoginSource getLoginSource() {
		return loginSource;
	}

	@Override
	public void setLoginSource(LoginSource loginSource) {
		this.loginSource = loginSource;
	}

	@Override
	public String getLoginToken() {
		return loginToken;
	}

	@Override
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = new UserImpl(user);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserLoginSourceImpl(UserLoginSource src) {
		this.loginSource = src.getLoginSource();
		this.loginToken = src.getLoginToken();
		this.user = new UserImpl(src.getUser());

		if (src instanceof UserLoginSourceImpl) {
			this.id = ((UserLoginSourceImpl)src).getId();
		}
	}
}
