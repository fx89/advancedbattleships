package com.advancedbattleships.security.dataservice.impl.springdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.advancedbattleships.security.dataservice.SecurityDataService;
import com.advancedbattleships.security.dataservice.impl.springdata.model.AuthorityImpl;
import com.advancedbattleships.security.dataservice.impl.springdata.model.GroupImpl;
import com.advancedbattleships.security.dataservice.impl.springdata.model.UserImpl;
import com.advancedbattleships.security.dataservice.impl.springdata.model.UserLoginSourceImpl;
import com.advancedbattleships.security.dataservice.model.Authority;
import com.advancedbattleships.security.dataservice.model.Group;
import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;

@Service
public class SpringDataSecurityDataService implements SecurityDataService {

	@Override
	public UserLoginSource getUserLoginSourceByLoginSourceAndLoginToken(LoginSource loginSource, String loginToken) {
		// TODO: use the database
		return null;
	}

	// TODO: make @Transactional
	@Override
	public UserLoginSource createUserLoginSource(String name, String pictureUrl, String primaryEmail,
			LoginSource loginSource, String loginToken)
	{
		// Create the user
		User user = new UserImpl();
		user.setName(name);
		user.setPictureUrl(pictureUrl);
		user.setPrimaryEmailAddress(primaryEmail);

		// TODO: remove the code adding the group and the authority - this is just for testing
		List<Authority> groupAuthorities = new ArrayList<>();
		groupAuthorities.add(new AuthorityImpl("TEST_AUTHORITY", "TEST_AUTHORITY DESCRIPTION"));
		List<Group> userGroups = new ArrayList<>();
		userGroups.add(new GroupImpl("TEST_GROUP", "TEST_GROUP_DESCRIPTION", groupAuthorities));
		user.setGroups(userGroups);

		// Save the user into the database
		// TODO: use the database

		// Create the user login source
		UserLoginSource userLoginSource = new UserLoginSourceImpl();
		userLoginSource.setLoginSource(loginSource);
		userLoginSource.setLoginToken(loginToken);
		userLoginSource.setUser(user);

		// Save the user login source
		// TODO: use the database
		
		return userLoginSource;
	}

}
