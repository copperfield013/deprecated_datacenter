package cn.sowell.datacenter.model.peopledata.service;

import java.util.List;
import java.util.Map;

import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleItemEntity;

/**
 * 
 * <p>Title: PeopleButtService</p>
 * <p>Description: </p><p>
 * 人口数据对接Service
 * </p>
 * @author Copperfield Zhang
 * @date 2017年11月8日 下午3:29:55
 */
public interface PeopleButtService {

	/**
	 * 
	 * @param field
	 * @return
	 */
	List<TBasePeopleItemEntity> fieldList(String field);

	/**
	 * 
	 * @param peopleCode
	 * @param map
	 */
	void updatePeople(String peopleCode, Map<String, String> map);


      List<TBasePeopleDictionaryEntity> titleSearch (String txt);

}
