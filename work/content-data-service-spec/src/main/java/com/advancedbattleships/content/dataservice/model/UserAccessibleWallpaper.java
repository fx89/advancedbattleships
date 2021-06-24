package com.advancedbattleships.content.dataservice.model;

import java.io.Serializable;

public interface UserAccessibleWallpaper extends Serializable {

	UserUiConfig getUser();

	void setUser(UserUiConfig user);

	String getWallpaperName();

	void setWallpaperName(String wallpaperName);
}
