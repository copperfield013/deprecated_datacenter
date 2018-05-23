package cn.sowell.datacenter.model.dict.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.datacenter.entityResolver.Label;
import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;
import cn.sowell.datacenter.model.dict.pojo.DictionaryField;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.dict.pojo.OptionItem;

public interface DictionaryService {

	List<DictionaryComposite> getAllComposites(String module);
	
	List<DictionaryField> getAllFields(String module);
	
	List<DictionaryOption> getAllOptions();

	public Map<Long, List<OptionItem>> getOptionsMap(Set<Long> fieldIds);

	/**
	 * 从所有配置文件中获得所有label字段的map
	 * @return
	 */
	Map<String, Set<Label>> getAllLabelsMap();

	DictionaryComposite getCurrencyCacheCompositeByFieldId(String module, Long fieldId);

	Map<String, Label> getModuleLabelMap(String module);

	
}
