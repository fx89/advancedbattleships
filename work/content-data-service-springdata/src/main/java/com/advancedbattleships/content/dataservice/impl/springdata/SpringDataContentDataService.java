package com.advancedbattleships.content.dataservice.impl.springdata;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advancedbattleships.content.dataservice.ContentDataService;
import com.advancedbattleships.content.dataservice.impl.springdata.dao.UserAccessibleIconThemesRepository;
import com.advancedbattleships.content.dataservice.impl.springdata.dao.UserAccessibleLogosRepository;
import com.advancedbattleships.content.dataservice.impl.springdata.dao.UserAccessibleStylesheetsRepository;
import com.advancedbattleships.content.dataservice.impl.springdata.dao.UserAccessibleWallpapersRepository;
import com.advancedbattleships.content.dataservice.impl.springdata.dao.UserUiConfigRepository;
import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleIconThemeImpl;
import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleLogoImpl;
import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleStylesheetImpl;
import com.advancedbattleships.content.dataservice.impl.springdata.model.UserAccessibleWallpaperImpl;
import com.advancedbattleships.content.dataservice.impl.springdata.model.UserUiConfigImpl;
import com.advancedbattleships.content.dataservice.model.UserAccessibleIconTheme;
import com.advancedbattleships.content.dataservice.model.UserAccessibleLogo;
import com.advancedbattleships.content.dataservice.model.UserAccessibleStylesheet;
import com.advancedbattleships.content.dataservice.model.UserAccessibleWallpaper;
import com.advancedbattleships.content.dataservice.model.UserUiConfig;

@Service
public class SpringDataContentDataService implements ContentDataService {

	@Autowired
	private UserUiConfigRepository usersRepository;

	@Autowired
	private UserAccessibleIconThemesRepository userAccessibleIconThemesRepository;

	@Autowired
	private UserAccessibleLogosRepository userAccessibleLogosRepository;

	@Autowired
	private UserAccessibleStylesheetsRepository userAccessibleStylesheetsRepository;

	@Autowired
	private UserAccessibleWallpapersRepository userAccessibleWallpapersRepository;

	@Override
	public UserUiConfig findUserByUniqueToken(String userUniqueToken) {
		return usersRepository.findOneByUserUniqueToken(userUniqueToken);
	}

	@Override
	public UserUiConfig saveUserUiConfig(UserUiConfig userConfig) {
		return usersRepository.save(new UserUiConfigImpl(userConfig));
	}

	@Override
	public UserUiConfig createNewUser(String userUniqueToken, String currentWallpaperName, String currentStylesheetName, String currentIconThemeName, String currentLogoName) {
		UserUiConfigImpl ret = new UserUiConfigImpl();

		ret.setCurrentIconThemeName(currentIconThemeName);
		ret.setCurrentLogoName(currentLogoName);
		ret.setCurrentStylesheetName(currentStylesheetName);
		ret.setCurrentWallpaperName(currentWallpaperName);
		ret.setUserUniqueToken(userUniqueToken);

		return usersRepository.save(ret);
	}

	@Override
	public Set<? extends UserAccessibleIconTheme> findAllUserAccessibleIconThemes(String userUniqueToken) {
		return userAccessibleIconThemesRepository.findAllByUserUserUniqueToken(userUniqueToken);
	}

	@Override
	public UserAccessibleIconTheme addUserAccessibleIconTheme(UserUiConfig userUiConfig, String iconThemeName) {
		UserAccessibleIconThemeImpl newIconTheme = new UserAccessibleIconThemeImpl();
		newIconTheme.setIconThemeName(iconThemeName);
		newIconTheme.setUser(userUiConfig);
		return userAccessibleIconThemesRepository.save(newIconTheme);
	}

	@Override
	public Set<? extends UserAccessibleLogo> findAllUserAccessibleLogos(String userUniqueToken) {
		return userAccessibleLogosRepository.findAllByUserUserUniqueToken(userUniqueToken);
	}

	@Override
	public UserAccessibleLogo addUserAccessibleLogo(UserUiConfig userUiConfig, String logoName) {
		UserAccessibleLogoImpl newLogo = new UserAccessibleLogoImpl();
		newLogo.setLogoName(logoName);
		newLogo.setUser(userUiConfig);
		return userAccessibleLogosRepository.save(newLogo);
	}

	@Override
	public Set<? extends UserAccessibleStylesheet> findAllUserAccessibleStylesheets(String userUniqueToken) {
		return userAccessibleStylesheetsRepository.findAllByUserUserUniqueToken(userUniqueToken);
	}

	@Override
	public UserAccessibleStylesheet addUserAccessibleStylesheet(UserUiConfig userUiConfig, String stylesheetName) {
		UserAccessibleStylesheetImpl newStylesheet = new UserAccessibleStylesheetImpl();
		newStylesheet.setStylesheetName(stylesheetName);
		newStylesheet.setUser(userUiConfig);
		return userAccessibleStylesheetsRepository.save(newStylesheet);
	}

	@Override
	public Set<? extends UserAccessibleWallpaper> findAllUserAccessibleWallpapaers(String userUniqueToken) {
		return userAccessibleWallpapersRepository.findAllByUserUserUniqueToken(userUniqueToken);
	}

	@Override
	public UserAccessibleWallpaper addUserAccessibleWallpaper(UserUiConfig userUiConfig, String wallpaperName) {
		UserAccessibleWallpaperImpl newWallpaper = new UserAccessibleWallpaperImpl();
		newWallpaper.setWallpaperName(wallpaperName);
		newWallpaper.setUser(userUiConfig);
		return userAccessibleWallpapersRepository.save(newWallpaper);
	}

	@Override
	@Transactional(transactionManager = "absContentTransactionManager")
	public void executeTransaction(Runnable transaction) {
		transaction.run();
	}

	@Override
	public Set<? extends UserUiConfig> findAllUsersByUniqueToken(Set<String> userUniqueTokens) {
		return usersRepository.findAllByUserUniqueTokenIn(userUniqueTokens);
	}
}
