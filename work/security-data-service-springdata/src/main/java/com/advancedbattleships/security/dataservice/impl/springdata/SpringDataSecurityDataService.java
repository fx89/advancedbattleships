package com.advancedbattleships.security.dataservice.impl.springdata;

import static com.advancedbattleships.common.lang.Multicast.multicastSet;

import java.util.Set;

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

@Service
public class SpringDataSecurityDataService implements SecurityDataService {

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
	@Transactional(transactionManager = "absSecurityTransactionManager")
	public UserLoginSource createUserLoginSource(String uniqueToken, String name, String pictureUrl, String primaryEmail,
			LoginSource loginSource, String loginToken, String nickName, Boolean isFirstLogin)
	{
		// Create the user
		UserImpl user = new UserImpl();
		user.setName(name);
		user.setPictureUrl(pictureUrl);
		user.setPrimaryEmailAddress(primaryEmail);
		user.setUniqueToken(uniqueToken);
		user.setNickName(nickName);
		user.setFirstLogin(isFirstLogin);
		user.setOnline(true);

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
	@Transactional(transactionManager = "absSecurityTransactionManager")
	public User mapUserToGroup(User user, Group group) {
		userGroupsRepository.save(new UserGroup(null, ((UserImpl) user).getId(), ((GroupImpl) group).getId()));
		return usersRepository.findById(((UserImpl) user).getId()).get();
	}

	@Override
	public Group findGroupByName(String groupName) {
		return groupsRepository.findFirstByName(groupName);
	}

	@Override
	public User saveUser(User user) {
		return usersRepository.save(new UserImpl(user));
	}

	@Override
	public User findUserByUniqueToken(String uniqueToken) {
		return usersRepository.findOneByUniqueToken(uniqueToken);
	}

	@Override
	public User findUserByNickName(String nickName) {
		return usersRepository.findOneByNickName(nickName);
	}

	@Override
	public Set<User> findUsersByUniqueToken(Set<String> uniqueTokens) {
		return multicastSet(usersRepository.findAllByUniqueTokenIn(uniqueTokens));
	}

	@Override
	@Transactional("absSecurityTransactionManager")
	public void setOnlineFlagForUsers(Set<String> userUniqueTokens, Boolean loggedIn) {
		usersRepository.setOnlineFlagWhereUserUniqueTokenIn(userUniqueTokens, loggedIn);
	}
}
