package cn.sowell.datacenter.model.peopledata.service;

import cn.sowell.datacenter.model.peopledata.service.impl.PropertyParser;

public interface PojoService {
	PropertyParser createPropertyParser(Object pojo);
	
}
