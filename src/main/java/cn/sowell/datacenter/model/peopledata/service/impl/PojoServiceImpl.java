package cn.sowell.datacenter.model.peopledata.service.impl;

import javax.annotation.Resource;

import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.spring.propTranslator.PropertyParser;
import cn.sowell.copframe.spring.propTranslator.PropertyValueTranslatorSet;
import cn.sowell.datacenter.model.peopledata.service.PojoService;

@Service
public class PojoServiceImpl implements PojoService{
	SpelExpressionParser parser;
	@Resource
	PropertyValueTranslatorSet translatorSet;
	
	public PojoServiceImpl() {
		SpelParserConfiguration config = new SpelParserConfiguration(true, true);
		this.parser = new SpelExpressionParser(config);
	}
	
	@Override
	public PropertyParser createPropertyParser(Object pojo) {
		return new PropertyParser(pojo, this.parser, translatorSet);
	}

}
