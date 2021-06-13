package com.advancedbattleships.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.advancedbattleships.system.dataservice.SystemDataService;

@Service
public class CachedSystemDataServiceWrapper {

	@Autowired
	private SystemDataService systemDataService;

	@Cacheable("stringParams")
	public String getStringParameter(String paramName) {
		return systemDataService.getStringParameter(paramName);
	}

	@Cacheable("longParams")
	public Long getLongParameter(String paramName) {
		return systemDataService.getLongParameter(paramName);
	}

	@Cacheable("intParams")
	public Integer getIntParameter(String paramName) {
		return systemDataService.getIntParameter(paramName);
	}

	@Cacheable("doubleParams")
	public Double getDoubleParameter(String paramName) {
		return systemDataService.getDoubleParameter(paramName);
	}

}
