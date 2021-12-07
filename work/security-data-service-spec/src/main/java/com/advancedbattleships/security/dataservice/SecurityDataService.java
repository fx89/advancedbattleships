package com.advancedbattleships.security.dataservice;

import java.util.Set;

import com.advancedbattleships.security.dataservice.model.Group;
import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;

public interface SecurityDataService {
	/**
	 * Returns the login source object which is uniquely identified by the given parameters. If the object
	 * does not exist, returns null.
	 */
	UserLoginSource getUserLoginSourceByLoginSourceAndLoginToken(LoginSource loginSource, String loginToken);

	/**
	 * Creates a new user with the given name, pictureUrl and primaryEmail. Then creates a new login source
	 * mapping for the newly created user, using the given loginSource and loginToken, setting the give nick name
	 * and the first login flag of the user. Then returns the mapping.
	 */
	UserLoginSource createUserLoginSource(String uniqueToken, String name, String pictureUrl, String primaryEmail, LoginSource loginSource, String loginToken, String nickName, Boolean isFirstLogin);

	/**
	 * Creates a mapping record between the referenced user and the referenced group,
	 * reloads the user and returns a reference.
	 */
	User mapUserToGroup(User user, Group group);

	Group findGroupByName(String groupName);

	User saveUser(User user);

	User findUserByUniqueToken(String uniqueToken);

	User findUserByNickName(String nickName);

	Set<? extends User> findUsersByUniqueToken(Set<String> uniqueTokens);

	/**
	 * Sets the Online flag to the given value for users who's unique tokens
	 * are found in the given list
	 */
	void setOnlineFlagForUsers(Set<String> userUniqueTokens, Boolean loggedIn);
}
