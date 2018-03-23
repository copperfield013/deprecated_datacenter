package cn.sowell.datacenter.model.dict.service;

import java.util.Set;

import cn.sowell.datacenter.model.modules.FieldParserDescription;

public interface FieldObserverService {

	Set<FieldParserDescription> getDynamicFieldDescriptionSet(String module);
	void updateDynamicFiedDescriptionSet(String module);
}
