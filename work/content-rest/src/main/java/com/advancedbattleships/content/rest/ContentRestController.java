package com.advancedbattleships.content.rest;

import java.io.IOException;
import java.io.InputStream;

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

	@RequestMapping(value="download", method=RequestMethod.GET)
	public void getDownload(
		@RequestParam() String schemaToken,
		@RequestParam() String contentType,
		@RequestParam() String fileName,
		HttpServletResponse response
	) throws IOException {

		// Get the current user's unique token from the security service
		String userUniqueToken = security.getCurrentUser().getUniqueToken();

		// Get the file stream from the content service
		InputStream myStream = content.getFile(userUniqueToken, schemaToken, contentType, fileName);

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setContentType("application/octet-stream");

		// Copy the stream to the response's output stream.
		IOUtils.copy(myStream, response.getOutputStream());
		response.flushBuffer();
	}
}
