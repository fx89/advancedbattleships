package com.advancedbattleships.content.dataservice;

import java.util.Set;

import com.advancedbattleships.content.dataservice.model.UserAccessibleIconTheme;
import com.advancedbattleships.content.dataservice.model.UserAccessibleLogo;
import com.advancedbattleships.content.dataservice.model.UserAccessibleStylesheet;
import com.advancedbattleships.content.dataservice.model.UserAccessibleWallpaper;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;

public interface ContentDataService {

	UserUiConfig findUserByUniqueToken(String userUniqueToken);

	UserUiConfig saveUserUiConfig(UserUiConfig userConfig);

	UserUiConfig createNewUser(String userUniqueToken, String currentWallpaperName, String currentStylesheetName, String currentIconThemeName, String currentLogoName);

	Set<UserAccessibleIconTheme> findAllUserAccessibleIconThemes(String userUniqueToken);

	UserAccessibleIconTheme addUserAccessibleIconTheme(UserUiConfig userUiConfig, String iconThemeName);

	Set<UserAccessibleLogo> findAllUserAccessibleLogos(String userUniqueToken);

	UserAccessibleLogo addUserAccessibleLogo(UserUiConfig userUiConfig, String logoName);

	Set<UserAccessibleStylesheet> findAllUserAccessibleStylesheets(String userUniqueToken);

	UserAccessibleStylesheet addUserAccessibleStylesheet(UserUiConfig userUiConfig, String stylesheetName);

	Set<UserAccessibleWallpaper> findAllUserAccessibleWallpapaers(String userUniqueToken);

	UserAccessibleWallpaper addUserAccessibleWallpaper(UserUiConfig userUiConfig, String wallpaperName);

	void executeTransaction(Runnable transaction);
}
