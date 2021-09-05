package com.advancedbattleships.social.dataservice;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.advancedbattleships.social.dataservice.model.FriendStatus;
import com.advancedbattleships.social.dataservice.model.Party;
import com.advancedbattleships.social.dataservice.model.UserFriend;
import com.advancedbattleships.social.dataservice.model.UserParty;

public interface SocialDataService {

	/**
	 * Returns a set of parties for which the name contains the given string
	 */
	public Set<Party> findPartiesByNameLike(String nameLike);

	/**
	 * Returns a set of unique tokens of the users in the given party
	 */
	public Set<String> getPartyUserUniqueTokens(String partyUniqueToken);

	/**
	 * Returns a set of parties to which the current user belongs
	 */
	public Set<Party> getUserParties(String userUniqueToken);

	/**
	 * Returns a set of unique friends of the user whose unique token is given as
	 * parameter
	 */
	public Set<UserFriend> getUserFriends(String userUniqueToken);
	
	/**
	 * Returns a set of unique friends of the user whose unique token is given as
	 * parameter, as long as the friendship status has the given name
	 */
	public Set<UserFriend> getUserFriends(String userUniqueToken, String statusName);

	/**
	 * Returns a set of unique friends of the user whose unique token is given as
	 * parameter, as long as the friendship status has one of the given names
	 */
	public Set<UserFriend> getUserFriends(String userUniqueToken, List<String> statusNames);

	/**
	 * Returns a user friend, if exists, or null if it doesn't
	 */
	public UserFriend findUserFriendByUserUniqueTokenAndFriendUniqueToken(String userUniqueToken, String friendUniqueToken);

	/**
	 * Returns a new empty instance of the data type used by the data layer
	 */
	public UserFriend newUserFriend();

	/**
	 * Saves one instance and returns the saved result
	 */
	public UserFriend saveUserFriend(UserFriend userFirend);

	/**
	 * Deletes the referenced user friend
	 */
	public void deleteUserFriend(UserFriend userFriend);

	/**
	 * Saves multiple instances
	 */
	public void saveUserFriends(Collection<UserFriend> userFriends);

	/**
	 * Returns a new empty instance of the data type used by the data layer
	 */
	public UserParty newUserParty();

	/**
	 * Saves one instance and returns the saved result
	 */
	public UserParty saveUserParty(UserParty userParty);

	/**
	 * Saves multiple instances
	 */
	public void saveUserParties(Collection<UserParty> userParties);

	/**
	 * Returns a new empty instance of the data type used by the data layer
	 */
	public Party newParty();

	/**
	 * Saves one instance and returns the saved result
	 */
	public Party saveParty(Party party);

	/**
	 * Saves multiple instances
	 */
	public void saveParties(Collection<Party> parties);

	/**
	 * Run the given code in a transaction
	 */
	public void executeTransaction(Runnable code);

	/**
	 * Returns a list of all friend statuses defined in the database
	 */
	public Iterable<FriendStatus> findAllFriendStatuses();
}
