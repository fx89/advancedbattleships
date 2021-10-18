package com.advancedbattleships.userstatustracker.service;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.security.service.SecurityService;
import com.advancedbattleships.system.SystemService;
import com.advancedbattleships.userstatustracker.utility.UserPingTracker;
import com.advancedbattleships.utilityservices.TaskManagerService;

@Service
public class UserStatusTrackerService {

	@Autowired
	private SystemService system;

	@Autowired
	private SecurityService security;

	@Autowired
	TaskManagerService taskManager;
	
	private UserPingTracker userPingTracker = new UserPingTracker();

	@PostConstruct
	public void init() {
		// Get system parameters
		final Integer SESSION_CHECK_SECS = system.getDataService().getIntParameter("SECURITY.SESSION_CHECK_SECS");
		final Integer EXPECTED_ONLINE_USER_COUNT = system.getDataService().getIntParameter("SECURITY.EXPECTED_ONLINE_USER_COUNT");

		// Initialize ping tracker based on system parameters
		userPingTracker
			.withMaxInactivitySecs(SESSION_CHECK_SECS)
			.withExpectedUserCount(EXPECTED_ONLINE_USER_COUNT);

		// Start a task for running the ping tracker
		taskManager.startRecurrentTask("sessionCheck", SESSION_CHECK_SECS, () -> sessionCheck());

		// Add the post-login callback
		security.addPostLoginCallback(user -> {
			pingUser(user.getUniqueToken());
		});
	}


	/**
	 * Sets the "logged in" flag to false for all users who are not active
	 */
	public void sessionCheck() {
		Set<String> inactiveUserTokens = userPingTracker.computeInactiveUserTokens();

		if (!inactiveUserTokens.isEmpty()) {
			security.setOnlineFlagForUsers(inactiveUserTokens, false);
			userPingTracker.clearInactiveUserTokens(inactiveUserTokens);
		}

		// TODO: Eventually run a full cleanup once every hour,
		// to set the online flag to false for any user that's not in the online users list

		// TODO: Try to figure out a way to do this even if there will be many instances of the
		// user service (maybe move the functionality into a micro-service and make sure it has only one instance)
	}

	/**
	 * Issues a ping to the user ping tracker for the given user unique token
	 */
	public void pingUser(String userUiqueToken) {
		userPingTracker.pingUser(userUiqueToken);
	}

	/**
	 * Returns true if the last ping date registered for the given user token
	 * is not before the current date minus the keep alive interval. Returns
	 * false in any other circumstance, including when the user does not exist.
	 */
	public boolean isUserTokenAlive(String userUniqueToken) {
		return userPingTracker.isUserTokenAlive(userUniqueToken);
	}

	/**
	 * Give a list of user tokens to query, returns a subset of the given list,
	 * representing the user which are still alive.
	 */
	public Set<String> getUserTokensStillAlive(Set<String> userTokensToQuery) {
		return userPingTracker.userTokensStillAlive(userTokensToQuery);
	}
}
