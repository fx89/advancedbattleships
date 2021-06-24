package com.advancedbattleships.content.provisioningservice;

import java.io.InputStream;

import com.advancedbattleships.content.provisioningservice.exception.AdvancedBattleshipsContentProvisioningServiceException;

public interface ContentProvisioningService {
	/**
	 * Returns an InputStream to the referenced file found in the underlying data
	 * structures. The reference is given using the following hierarchy: schemaToken
	 * >> contentType >> fileName.
	 * 
	 * @params
	 * @param schemaToken has the role of differentiating between multiple visual
	 *                    styles / themes (i.e. blue icon theme or dark UI theme)
	 * @param contentType differentiates between different types of content (i.e.
	 *                    wallpaper, icon, stylesheet)
	 * @param fileName    ultimately identifies the file to be served (i.e.
	 *                    subsys_AC1~~~~~~~~~.png or ocean.mp4)
	 */
	InputStream getFile(String schemaToken, String contentType, String fileName)
			throws AdvancedBattleshipsContentProvisioningServiceException;
}
