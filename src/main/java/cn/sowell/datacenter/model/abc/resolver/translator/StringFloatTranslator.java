package cn.sowell.datacenter.model.abc.resolver.translator;

import cn.sowell.datacenter.model.abc.resolver.PropertyTranslator;

public class StringFloatTranslator implements PropertyTranslator<String, Float> {

	@Override
	public boolean check(String propValue) {
		try {
			return propValue == null || Float.valueOf(propValue) != null;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public Float transfer(String propValue) {
		return propValue == null? null: Float.valueOf(propValue);
	}


}
