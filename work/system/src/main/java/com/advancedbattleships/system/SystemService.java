package com.advancedbattleships.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemService {

	@Autowired
	CachedSystemDataServiceWrapper dataService;

	public CachedSystemDataServiceWrapper getDataService() {
		return dataService;
	}
}
