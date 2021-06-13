package com.advancedbattleships.system.dataservice.impl.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advancedbattleships.system.dataservice.SystemDataService;
import com.advancedbattleships.system.dataservice.impl.springdata.dao.ParametersRepository;
import com.advancedbattleships.system.dataservice.impl.springdata.model.Parameter;

@Service
public class SpringDataSystemDataService implements SystemDataService {

	@Autowired
	private ParametersRepository parametersRepository;

	@Override
	public String getStringParameter(String paramName) {
		Parameter ret = parametersRepository.findFirstByName(paramName);

		if (ret == null) {
			return null;
		}

		return ret.getValueChar();
	}

	@Override
	public Long getLongParameter(String paramName) {
		Parameter ret = parametersRepository.findFirstByName(paramName);

		if (ret == null) {
			return null;
		}

		return ret.getValueNumber();
	}

	@Override
	public Integer getIntParameter(String paramName) {
		Parameter ret = parametersRepository.findFirstByName(paramName);

		if (ret == null) {
			return null;
		}

		long l = ret.getValueNumber();
		int i = (int) l;

		return i;
	}

	@Override
	public Double getDoubleParameter(String paramName) {
		Parameter ret = parametersRepository.findFirstByName(paramName);

		if (ret == null) {
			return null;
		}

		return ret.getValueDecimal();
	}

}
