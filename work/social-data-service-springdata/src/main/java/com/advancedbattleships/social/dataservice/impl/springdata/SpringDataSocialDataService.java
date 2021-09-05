package com.advancedbattleships.social.dataservice.impl.springdata;

import static com.advancedbattleships.common.lang.Multicast.multicastCollection;
import static com.advancedbattleships.common.lang.Multicast.multicastSet;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advancedbattleships.social.dataservice.SocialDataService;
import com.advancedbattleships.social.dataservice.impl.springdata.dao.PartiesRepository;
import com.advancedbattleships.social.dataservice.impl.springdata.dao.UserFriendsRepository;
import com.advancedbattleships.social.dataservice.impl.springdata.dao.UserPartiesRepository;
import com.advancedbattleships.social.dataservice.impl.springdata.model.PartyImpl;
import com.advancedbattleships.social.dataservice.impl.springdata.model.UserFriendImpl;
import com.advancedbattleships.social.dataservice.impl.springdata.model.UserPartyImpl;
import com.advancedbattleships.social.dataservice.model.Party;
import com.advancedbattleships.social.dataservice.model.UserFriend;
import com.advancedbattleships.social.dataservice.model.UserParty;

@Service
public class SpringDataSocialDataService implements SocialDataService {

	@Autowired
	private PartiesRepository partiesRepository;

	@Autowired
	private UserPartiesRepository userPartiesRepository;

	@Autowired
	private UserFriendsRepository userFriendsRepository;

	@Override
	public Set<Party> findPartiesByNameLike(String nameLike) {
		return multicastSet(partiesRepository.findAllByNameLike(nameLike));
	}

	@Override
	public Set<String> getPartyUserUniqueTokens(String partyUniqueToken) {
		return userPartiesRepository.findAllByPartyPartyUniqueToken(partyUniqueToken)
			.stream()
				.map(UserPartyImpl::getUserUniqueToken)
				.collect(toSet());
	}

	@Override
	public Set<Party> getUserParties(String userUniqueToken) {
		return userPartiesRepository.findAllByUserUniqueToken(userUniqueToken)
			.stream()
				.map(UserPartyImpl::getParty)
				.collect(toSet());
	}

	@Override
	public Set<UserFriend> getUserFriends(String userUniqueToken) {
		return multicastSet(userFriendsRepository.findAllByUserUniqueToken(userUniqueToken));
	}

	@Override
	public UserFriend newUserFriend() {
		return new UserFriendImpl();
	}


	@Override
	public UserFriend saveUserFriend(UserFriend userFriend) {
		return userFriendsRepository.save(new UserFriendImpl(userFriend));
	}

	@Override
	public void saveUserFriends(Collection<UserFriend> userFriends) {
		userFriendsRepository.saveAll(
			multicastCollection(
				userFriends,
				(s) -> new ArrayList<>(s),
				userFriend -> new UserFriendImpl(userFriend)
			)
		);
	}

	@Override
	public UserParty newUserParty() {
		return new UserPartyImpl();
	}

	@Override
	public UserParty saveUserParty(UserParty userParty) {
		return userPartiesRepository.save(new UserPartyImpl(userParty));
	}

	@Override
	public void saveUserParties(Collection<UserParty> userParties) {
		userPartiesRepository.saveAll(
			multicastCollection(
					userParties,
				(s) -> new ArrayList<>(s),
				userParty -> new UserPartyImpl(userParty)
			)
		);
	}

	@Override
	public Party newParty() {
		return new PartyImpl();
	}

	@Override
	public Party saveParty(Party party) {
		return partiesRepository.save(new PartyImpl(party));
	}

	@Override
	public void saveParties(Collection<Party> parties) {
		partiesRepository.saveAll(
			multicastCollection(
					parties,
				(s) -> new ArrayList<>(s),
				party -> new PartyImpl(party)
			)
		);
	}

	@Override
	public UserFriend findUserFriendByUserUniqueTokenAndFriendUniqueToken(String userUniqueToken, String friendUniqueToken) {
		return userFriendsRepository.findOneByUserUniqueTokenAndFriendUserUniqueToken(userUniqueToken, friendUniqueToken);
	}

	@Override
	@Transactional("absSocialTransactionManager")
	public void executeTransaction(Runnable code) {
		code.run();
	}
}
