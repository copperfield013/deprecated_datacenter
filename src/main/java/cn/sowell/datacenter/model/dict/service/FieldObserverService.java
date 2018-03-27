package cn.sowell.datacenter.model.dict.service;

import java.util.Set;

import cn.sowell.datacenter.model.abc.resolver.FieldParserDescription;

public interface FieldObserverService {

	Set<FieldParserDescription> getDynamicFieldDescriptionSet(String module);
	void updateDynamicFiedDescriptionSet(String module);
}
