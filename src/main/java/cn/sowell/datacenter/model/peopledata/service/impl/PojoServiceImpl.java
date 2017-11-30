package cn.sowell.datacenter.model.peopledata.service.impl;

import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import cn.sowell.datacenter.model.peopledata.service.PojoService;

@Service
public class PojoServiceImpl implements PojoService{
	SpelExpressionParser parser;

	public PojoServiceImpl() {
		SpelParserConfiguration config = new SpelParserConfiguration(true, true);
		this.parser = new SpelExpressionParser(config);
	}
	
	@Override
	public PropertyParser createPropertyParser(Object pojo) {
		return new PropertyParser(pojo, this.parser);
	}

}