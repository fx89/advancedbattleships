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

import com.advancedbattleships.security.dataservice.model.Group;
import com.advancedbattleships.security.dataservice.model.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="USER")
@AllArgsConstructor
@NoArgsConstructor
public class UserImpl implements User {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="UNIQUE_TOKEN")
	private String uniqueToken;

	@Column(name="NAME")
	private String name;

	@Column(name="PICTURE_URL")
	private String pictureUrl;

	@Column(name="PRIMARY_EMAIL_ADDRESS")
	private String primaryEmailAddress;

	@Column(name="NICK_NAME")
	private String nickName;

	@Column(name="IS_FIRST_LOGIN")
	private Boolean isFirstLogin;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	  name = "USER_GROUP", 
	  joinColumns = @JoinColumn(name = "USER_ID"), 
	  inverseJoinColumns = @JoinColumn(name = "GROUP_ID")
	)
	private Set<GroupImpl> groups;

	@Override
	public String getUniqueToken() {
		return uniqueToken;
	}

	@Override
	public void setUniqueToken(String uniqueToken) {
		this.uniqueToken = uniqueToken;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getPicutreUrl() {
		return pictureUrl;
	}

	@Override
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	@Override
	public String getPrimaryEmailAddress() {
		return primaryEmailAddress;
	}

	@Override
	public void setPrimaryEmailAddress(String primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}

	@Override
	public String getNickName() {
		return nickName;
	}

	@Override
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public Boolean isFirstLogin() {
		return isFirstLogin;
	}

	@Override
	public void setFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	@Override
	public Set<Group> getGroups() {
		return multicastSet(groups);
	}

	@Override
	public void setGroups(Set<Group> groups) {
		this.groups = multicastSet(groups, g -> new GroupImpl(g));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserImpl(User src) {
		this.name = src.getName();
		this.pictureUrl = src.getPicutreUrl();
		this.primaryEmailAddress = src.getPrimaryEmailAddress();
		this.uniqueToken = src.getUniqueToken();
		this.nickName = src.getNickName();
		this.isFirstLogin = src.isFirstLogin();
		this.setGroups(src.getGroups());

		if (src instanceof UserImpl) {
			this.id = ((UserImpl) src).getId();
		}
	}
}
