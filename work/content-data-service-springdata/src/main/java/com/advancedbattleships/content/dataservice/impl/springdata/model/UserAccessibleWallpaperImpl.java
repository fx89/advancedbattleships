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

import com.advancedbattleships.content.dataservice.model.UserAccessibleWallpaper;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_ACCESSIBLE_WALLPAPER")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessibleWallpaperImpl implements UserAccessibleWallpaper {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_UI_CONFIG_ID")
	private UserUiConfigImpl user;

	@Column(name = "WALLPAPER_NAME")
	private String wallpaperName;

	@Override
	public UserUiConfig getUser() {
		return user;
	}

	@Override
	public void setUser(UserUiConfig user) {
		this.user = new UserUiConfigImpl(user);
	}

	@Override
	public String getWallpaperName() {
		return wallpaperName;
	}

	@Override
	public void setWallpaperName(String wallpaperName) {
		this.wallpaperName = wallpaperName;
	}

	public UserAccessibleWallpaperImpl(UserAccessibleWallpaper source) {
		this.setUser(source.getUser());
		this.setWallpaperName(source.getWallpaperName());

		if (source instanceof UserAccessibleWallpaperImpl) {
			this.id = ((UserAccessibleWallpaperImpl) source).id;
		}
	}
}
