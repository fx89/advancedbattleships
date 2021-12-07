package com.advancedbattleships.messaging.dataservice;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.advancedbattleships.messaging.dataservice.model.PersistentMessage;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageChannel;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageSourceType;
import com.advancedbattleships.messaging.dataservice.model.PersistentMessageType;

public interface MessagingDataService {

	List<? extends PersistentMessage> findPersistentMessagesByUserUniqueTokenAndRead(String userUniqueToken, Boolean read);

	List<? extends PersistentMessage> findPersistentMessagesByUserUniqueTokenAndReadAndChannelName(String userUniqueToken,
			Boolean read, String channelName);

	List<? extends PersistentMessage> findPersistentMessagesByUserUniqueTokenAndReadAndChannel(String userUniqueToken,
			Boolean read, PersistentMessageChannel channel);

	List<? extends PersistentMessage> findPersistentMessagesByUserUniqueTokenAndIsUserNotified(
			String userUniqueToken, Boolean isUserNotified);

	PersistentMessage newPersistentMessage();

	PersistentMessage savePersistentMessage(PersistentMessage persistentMessage);

	void savePersistentMessages(Collection<? extends PersistentMessage> persistentMessages);

	Set<? extends PersistentMessageChannel> findAllPersistentMessageChannels();

	Set<? extends PersistentMessageChannel> findAllPersistentMessageChannelsByMessageType(PersistentMessageType messageType);

	Set<? extends PersistentMessageChannel> findAllPersistentMessageChannelsByMessageTypeName(String messageTypeName);

	PersistentMessageChannel newPersistentMessageChannel();

	PersistentMessageChannel savePersistentMessageChannel(PersistentMessageChannel peristentMessageChannel);

	void savePersistentMessageChannels(Collection<? extends PersistentMessageChannel> persistentMessageChannels);

	Set<? extends PersistentMessageType> findAllPersistentMessageTypes();

	PersistentMessageType newPersistentMessageType();

	PersistentMessageType savePersistentMessageType(PersistentMessageType persistentMessageType);

	void savePersistentMessageTypes(Collection<? extends PersistentMessageType> persistentMessageTypes);

	Set<? extends PersistentMessageSourceType> findAllPersistentMessageSourceTypes();
}
