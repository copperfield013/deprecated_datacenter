package cn.sowell.datacenter.model.dict.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;
import cn.sowell.datacenter.model.dict.pojo.DictionaryField;

public interface DictionaryDao {

	/**
	 * 根据模块获得所有可用字段组
	 * @param module
	 * @return
	 */
	List<DictionaryComposite> getAllComposites(String module);

	/**
	 * 根据字段组获得其对应的所有可用字段
	 * @param compositeIds
	 * @return
	 */
	Map<Long, List<DictionaryField>> getAllFields(Set<Long> compositeIds);

}
