package cn.sowell.datacenter.model.peopledata.service;

import cn.sowell.datacenter.common.property.PropertyParser;

public interface PojoService {
	PropertyParser createPropertyParser(Object pojo);
	
}
