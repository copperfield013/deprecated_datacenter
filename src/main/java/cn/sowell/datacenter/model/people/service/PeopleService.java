package cn.sowell.datacenter.model.people.service;

import java.util.List;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.people.pojo.PeopleData;
import cn.sowell.datacenter.model.people.pojo.criteria.PeopleDataCriteria;

public interface PeopleService {

	List<PeopleData> query(PeopleDataCriteria criteria, PageInfo pageInfo);

	PeopleData getPeople(String peopleCode);

}
