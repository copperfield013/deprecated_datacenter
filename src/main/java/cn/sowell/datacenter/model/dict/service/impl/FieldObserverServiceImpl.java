package cn.sowell.datacenter.model.dict.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.dict.service.DictionaryService;
import cn.sowell.datacenter.model.dict.service.FieldObserverService;
import cn.sowell.datacenter.model.modules.FieldParserDescription;

@Service
public class FieldObserverServiceImpl implements FieldObserverService {

	Map<String, Set<FieldParserDescription>> fieldDescsMap = new HashMap<>();
	
	@Resource
	DictionaryService dictService;
	
	@Override
	public Set<FieldParserDescription> getDynamicFieldDescriptionSet(String module) {
		if(!fieldDescsMap.containsKey(module)) {
			Set<FieldParserDescription> fieldDescs = getLastFieldDescs(module);
			fieldDescsMap.put(module, fieldDescs);
			return fieldDescs;
		}
		return fieldDescsMap.get(module);
	}

	private Set<FieldParserDescription> getLastFieldDescs(String module){
		Set<FieldParserDescription> fieldDescs = CollectionUtils.toSet(dictService.getAllFields(module), field->new FieldParserDescription(field));
		return Collections.synchronizedSet(fieldDescs);
	}
	
	@Override
	public void updateDynamicFiedDescriptionSet(String module) {
		if(fieldDescsMap.containsKey(module)) {
			Set<FieldParserDescription> lastFieldDescs = getLastFieldDescs(module);
			Set<FieldParserDescription> fieldDescs = fieldDescsMap.get(module);
			fieldDescs.clear();
			fieldDescs.addAll(lastFieldDescs);
		}else {
			getDynamicFieldDescriptionSet(module);
		}
		
	}

}
