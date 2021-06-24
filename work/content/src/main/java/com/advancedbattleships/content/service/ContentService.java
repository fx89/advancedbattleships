package com.advancedbattleships.content.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.content.provisioningservice.ContentProvisioningService;

@Service
public class ContentService {

	@Autowired
	private ContentProvisioningService provisioningService;

	// TODO: add the data service

	public InputStream getFile(String userUniqueToken, String schemaToken, String contentType, String fileName) {
		// TODO: check if the user has access to the content, using the data service
		return provisioningService.getFile(schemaToken, contentType, fileName);
	}
}
