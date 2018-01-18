package cn.sowell.datacenter.model.peopledata.service;

import cn.sowell.copframe.spring.propTranslator.PropertyParser;


public interface PojoService {
	PropertyParser createPropertyParser(Object pojo);
	
}
