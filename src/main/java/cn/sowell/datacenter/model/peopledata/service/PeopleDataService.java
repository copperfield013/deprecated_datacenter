package cn.sowell.datacenter.model.peopledata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;

public interface PeopleDataService {

	List<PeopleData> query(PeopleDataCriteria criteria, PageInfo pageInfo);
	
	List<PeopleData> query(Set<NormalCriteria> criterias, PageInfo pageInfo);

	PeopleData getPeople(String peopleCode);

	PeopleData getHistoryPeople(String peopleCode, Date date);

	List<Map<String, Object>> queryMap(PeopleDataCriteria criteria, PageInfo pageInfo, List<TBasePeopleDictionaryEntity> keys);



}
