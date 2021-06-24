package com.advancedbattleships.content.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
			"userWallpaper." + mimeType
		);
	}

	@RequestMapping(value="userIcon", method=RequestMethod.GET)
	public void userIcon(@RequestParam() String fileName, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getUserIcon(usrToken, fileName),
			fileName
		);
	}

	@RequestMapping(value="userLogo", method=RequestMethod.GET)
	public void userLogo(HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getUserLogo(usrToken),
			"userLogo.png" // TODO: Make the extension a static variable or something, so it doesn't have to appear twice in two classes
		);
	}

	@RequestMapping(value="userStylesheet", method=RequestMethod.GET)
	public void userStylesheet(@RequestParam() String fileName, HttpServletResponse response) throws IOException {
		buildFileResponse(response,
			(usrToken) -> content.getUserStylesheet(usrToken, fileName),
			fileName
		);
	}

	private void buildFileResponse(
			HttpServletResponse response,
			Function<String,InputStream> inputStreamFunction,
			String fileName
	) throws IOException {
		// Get the current user's unique token from the security service
		String userUniqueToken = security.getCurrentUser().getUniqueToken();

		// Get the file stream from the content service
		InputStream myStream = inputStreamFunction.apply(userUniqueToken);

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setContentType("application/octet-stream");

		// Copy the stream to the response's output stream.
		IOUtils.copy(myStream, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO: add methods for getting available assets and for setting the user configuration
}
