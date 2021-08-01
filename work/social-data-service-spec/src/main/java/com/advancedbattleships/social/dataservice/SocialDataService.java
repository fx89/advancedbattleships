package com.advancedbattleships.social.dataservice;

import java.util.Collection;
import java.util.Set;

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
	 * Returns a set of unique tokens belonging to the friends of the user whose
	 * unique token is given as parameter
	 */
	public Set<String> getUserFriendsUniqueTokens(String userUniqueToken);

	/**
	 * Returns a new empty instance of the data type used by the data layer
	 */
	public UserFriend newUserFriend();

	/**
	 * Saves one instance and returns the saved result
	 */
	public UserFriend saveUserFirend(UserFriend userFirend);

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
}
