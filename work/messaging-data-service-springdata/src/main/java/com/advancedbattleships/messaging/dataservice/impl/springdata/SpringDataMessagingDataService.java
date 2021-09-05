package com.advancedbattleships.messaging.dataservice.impl.springdata;

import static com.advancedbattleships.common.lang.Multicast.multicastCollection;
import static com.advancedbattleships.common.lang.Multicast.multicastList;
import static com.advancedbattleships.common.lang.Multicast.multicastSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.messaging.dataservice.MessagingDataService;
import com.advancedbattleships.messaging.dataservice.impl.springdata.dao.PersistentMessageChannelsRepository;
import com.advancedbattleships.messaging.dataservice.impl.springdata.dao.PersistentMessageSourceTypesRepository;
import com.advancedbattleships.messaging.dataservice.impl.springdata.dao.PersistentMessageTypesRepository;
import com.advancedbattleships.messaging.dataservice.impl.springdata.dao.PersistentMessagesRepository;
import com.advancedbattleships.messaging.dataservice.impl.springdata.model.PersistentMessageChannelImpl;
import com.advancedbattleships.messaging.dataservice.impl.springdata.model.PersistentMessageImpl;
import com.advancedbattleships.messaging.dataservice.impl.springdata.model.PersistentMessageTypeImpl;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessage;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageChannel;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageSourceType;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageType;

@Service
public class SpringDataMessagingDataService implements MessagingDataService {

	@Autowired
	PersistentMessagesRepository persistentMessagesRepository;

	@Autowired
	PersistentMessageChannelsRepository persistentMessageChannelsRepository;

	@Autowired
	PersistentMessageTypesRepository persistentMessageTypesRepository;

	@Autowired
	PersistentMessageSourceTypesRepository persistentMessageSourceTypesRepository;

	@Override
	public List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndRead(String userUniqueToken, Boolean read) {
		return multicastList(
			persistentMessagesRepository.findAllByUserUniqueTokenAndIsRead(userUniqueToken, read)
		);
		
	}

	@Override
	public List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndReadAndChannelName(
		String userUniqueToken,
		Boolean read,
		String channelName
	) {
		return multicastList(
			persistentMessagesRepository.findAllByUserUniqueTokenAndIsReadAndChannelName(userUniqueToken, read, channelName)
		);
	}

	@Override
	public List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndReadAndChannel(
		String userUniqueToken,
		Boolean read,
		PersistentMessageChannel channel
	) {
		return multicastList(
			persistentMessagesRepository.findAllByUserUniqueTokenAndIsReadAndChannelId(userUniqueToken, read, (new PersistentMessageChannelImpl(channel)).getId())
		);
	}

	@Override
	public PersistentMessage newPersistentMessage() {
		return new PersistentMessageImpl();
	}

	@Override
	public PersistentMessage savePersistentMessage(PersistentMessage persistentMessage) {
		return persistentMessagesRepository.save(new PersistentMessageImpl(persistentMessage));
	}

	@Override
	public void savePersistentMessages(Collection<PersistentMessage> persistentMessages) {
		persistentMessagesRepository.saveAll(
			multicastCollection(
				persistentMessages,
				n -> new ArrayList<>(n),
				m -> new PersistentMessageImpl(m)
			)
		);
	}

	@Override
	public Set<PersistentMessageChannel> findAllPersistentMessageChannels() {
		return multicastSet(persistentMessageChannelsRepository.findAll());
	}

	@Override
	public Set<PersistentMessageChannel> findAllPersistentMessageChannelsByMessageType(PersistentMessageType messageType) {
		return multicastSet(
			persistentMessageChannelsRepository.findAllByMessageTypeId(((PersistentMessageTypeImpl)messageType).getId())
		);
	}

	@Override
	public Set<PersistentMessageChannel> findAllPersistentMessageChannelsByMessageTypeName(String messageTypeName) {
		return multicastSet(
			persistentMessageChannelsRepository.findAllByMessageTypeName(messageTypeName)
		);
	}

	@Override
	public PersistentMessageChannel newPersistentMessageChannel() {
		return new PersistentMessageChannelImpl();
	}

	@Override
	public PersistentMessageChannel savePersistentMessageChannel(PersistentMessageChannel peristentMessageChannel) {
		return persistentMessageChannelsRepository
				.save(new PersistentMessageChannelImpl(peristentMessageChannel));
	}

	@Override
	public void savePersistentMessageChannels(Collection<PersistentMessageChannel> persistentMessageChannels) {
		persistentMessageChannelsRepository.saveAll(
			multicastCollection(
				persistentMessageChannels,
				n -> new ArrayList<>(n),
				c -> new PersistentMessageChannelImpl(c)
			)
		);
	}

	@Override
	public Set<PersistentMessageType> findAllPersistentMessageTypes() {
		return multicastSet(persistentMessageTypesRepository.findAll());
	}

	@Override
	public PersistentMessageType newPersistentMessageType() {
		return new PersistentMessageTypeImpl();
	}

	@Override
	public PersistentMessageType savePersistentMessageType(PersistentMessageType persistentMessageType) {
		return persistentMessageTypesRepository.save(new PersistentMessageTypeImpl(persistentMessageType));
	}

	@Override
	public void savePersistentMessageTypes(Collection<PersistentMessageType> persistentMessageTypes) {
		persistentMessageTypesRepository.saveAll(
			multicastCollection(
				persistentMessageTypes,
				n -> new ArrayList<>(n),
				t -> new PersistentMessageTypeImpl(t)
			)
		);
	}

	@Override
	public Set<PersistentMessageSourceType> findAllPersistentMessageSourceTypes() {
		return multicastSet(persistentMessageSourceTypesRepository.findAll());
	}

	@Override
	public List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndIsUserNotified(
		String userUniqueToken, Boolean isUserNotified
	) {
		return
			multicastList(
				persistentMessagesRepository
					.findAllByUserUniqueTokenAndIsUserNotified(userUniqueToken, isUserNotified)
			);
	}
}