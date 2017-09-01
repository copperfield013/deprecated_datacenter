package cn.sowell.datacenter.model.basepeople.dao;

import java.util.List;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;
import cn.sowell.datacenter.model.people.pojo.criteria.PeopleDataCriteria;

public interface ABCPeopleDao {

	List<BasePeople> queryABC(PeopleDataCriteria criteria, PageInfo pageInfo);

	BasePeople getPeople(String peopleCode);

	
	
}
