package com.advancedbattleships.messaging.dataservice;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.advancedbattleships.messaging.dataservice.model.PersistentMessage;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageChannel;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageSourceType;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageType;

public interface MessagingDataService {

	List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndRead(String userUniqueToken, Boolean read);

	List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndReadAndChannelName(String userUniqueToken,
			Boolean read, String channelName);

	List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndReadAndChannel(String userUniqueToken,
			Boolean read, PersistentMessageChannel channel);

	List<PersistentMessage> findPersistentMessagesByUserUniqueTokenAndIsUserNotified(
			String userUniqueToken, Boolean isUserNotified);

	PersistentMessage newPersistentMessage();

	PersistentMessage savePersistentMessage(PersistentMessage persistentMessage);

	void savePersistentMessages(Collection<PersistentMessage> persistentMessages);

	Set<PersistentMessageChannel> findAllPersistentMessageChannels();

	Set<PersistentMessageChannel> findAllPersistentMessageChannelsByMessageType(PersistentMessageType messageType);

	Set<PersistentMessageChannel> findAllPersistentMessageChannelsByMessageTypeName(String messageTypeName);

	PersistentMessageChannel newPersistentMessageChannel();

	PersistentMessageChannel savePersistentMessageChannel(PersistentMessageChannel peristentMessageChannel);

	void savePersistentMessageChannels(Collection<PersistentMessageChannel> persistentMessageChannels);

	Set<PersistentMessageType> findAllPersistentMessageTypes();

	PersistentMessageType newPersistentMessageType();

	PersistentMessageType savePersistentMessageType(PersistentMessageType persistentMessageType);

	void savePersistentMessageTypes(Collection<PersistentMessageType> persistentMessageTypes);

	Set<PersistentMessageSourceType> findAllPersistentMessageSourceTypes();
}
