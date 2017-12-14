package cn.sowell.datacenter.model.basepeople.dao;

import java.util.List;

import cn.sowell.datacenter.model.basepeople.pojo.PeoplePropertyDictionary;

public interface PropertyDictionaryDao {

	List<PeoplePropertyDictionary> queryAllPropertyDictionary(Long appId);

}
