package com.advancedbattleships.content.dataservice.model;

import java.io.Serializable;

public interface UserUiConfig extends Serializable {

	String getUserUniqueToken();

	void setUserUniqueToken(String userUniqueToken);

	String getCurrentWallpaperName();

	void setCurrentWallpaperName(String currentWallpaperName);

	String getCurrentStylesheetName();

	void setCurrentStylesheetName(String currentStylesheetName);

	String getCurrentIconThemeName();

	void setCurrentIconThemeName(String currentIconThemeName);

	String getCurrentLogoName();

	void setCurrentLogoName(String currentLogoName);
}
