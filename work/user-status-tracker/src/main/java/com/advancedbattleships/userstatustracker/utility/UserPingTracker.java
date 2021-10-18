package com.advancedbattleships.userstatustracker.utility;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toSet;
import static java.util.Collections.emptySet;

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
						entry -> isUserTokenAlive(entry, nowMillis)
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

	/**
	 * Returns true if the last ping date registered for the given user token
	 * is not before the current date minus the keep alive interval. Returns
	 * false in any other circumstance, including when the user does not exist.
	 */
	public boolean isUserTokenAlive(String userUniqueToken) {
		// Get the last ping date
		Date userLastPingDate = userLastPingDates.get(userUniqueToken);

		// If the user is not found, return false
		if (userLastPingDate == null) {
			return false;
		}

		// Chek the last ping date against the current time and return the result
		return isUserTokenAlive(userLastPingDate, new Date().getTime());
	}

	/**
	 * Give a list of user tokens to query, returns a subset of the given list,
	 * representing the user which are still alive.
	 */
	public Set<String> userTokensStillAlive(Set<String> userTokensToQuery) {
		// If the given list is invalid, return an empty set
		if (userTokensToQuery == null || userTokensToQuery.isEmpty()) {
			return emptySet();
		}

		// Get the current time in milliseconds
		long nowMillis = new Date().getTime();

		// Search and return
		return
			userLastPingDates.entrySet().parallelStream()
				.filter(entry -> userTokensToQuery.contains(entry.getKey()))
				.filter(entry -> isUserTokenAlive(entry, nowMillis)) // TODO: switch the order of the filters if there turn out to be less users online than the list of friends for any given user
				.map(Map.Entry::getKey)
				.collect(toSet())
			;
	}

	private boolean isUserTokenAlive(Map.Entry<String, Date> entry, long nowMillis) {
		return isUserTokenAlive(entry.getValue(), maxInactivityMillis);
	}

	private boolean isUserTokenAlive(Date userTokenDate, long nowMillis) {
		return nowMillis - userTokenDate.getTime() < maxInactivityMillis;
	}
}
