package com.advancedbattleships.content.dataservice;

import java.util.Set;

import com.advancedbattleships.content.dataservice.model.UserAccessibleIconTheme;
import com.advancedbattleships.content.dataservice.model.UserAccessibleLogo;
import com.advancedbattleships.content.dataservice.model.UserAccessibleStylesheet;
import com.advancedbattleships.content.dataservice.model.UserAccessibleWallpaper;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;

public interface ContentDataService {

	UserUiConfig findUserByUniqueToken(String userUniqueToken);

	Set<? extends UserUiConfig> findAllUsersByUniqueToken(Set<String> userUniqueTokens);

	UserUiConfig saveUserUiConfig(UserUiConfig userConfig);

	UserUiConfig createNewUser(String userUniqueToken, String currentWallpaperName, String currentStylesheetName, String currentIconThemeName, String currentLogoName);

	Set<? extends UserAccessibleIconTheme> findAllUserAccessibleIconThemes(String userUniqueToken);

	UserAccessibleIconTheme addUserAccessibleIconTheme(UserUiConfig userUiConfig, String iconThemeName);

	Set<? extends UserAccessibleLogo> findAllUserAccessibleLogos(String userUniqueToken);

	UserAccessibleLogo addUserAccessibleLogo(UserUiConfig userUiConfig, String logoName);

	Set<? extends UserAccessibleStylesheet> findAllUserAccessibleStylesheets(String userUniqueToken);

	UserAccessibleStylesheet addUserAccessibleStylesheet(UserUiConfig userUiConfig, String stylesheetName);

	Set<? extends UserAccessibleWallpaper> findAllUserAccessibleWallpapaers(String userUniqueToken);

	UserAccessibleWallpaper addUserAccessibleWallpaper(UserUiConfig userUiConfig, String wallpaperName);

	void executeTransaction(Runnable transaction);
}
