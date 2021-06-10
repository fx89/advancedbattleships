package com.advancedbattleships.security.dataservice.impl.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advancedbattleships.security.dataservice.SecurityDataService;
import com.advancedbattleships.security.dataservice.impl.springdata.dao.GroupsRepository;
import com.advancedbattleships.security.dataservice.impl.springdata.dao.UserGroupsRepository;
import com.advancedbattleships.security.dataservice.impl.springdata.dao.UserLoginSourcesRepository;
import com.advancedbattleships.security.dataservice.impl.springdata.dao.UsersRepository;
import com.advancedbattleships.security.dataservice.impl.springdata.model.GroupImpl;
import com.advancedbattleships.security.dataservice.impl.springdata.model.UserGroup;
import com.advancedbattleships.security.dataservice.impl.springdata.model.UserImpl;
import com.advancedbattleships.security.dataservice.impl.springdata.model.UserLoginSourceImpl;
import com.advancedbattleships.security.dataservice.model.Group;
import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;
import com.advancedbattleships.utilityservices.UniqueTokenProviderService;

@Service
public class SpringDataSecurityDataService implements SecurityDataService {

	@Autowired
	private UniqueTokenProviderService uniqueTokenProvider;

	@Autowired
	private UserLoginSourcesRepository userLoginSourcesRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private GroupsRepository groupsRepository;

	@Autowired
	private UserGroupsRepository userGroupsRepository;
	
	@Override
	public UserLoginSource getUserLoginSourceByLoginSourceAndLoginToken(LoginSource loginSource, String loginToken) {
		return userLoginSourcesRepository.findFirstByLoginSourceAndLoginToken(loginSource, loginToken);
	}

	@Override
	@Transactional
	public UserLoginSource createUserLoginSource(String name, String pictureUrl, String primaryEmail,
			LoginSource loginSource, String loginToken)
	{
		// Create the user
		UserImpl user = new UserImpl();
		user.setName(name);
		user.setPictureUrl(pictureUrl);
		user.setPrimaryEmailAddress(primaryEmail);
		user.setUniqueToken(uniqueTokenProvider.provide());

		// Save the user into the database
		user = usersRepository.save(user);

		// Create the user login source
		UserLoginSourceImpl userLoginSource = new UserLoginSourceImpl();
		userLoginSource.setLoginSource(loginSource);
		userLoginSource.setLoginToken(loginToken);
		userLoginSource.setUser(user);

		// Save the user login source
		userLoginSource = userLoginSourcesRepository.save(userLoginSource);
	
		// Return the reference to the saved user login source
		return userLoginSource;
	}

	@Override
	@Transactional
	public User mapUserToGroup(User user, Group group) {
		userGroupsRepository.save(new UserGroup(null, ((UserImpl) user).getId(), ((GroupImpl) group).getId()));
		return usersRepository.findById(((UserImpl) user).getId()).get();
	}

	@Override
	public Group findGroupByName(String groupName) {
		return groupsRepository.findFirstByName(groupName);
	}

}
