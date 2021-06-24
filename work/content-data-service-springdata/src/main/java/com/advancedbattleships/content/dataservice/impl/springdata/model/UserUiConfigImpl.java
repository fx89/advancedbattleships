package com.advancedbattleships.content.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.content.dataservice.model.UserUiConfig;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER_UI_CONFIG")
@AllArgsConstructor
@NoArgsConstructor
public class UserUiConfigImpl implements UserUiConfig {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USER_UNIQUE_TOKEN")
	private String userUniqueToken;

	@Column(name = "CURRENT_WALLPAPER_NAME")
	private String currentWallpaperName;

	@Column(name = "CURRENT_STYLESHEET_NAME")
	private String currentStylesheetName;

	@Column(name = "CURRENT_ICON_THEME_NAME")
	private String currentIconThemeName;

	@Column(name = "CURRENT_LOGO_NAME")
	private String currentLogoName;

	@Override
	public String getUserUniqueToken() {
		return userUniqueToken;
	}

	@Override
	public void setUserUniqueToken(String userUniqueToken) {
		this.userUniqueToken = userUniqueToken;
	}

	@Override
	public String getCurrentWallpaperName() {
		return currentWallpaperName;
	}

	@Override
	public void setCurrentWallpaperName(String currentWallpaperName) {
		this.currentWallpaperName = currentWallpaperName;
	}

	@Override
	public String getCurrentStylesheetName() {
		return currentStylesheetName;
	}

	@Override
	public void setCurrentStylesheetName(String currentStylesheetName) {
		this.currentStylesheetName = currentStylesheetName;
	}

	@Override
	public String getCurrentIconThemeName() {
		return currentIconThemeName;
	}

	@Override
	public void setCurrentIconThemeName(String currentIconThemeName) {
		this.currentIconThemeName = currentIconThemeName;
	}

	@Override
	public String getCurrentLogoName() {
		return currentLogoName;
	}

	@Override
	public void setCurrentLogoName(String currentLogoName) {
		this.currentLogoName = currentLogoName;
	}

	public UserUiConfigImpl(UserUiConfig source) {
		this.setCurrentIconThemeName(source.getCurrentIconThemeName());
		this.setCurrentLogoName(source.getCurrentLogoName());
		this.setCurrentStylesheetName(source.getCurrentStylesheetName());
		this.setCurrentWallpaperName(source.getCurrentWallpaperName());
		this.setUserUniqueToken(source.getUserUniqueToken());

		if (source instanceof UserUiConfigImpl) {
			this.id = ((UserUiConfigImpl) source).id;
		}
	}
}
