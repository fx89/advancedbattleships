package com.advancedbattleships.content.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advancedbattleships.content.dataservice.model.UserUiConfig;
import com.advancedbattleships.content.service.ContentService;
import com.advancedbattleships.security.service.SecurityService;



@RestController
@RequestMapping("/content")
public class ContentRestController {
	@Autowired
	private ContentService content;

	@Autowired
	private SecurityService security;

	@RequestMapping(value="userWallpaper", method=RequestMethod.GET)
	public void userWallpaper(@RequestParam() String mimeType, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getUserWallpaper(usrToken, mimeType),
			"userWallpaper." + mimeType, "application/octet-stream"
		);
	}

	@RequestMapping(value="wallpaperImage", method=RequestMethod.GET)
	public void wallpaperImage(@RequestParam() String wallpaperName, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getWallpaperImage(usrToken, wallpaperName),
			wallpaperName + ".png", // TODO:  Make the extension a static variable or something, so it doesn't have to appear twice in two classes
			"application/octet-stream"
		);
	}

	@RequestMapping(value="userIcon", method=RequestMethod.GET)
	public void userIcon(@RequestParam() String fileName, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getUserIcon(usrToken, fileName),
			fileName, "application/octet-stream"
		);
	}

	@RequestMapping(value="userLogo", method=RequestMethod.GET)
	public void userLogo(HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getUserLogo(usrToken),
			"userLogo.png", // TODO: Make the extension a static variable or something, so it doesn't have to appear twice in two classes
			"application/octet-stream"
		);
	}

	@RequestMapping(value="logo", method=RequestMethod.GET)
	public void logo(@RequestParam() String logoName, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getLogo(usrToken, logoName),
			logoName + ".png", // TODO:  Make the extension a static variable or something, so it doesn't have to appear twice in two classes
			"application/octet-stream"
		);
	}

	@RequestMapping(value="userStylesheet", method=RequestMethod.GET)
	public void userStylesheet(@RequestParam() String fileName, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getUserStylesheet(usrToken, fileName),
			fileName, "text/css"
		);
	}

	@RequestMapping(value="stylesheetThumb", method=RequestMethod.GET)
	public void stylesheetThumb(@RequestParam() String stylesheetName, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getStylesheetThumb(usrToken, stylesheetName),
			stylesheetName + "-thumb.png", "application/octet-stream" // TODO: file extension handling
		);
	}

	private void buildFileResponse(
			HttpServletResponse response,
			Function<String,InputStream> inputStreamFunction,
			String fileName,
			String mimeType
	) throws IOException {
		// Get the current user's unique token from the security service
		String userUniqueToken = security.getCurrentUser().getUniqueToken();

		// Get the file stream from the content service
		InputStream myStream = inputStreamFunction.apply(userUniqueToken);

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setContentType(mimeType);

		// Copy the stream to the response's output stream.
		IOUtils.copy(myStream, response.getOutputStream());
		response.flushBuffer();
	}

	@RequestMapping(value="getAvailableLogos", method=RequestMethod.GET)
	public Set<String> getAvailableLogos() {
		return content.getUserAvailableLogos(security.getCurrentUser().getUniqueToken());
	}

	@RequestMapping(value="getAvailableWallpapers", method=RequestMethod.GET)
	public Set<String> getAvailableWallpapers() {
		return content.getUserAvailableWallpapers(security.getCurrentUser().getUniqueToken());
	}

	@RequestMapping(value="getAvailableStylesheets", method=RequestMethod.GET)
	public Set<String> getAvailableStylesheets() {
		return content.getUserAvailableStylesheets(security.getCurrentUser().getUniqueToken());
	}

	@RequestMapping(value="getAvailableIconThemes", method=RequestMethod.GET)
	public Set<String> getAvailableIconThemes() {
		return content.getUserAvailableIconThemes(security.getCurrentUser().getUniqueToken());
	}

	@RequestMapping(value="updateUserLogo", method=RequestMethod.POST)
	public void updateUserLogo(@RequestParam() String logoName) {
		content.updateUserLogo(security.getCurrentUser().getUniqueToken(), logoName);
	}

	@RequestMapping(value="updateUserWallpaper", method=RequestMethod.POST)
	public void updateUserWallpaper(@RequestParam() String wallpaperName) {
		content.updateUserWallpaper(security.getCurrentUser().getUniqueToken(), wallpaperName);
	}

	@RequestMapping(value="updateUserStylesheet", method=RequestMethod.POST)
	public void updateUserStylesheet(@RequestParam() String stylesheetName) {
		content.updateUserStylesheet(security.getCurrentUser().getUniqueToken(), stylesheetName);
	}

	@RequestMapping(value="updateUserIconTheme", method=RequestMethod.POST)
	public void updateUserIconTheme(@RequestParam() String iconThemeName) {
		content.updateUserIconTheme(security.getCurrentUser().getUniqueToken(), iconThemeName);
	}

	@RequestMapping(value="getUserConfig", method=RequestMethod.GET)
	public UserUiConfig getUserConfig() {
		return content.getUserConfig(security.getCurrentUser().getUniqueToken());
	}
}
