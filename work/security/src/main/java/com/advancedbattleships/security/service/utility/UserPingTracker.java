package com.advancedbattleships.security.service.utility;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toSet;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserPingTracker {
	private Map<String, Date> userLastPingDates = new ConcurrentHashMap<>();

	private long maxInactivityMillis = 200000;

	/**
	 * Initial memory allocation for the given user count
	 */
	public UserPingTracker withExpectedUserCount(int expecteduserCount) {
		userLastPingDates = new ConcurrentHashMap<>(expecteduserCount);
		return this;
	}

	/**
	 * If no ping from a given user is received for the amount of time
	 * set here, then the user is considered inactive
	 */
	public UserPingTracker withMaxInactivitySecs(int maxInactivitySecs) {
		this.maxInactivityMillis = maxInactivitySecs * 1000;
		return this;
	} 

	/**
	 * Register the ping from the user referenced by the given unique token,
	 * so it doesn't become inactive 
	 */
	public synchronized void pingUser(String userUniqueToken) {
		userLastPingDates.put(userUniqueToken, new Date());
	}

	/**
	 * Fetch a set of unique tokens for the inactive users based on the last
	 * ping time of each user session
	 */
	public Set<String> computeInactiveUserTokens() {
		// Get the current time in milliseconds
		long nowMillis = new Date().getTime();

		// Split users into active and inactive
		Map<Boolean, List<Map.Entry<String, Date>>> splitUsers
			= userLastPingDates.entrySet().parallelStream()
				.collect(partitioningBy((
						entry -> nowMillis - entry.getValue().getTime() < maxInactivityMillis
					)));

		// Get a set of inactive user tokens
		return splitUsers.get(false).stream()
					.map(Map.Entry::getKey)
					.collect(toSet());
	}

	/**
	 * Clear the inactive user tokens from memory, so to make processing easier
	 * for the next iteration
	 * @param inactiveUserTokens
	 */
	public void clearInactiveUserTokens(Set<String> inactiveUserTokens) {
		inactiveUserTokens.stream().forEach(inactiveUserToken -> {
			userLastPingDates.remove(inactiveUserToken);
		});
	}
}
