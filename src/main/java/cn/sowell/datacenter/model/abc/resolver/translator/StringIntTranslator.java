package cn.sowell.datacenter.model.abc.resolver.translator;

import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.datacenter.model.abc.resolver.PropertyTranslator;

public class StringIntTranslator implements PropertyTranslator<String, Integer> {

	@Override
	public boolean check(String propValue) {
		return propValue == null || propValue.matches("^\\d+$");
	}

	@Override
	public Integer transfer(String propValue) {
		return FormatUtils.toInteger(propValue);
	}

}
