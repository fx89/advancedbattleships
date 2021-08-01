package com.advancedbattleships.social.dataservice.impl.springdata;

import static com.advancedbattleships.common.lang.Multicast.multicastCollection;
import static com.advancedbattleships.common.lang.Multicast.multicastSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.chat.dataservice.ChatDataService;
import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.dataservice.model.ChatChannelBan;
import com.advancedbattleships.social.dataservice.impl.springdata.dao.ChatChannelBansRepository;
import com.advancedbattleships.social.dataservice.impl.springdata.dao.ChatChannelsRepository;
import com.advancedbattleships.social.dataservice.impl.springdata.model.ChatChannelBanImpl;
import com.advancedbattleships.social.dataservice.impl.springdata.model.ChatChannelImpl;

@Service
public class SpringDataChatDataServiceImpl implements ChatDataService {

	@Autowired
	ChatChannelsRepository chatChannelsRepository;

	@Autowired
	ChatChannelBansRepository chatChannelBansRepository;

	@Override
	public Set<ChatChannel> findAllChatChannels() {
		return multicastSet(chatChannelsRepository.findAll());
	}

	@Override
	public Set<ChatChannel> findAllPublicChatChannels() {
		return multicastSet(chatChannelsRepository.findAllByIsPrivate(false));
	}

	@Override
	public Set<ChatChannel> findChatChannelsForParties(Collection<String> partyUniqueTokens) {
		return multicastSet(chatChannelsRepository.findAllByPartyUniqueTokenIn(partyUniqueTokens));
	}

	@Override
	public ChatChannel newChatChannel() {
		return new ChatChannelImpl();
	}

	@Override
	public ChatChannel saveChatChannel(ChatChannel chatChannel) {
		return chatChannelsRepository.save(new ChatChannelImpl(chatChannel));
	}

	@Override
	public void saveChatChannels(Collection<ChatChannel> chatChannels) {
		chatChannelsRepository.saveAll(
			multicastCollection(
				chatChannels,
				(s) -> new ArrayList<>(s),
				chatChannel -> new ChatChannelImpl(chatChannel)
			)
		);
	}

	@Override
	public Collection<ChatChannelBan> findAllChatChannelBans() {
		return multicastCollection(
			chatChannelBansRepository.findAll(),
			(s) -> new ArrayList<>(s),
			chatChannelBan -> chatChannelBan
		);
	}

	@Override
	public Collection<ChatChannelBan> findAllChatChannelBansByChatChannel(ChatChannel chatChannel) {
		return multicastCollection(
			chatChannelBansRepository.findAllByChatChannelId(
				((ChatChannelImpl) chatChannel).getId()
			),
			(s) -> new ArrayList<>(s),
			chatChannelBan -> chatChannelBan
		);
	}

	@Override
	public Collection<ChatChannelBan> findAllChatChannelBansByChatChannelName(String chatChannelName) {
		return multicastCollection(
			chatChannelBansRepository.findAllByChatChannelName(chatChannelName),
			(s) -> new ArrayList<>(s),
			chatChannelBan -> chatChannelBan
		);
	}

	@Override
	public Collection<ChatChannelBan> findAllChatChannelBansByUserUniqueToken(String userUniqueToken) {
		return multicastCollection(
			chatChannelBansRepository.findAllByUserUniqueToken(userUniqueToken),
			(s) -> new ArrayList<>(s),
			chatChannelBan -> chatChannelBan
		);
	}

	@Override
	public Collection<ChatChannelBan> findAllChatChannelBansByUserUniqueTokenAndTimeTillLiftedMinsGreaterThan(
		String userUniqueToken,
		Long timeTillLiftedMins
	) {
		return multicastCollection(
			chatChannelBansRepository.findAllByUserUniqueTokenAndTimeTillLiftedMinsGreaterThan(
				userUniqueToken,
				timeTillLiftedMins
			),
			(s) -> new ArrayList<>(s),
			chatChannelBan -> chatChannelBan
		);
	}

	@Override
	public Collection<ChatChannelBan> findAllChatChannelBansByUserUniqueTokenAndChatChannelAndTimeTillLiftedMinsGreaterThan(
		String userUniqueToken,
		ChatChannel chatChannel,
		Long timeTillLiftedMins
	) {
		return multicastCollection(
			chatChannelBansRepository.findAllByUserUniqueTokenAndChatChannelIdAndTimeTillLiftedMinsGreaterThan(
				userUniqueToken,
				((ChatChannelImpl) chatChannel).getId(),
				timeTillLiftedMins
			),
			(s) -> new ArrayList<>(s),
			chatChannelBan -> chatChannelBan
		);
	}

	@Override
	public Collection<ChatChannelBan> findAllChatChannelBansByUserUniqueTokenAndChatChannelNameAndTimeTillLiftedMinsGreaterThan(
		String userUniqueToken,
		String chatChannelName,
		Long timeTillLiftedMins
	) {
		return multicastCollection(
			chatChannelBansRepository.findAllByUserUniqueTokenAndChatChannelNameAndTimeTillLiftedMinsGreaterThan(
				userUniqueToken,
				chatChannelName,
				timeTillLiftedMins
			),
			(s) -> new ArrayList<>(s),
			chatChannelBan -> chatChannelBan
		);
	}

	@Override
	public ChatChannelBan newChatChannelBan() {
		return new ChatChannelBanImpl();
	}

	@Override
	public ChatChannelBan saveChatChannelBan(ChatChannelBan chatChannelBan) {
		return chatChannelBansRepository.save(new ChatChannelBanImpl(chatChannelBan));
	}

	@Override
	public void saveChatChannelBans(Collection<ChatChannelBan> chatChannelBans) {
		chatChannelBansRepository.saveAll(
			multicastCollection(
				chatChannelBans,
				s -> new ArrayList<>(s),
				chatChannelBan -> new ChatChannelBanImpl(chatChannelBan)
			)
		);
	}

	@Override
	public void deleteChatChannelBan(ChatChannelBan chatChannelBan) {
		chatChannelBansRepository.delete(new ChatChannelBanImpl(chatChannelBan));
	}

	@Override
	public void deleteChatChannelBans(Collection<ChatChannelBan> chatChannelBans) {
		chatChannelBansRepository.deleteAll(
			multicastCollection(
				chatChannelBans,
				s -> new ArrayList<>(s),
				chatChannelBan -> new ChatChannelBanImpl(chatChannelBan)
			)
		);
	}

}
