package cn.sowell.datacenter.model.peopledata.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.pojo.OptionItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;


public interface PeopleDictionaryService {

	
	List<PeopleCompositeDictionaryItem> getAllInfo(String code);

	/**
	 * 获取所有枚举对象
	 * @return
	 */
	List<DictionaryOption> getAllEnumList();

	Map<Long, List<OptionItem>> getOptionsMap(Set<Long> fieldIds);


}
