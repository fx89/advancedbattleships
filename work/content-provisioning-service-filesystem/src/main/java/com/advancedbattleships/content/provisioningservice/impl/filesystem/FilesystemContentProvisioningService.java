package com.advancedbattleships.content.provisioningservice.impl.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.content.provisioningservice.ContentProvisioningService;
import com.advancedbattleships.content.provisioningservice.exception.AdvancedBattleshipsContentProvisioningServiceException;

@Service
public class FilesystemContentProvisioningService implements ContentProvisioningService {

	@Autowired
	private FilesystemContentProvisioningConfig config;

	@Override
	public InputStream getFile(String contentType, String schemaToken, String fileName)
			throws AdvancedBattleshipsContentProvisioningServiceException
	{
		final String filePathName
			= config.getStorageDir() + "/" + contentType + "/" + schemaToken + "/" + fileName;

		File file = new File(filePathName);

	    try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new AdvancedBattleshipsContentProvisioningServiceException(
					"Requested content not found", e
				);
		}
	}

}
