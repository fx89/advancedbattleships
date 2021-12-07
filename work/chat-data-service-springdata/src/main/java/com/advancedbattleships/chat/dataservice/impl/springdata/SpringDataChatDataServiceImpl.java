package com.advancedbattleships.chat.dataservice.impl.springdata;

import static com.advancedbattleships.common.lang.Multicast.multicastCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.chat.dataservice.ChatDataService;
import com.advancedbattleships.chat.dataservice.impl.springdata.dao.ChatChannelBansRepository;
import com.advancedbattleships.chat.dataservice.impl.springdata.dao.ChatChannelsRepository;
import com.advancedbattleships.chat.dataservice.impl.springdata.model.ChatChannelBanImpl;
import com.advancedbattleships.chat.dataservice.impl.springdata.model.ChatChannelImpl;
import com.advancedbattleships.chat.dataservice.model.ChatChannel;
import com.advancedbattleships.chat.dataservice.model.ChatChannelBan;

@Service
public class SpringDataChatDataServiceImpl implements ChatDataService {

	@Autowired
	ChatChannelsRepository chatChannelsRepository;

	@Autowired
	ChatChannelBansRepository chatChannelBansRepository;

	@Override
	public Set<? extends ChatChannel> findAllChatChannels() {
		return chatChannelsRepository.findAll();
	}

	@Override
	public Set<? extends ChatChannel> findAllPublicChatChannels() {
		return chatChannelsRepository.findAllByIsPrivate(false);
	}

	@Override
	public Set<? extends ChatChannel> findChatChannelsForParties(Collection<String> partyUniqueTokens) {
		return chatChannelsRepository.findAllByPartyUniqueTokenIn(partyUniqueTokens);
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
	public void saveChatChannels(Collection<? extends ChatChannel> chatChannels) {
		chatChannelsRepository.saveAll(
			multicastCollection(
				chatChannels,
				(s) -> new ArrayList<>(s),
				chatChannel -> new ChatChannelImpl(chatChannel)
			)
		);
	}

	@Override
	public Collection<? extends ChatChannelBan> findAllChatChannelBans() {
		return chatChannelBansRepository.findAll();
	}

	@Override
	public Collection<? extends ChatChannelBan> findAllChatChannelBansByChatChannel(ChatChannel chatChannel) {
		return 
			chatChannelBansRepository.findAllByChatChannelId(
				((ChatChannelImpl) chatChannel).getId()
			);
	}

	@Override
	public Collection<? extends ChatChannelBan> findAllChatChannelBansByChatChannelName(String chatChannelName) {
		return chatChannelBansRepository.findAllByChatChannelName(chatChannelName);
	}

	@Override
	public Collection<? extends ChatChannelBan> findAllChatChannelBansByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(
			String userUniqueToken, String partyUniqueToken, Date timeWhenLifted
		)
	{
		return
			chatChannelBansRepository
				.findAllByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(
						userUniqueToken,
						partyUniqueToken,
						timeWhenLifted
					);
	}

	@Override
	public int countAllChatChannelBansByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(
		String userUniqueToken,
		String partyUniqueToken,
		Date timeWhenLifted
	) {
		return chatChannelBansRepository
				.countAllByUserUniqueTokenAndChatChannelPartyUniqueTokenAndTimeWhenLiftedAfter(
						userUniqueToken,
						partyUniqueToken,
						timeWhenLifted
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
	public void saveChatChannelBans(Collection<? extends ChatChannelBan> chatChannelBans) {
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
	public void deleteChatChannelBans(Collection<? extends ChatChannelBan> chatChannelBans) {
		chatChannelBansRepository.deleteAll(
			multicastCollection(
				chatChannelBans,
				s -> new ArrayList<>(s),
				chatChannelBan -> new ChatChannelBanImpl(chatChannelBan)
			)
		);
	}

}
