package com.advancedbattleships.social.dataservice.impl.springdata.dao;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.advancedbattleships.social.dataservice.impl.springdata.model.ChatChannelImpl;

public interface ChatChannelsRepository extends PagingAndSortingRepository<ChatChannelImpl, Long> {

	Set<ChatChannelImpl> findAll();

	Set<ChatChannelImpl> findAllByIsPrivate(Boolean isPrivate);

	Set<ChatChannelImpl> findAllByPartyUniqueTokenIn(Collection<String> partyUniqueTokens);
}
