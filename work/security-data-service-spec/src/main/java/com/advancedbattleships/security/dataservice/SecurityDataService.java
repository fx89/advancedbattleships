package com.advancedbattleships.security.dataservice;

import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;

public interface SecurityDataService {
	/**
	 * Returns the login source object which is uniquely identified by the given parameters. If the object
	 * does not exist, returns null.
	 */
	UserLoginSource getUserLoginSourceByLoginSourceAndLoginToken(LoginSource loginSource, String loginToken);

	/**
	 * Creates a new user with the given name, pictureUrl and primaryEmail. Then creates a new login source
	 * mapping for the newly created user, using the given loginSource and loginToken. Then returns the mapping.
	 */
	UserLoginSource createUserLoginSource(String name, String pictureUrl, String primaryEmail, LoginSource loginSource, String loginToken);
}
