package cn.sowell.datacenter.model.peopledata.dao;

import java.util.List;
import java.util.Set;

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


}
