package cn.sowell.datacenter.model.peopledata.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.datacenter.model.dict.pojo.DictionaryField;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.pojo.OptionItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;

public interface PeopleDictionaryDao {

	/**
	 * 根据条件查找所有人口条线（不包含字段）
	 */
	List<PeopleCompositeDictionaryItem> queryAllInfo(String code);

	/**
	 * 根据条线id获得对应的所有字段
	 * @param list
	 * @return
	 */
	List<PeopleFieldDictionaryItem> queryAllField(Set<Long> list);

	

	/**
	 * 根据字段id集合获得所有字段映射
	 * @param fieldIds
	 * @return
	 */
	Map<Long, DictionaryField> getFieldMap(Set<Long> fieldIds);

	/**
	 * 
	 * @return
	 */
	List<DictionaryOption> getAllEnumList();

}
