package com.advancedbattleships.content.service;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.content.dataservice.ContentDataService;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;
import com.advancedbattleships.content.provisioningservice.ContentProvisioningService;
import com.advancedbattleships.content.service.exception.AdvancedBattleshipsContentServiceSecurityException;

@Service
public class ContentService {

	@Autowired
	private ContentServiceConfig config;

	@Autowired
	private ContentProvisioningService provisioningService;

	@Autowired
	private ContentDataService dataService;

	private Map<String, UserUiConfig> localCache = new ConcurrentHashMap<>();

	public InputStream getUserWallpaper(String userUniqueToken, String mimeType) {
		UserUiConfig userConfig = resolveUserUiConfig(userUniqueToken);
		String wallpaperName = userConfig.getCurrentWallpaperName();
		return provisioningService.getFile("wallpaper", "_all", wallpaperName + "." + mimeType);
	}

	public InputStream getWallpaperImage(String userUniqueToken, String wallpaperName) {
		verifyAvailableWallpaper(userUniqueToken, wallpaperName);
		return provisioningService.getFile("wallpaper", "_all", wallpaperName + ".png");
	}

	public InputStream getUserLogo(String userUniqueToken) {
		UserUiConfig userConfig = resolveUserUiConfig(userUniqueToken);
		String logoName = userConfig.getCurrentLogoName();
		return provisioningService.getFile("logo", "_all", logoName + ".png");
	}

	public InputStream getLogo(String userUniqueToken, String logoName) {
		verifyAvailableLogo(userUniqueToken, logoName);
		return viewLogo(logoName);
	}

	public InputStream viewLogo(String logoName) {
		return provisioningService.getFile("logo", "_all", logoName + ".png");
	}

	public InputStream getUserStylesheet(String userUniqueToken, String fileName) {
		UserUiConfig userConfig = resolveUserUiConfig(userUniqueToken);
		String stylesheetName = userConfig.getCurrentStylesheetName();
		return provisioningService.getFile("stylesheet", stylesheetName, fileName);
	}

	public InputStream getStylesheetThumb(String userUniqueToken, String stylesheetName) {
		verifyAvailableStylesheet(userUniqueToken, stylesheetName);
		return provisioningService.getFile("stylesheet", stylesheetName, "thumb.png");
	}

	public InputStream getUserIcon(String userUniqueToken, String fileName) {
		UserUiConfig userConfig = resolveUserUiConfig(userUniqueToken);
		String iconThemeName = userConfig.getCurrentIconThemeName();
		return provisioningService.getFile("icon", iconThemeName, fileName);
	}

	public Set<String> getUserAvailableWallpapers(String userUniqueToken) {
		return
			dataService.findAllUserAccessibleWallpapaers(userUniqueToken)
				.stream()
					.map(x -> x.getWallpaperName())
					.collect(Collectors.toSet())
			;
	}

	public Set<String> getUserAvailableLogos(String userUniqueToken) {
		return
			dataService.findAllUserAccessibleLogos(userUniqueToken)
				.stream()
					.map(x -> x.getLogoName())
					.collect(Collectors.toSet())
			;
	}

	public Set<String> getUserAvailableStylesheets(String userUniqueToken) {
		return
			dataService.findAllUserAccessibleStylesheets(userUniqueToken)
				.stream()
					.map(x -> x.getStylesheetName())
					.collect(Collectors.toSet())
			;
	}

	public Set<String> getUserAvailableIconThemes(String userUniqueToken) {
		return
			dataService.findAllUserAccessibleIconThemes(userUniqueToken)
				.stream()
					.map(x -> x.getIconThemeName())
					.collect(Collectors.toSet())
			;
	}

	public void updateUserLogo(String userUniqueToken, String currentLogoName) {
		// Check if the logo is allowed
		verifyAvailableLogo(userUniqueToken, currentLogoName);

		// If all checks passed, then get, update and save the user config
		UserUiConfig userUiConfig = resolveUserUiConfig(userUniqueToken);
		userUiConfig.setCurrentLogoName(currentLogoName);
		saveUserUiConfig(userUiConfig);
	}

	public void updateUserWallpaper(String userUniqueToken, String currentWallpaperName) {
		// Check if the wallpaper is allowed
		verifyAvailableWallpaper(userUniqueToken, currentWallpaperName);

		// If all checks passed, then get, update and save the user config
		UserUiConfig userUiConfig = resolveUserUiConfig(userUniqueToken);
		userUiConfig.setCurrentWallpaperName(currentWallpaperName);
		saveUserUiConfig(userUiConfig);
	}

	public void updateUserStylesheet(String userUniqueToken, String currentStylesheetName) {
		// Check if the style sheet is allowed
		verifyAvailableStylesheet(userUniqueToken, currentStylesheetName);

		// If all checks passed, then get, update and save the user config
		UserUiConfig userUiConfig = resolveUserUiConfig(userUniqueToken);
		userUiConfig.setCurrentStylesheetName(currentStylesheetName);
		saveUserUiConfig(userUiConfig);
	}

	public void updateUserIconTheme(String userUniqueToken, String currentIconThemeName) {
		// Check if the icon theme is allowed
		verifyAvailableIconTheme(userUniqueToken, currentIconThemeName);

		// If all checks passed, then get, update and save the user config
		UserUiConfig userUiConfig = resolveUserUiConfig(userUniqueToken);
		userUiConfig.setCurrentIconThemeName(currentIconThemeName);
		saveUserUiConfig(userUiConfig);
	}

	public UserUiConfig getUserConfig(String userUniqueToken) {
		return resolveUserUiConfig(userUniqueToken);
	}

	public void saveUserUiConfig(
		String userUniqueToken,
		String currentWallpaperName,
		String currentStylesheetName,
		String currentIconThemeName,
		String currentLogoName
	) {
		// TODO: maybe cache the database response using a new service with the the @Cacheable annotation
		// TODO: in the REST module, find a way to enforce a timeout between calls to this method on a per user basis, so that these operations don't execute if the user machine guns the save button

		// Verify the availability of the referenced resources to the current user
		verifyAvailableWallpaper(userUniqueToken, currentWallpaperName);
		verifyAvailableStylesheet(userUniqueToken, currentStylesheetName);
		verifyAvailableIconTheme(userUniqueToken, currentLogoName);
		verifyAvailableLogo(userUniqueToken, currentLogoName);

		// If all checks passed, then get, update and save the user config
		UserUiConfig userUiConfig = resolveUserUiConfig(userUniqueToken);

		userUiConfig.setCurrentIconThemeName(currentIconThemeName);
		userUiConfig.setCurrentLogoName(currentLogoName);
		userUiConfig.setCurrentStylesheetName(currentStylesheetName);
		userUiConfig.setCurrentWallpaperName(currentWallpaperName);

		saveUserUiConfig(userUiConfig);
	}

	@SuppressWarnings("unchecked")
	public Set<UserUiConfig> getUsersConfig(Set<String> userUniqueTokens) {
		return (Set<UserUiConfig>) dataService.findAllUsersByUniqueToken(userUniqueTokens);
	}

	private void verifyAvailableLogo(String userUniqueToken, String logoName) {
		if (false == getUserAvailableLogos(userUniqueToken).stream().anyMatch(x -> x.equals(logoName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The logo [" + logoName + "] is not accessible"
				);
		}
		// TODO: really think about caching this
	}
	
	private void verifyAvailableIconTheme(String userUniqueToken, String iconThemeName) {
		if (false == getUserAvailableStylesheets(userUniqueToken).stream().anyMatch(x -> x.equals(iconThemeName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The icon theme [" + iconThemeName + "] is not accessible"
				);
		}
		// TODO: really think about caching this
	}

	private void verifyAvailableStylesheet(String userUniqueToken, String stylesheetName) {
		if (false == getUserAvailableStylesheets(userUniqueToken).stream().anyMatch(x -> x.equals(stylesheetName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The style sheet [" + stylesheetName + "] is not accessible"
				);
		}
		// TODO: really think about caching this
	}

	private void verifyAvailableWallpaper(String userUniqueToken, String wallpaperName) {
		if (false == getUserAvailableWallpapers(userUniqueToken).stream().anyMatch(x -> x.equals(wallpaperName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The wallpaper [" + wallpaperName + "] is not accessible"
				);
		}
		// TODO: really think about caching this
	}

	private synchronized UserUiConfig resolveUserUiConfig(String userUniqueToken) {
		// Attempt to get the user config from the local cache
		final UserUiConfig[] ret = new UserUiConfig[1];
		ret[0] = localCache.get(userUniqueToken);

		// If the user config is not found in the local cache, then:
		if (ret[0] == null) {
			// Attempt to get the user config from the data source
			ret[0] = dataService.findUserByUniqueToken(userUniqueToken);

			// If the user config does not exist in the data source, then add it using
			// default values and default access rights
			if (ret[0] == null) {
				dataService.executeTransaction(() -> {
					ret[0] = dataService.createNewUser(
							userUniqueToken,
							config.getDefaultWallpaperName(),
							config.getDefaultStylesheetName(),
							config.getDefaultIconThemeName(),
							config.getDefaultLogoName()
						);

					dataService.addUserAccessibleIconTheme(ret[0], config.getDefaultIconThemeName());

					dataService.addUserAccessibleLogo(ret[0], config.getDefaultLogoName());
					dataService.addUserAccessibleLogo(ret[0], "sad");

					dataService.addUserAccessibleStylesheet(ret[0], config.getDefaultStylesheetName());
					dataService.addUserAccessibleWallpaper(ret[0], config.getDefaultWallpaperName());
				});
			}

			// In any case, update the local cache
			localCache.put(userUniqueToken, ret[0]);
		}

		// Finally, return a reference to the resolved user config
		return ret[0];
	}

	private synchronized UserUiConfig saveUserUiConfig(UserUiConfig user) {
		// Save the user config
		UserUiConfig ret = dataService.saveUserUiConfig(user);

		// Update the local cache
		localCache.put(ret.getUserUniqueToken(), ret);

		// Return a reference to the saved user config
		return ret;
	}
}
