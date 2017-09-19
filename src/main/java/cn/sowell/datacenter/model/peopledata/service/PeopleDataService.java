package cn.sowell.datacenter.model.peopledata.service;

import java.util.Date;
import java.util.List;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;

public interface PeopleDataService {

	List<PeopleData> query(PeopleDataCriteria criteria, PageInfo pageInfo);

	PeopleData getPeople(String peopleCode);

	PeopleData getHistoryPeople(String peopleCode, Date date);

}
