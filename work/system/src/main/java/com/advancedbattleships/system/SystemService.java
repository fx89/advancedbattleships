package com.advancedbattleships.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.system.model.MessagingProperties;

@Service
public class SystemService {

	@Autowired
	CachedSystemDataServiceWrapper dataService;

	public CachedSystemDataServiceWrapper getDataService() {
		return dataService;
	}

	public MessagingProperties getMessagingProperties() {
		MessagingProperties ret = new MessagingProperties();

		ret.setPollingIntervalMS(dataService.getIntParameter("MESSAGES.NOTIFICATIONS_POLLING_INTERVAL_MS"));
		ret.setMaxNotificationsOnScreen(dataService.getIntParameter("MESSAGES.MAX_NOTIFICATIONS_ON_SCREEN"));

		return ret;
	}
}
