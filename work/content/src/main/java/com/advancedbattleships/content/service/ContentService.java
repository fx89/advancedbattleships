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

	public InputStream getUserLogo(String userUniqueToken) {
		UserUiConfig userConfig = resolveUserUiConfig(userUniqueToken);
		String logoName = userConfig.getCurrentLogoName();
		return provisioningService.getFile("logo", "_all", logoName + ".png");
	}

	public InputStream getUserStylesheet(String userUniqueToken, String fileName) {
		UserUiConfig userConfig = resolveUserUiConfig(userUniqueToken);
		String stylesheetName = userConfig.getCurrentStylesheetName();
		return provisioningService.getFile("stylesheet", stylesheetName, fileName);
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

	public void saveUserUiConfig(
		String userUniqueToken,
		String currentWallpaperName,
		String currentStylesheetName,
		String currentIconThemeName,
		String currentLogoName
	) {
		// TODO: maybe cache the database response using a new service with the the @Cacheable annotation
		// TODO: in the REST module, find a way to enforce a timeout between calls to this method on a per user basis, so that these operations don't execute if the user machine guns the save button

		// Check if the wallpaper is allowed
		if (false == getUserAvailableWallpapers(userUniqueToken).stream().anyMatch(x -> x.equals(currentWallpaperName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The wallpaper [" + currentWallpaperName + "] is not accessible"
				);
		}

		// Check if the style sheet is allowed
		if (false == getUserAvailableStylesheets(userUniqueToken).stream().anyMatch(x -> x.equals(currentStylesheetName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The style sheet [" + currentStylesheetName + "] is not accessible"
				);
		}

		// Check if the icon theme is allowed
		if (false == getUserAvailableStylesheets(userUniqueToken).stream().anyMatch(x -> x.equals(currentIconThemeName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The icon theme [" + currentIconThemeName + "] is not accessible"
				);
		}

		// Check if the logo is allowed
		if (false == getUserAvailableLogos(userUniqueToken).stream().anyMatch(x -> x.equals(currentLogoName))) {
			throw new AdvancedBattleshipsContentServiceSecurityException(
					"The logo [" + currentLogoName + "] is not accessible"
				);
		}

		// If all checks passed, then get, update and save the user config
		UserUiConfig userUiConfig = resolveUserUiConfig(userUniqueToken);

		userUiConfig.setCurrentIconThemeName(currentIconThemeName);
		userUiConfig.setCurrentLogoName(currentLogoName);
		userUiConfig.setCurrentStylesheetName(currentStylesheetName);
		userUiConfig.setCurrentWallpaperName(currentWallpaperName);

		saveUserUiConfig(userUiConfig);
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
