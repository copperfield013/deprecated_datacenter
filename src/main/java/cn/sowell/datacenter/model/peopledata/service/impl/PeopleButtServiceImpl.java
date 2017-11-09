package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;

import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.dao.BasePeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleItemEntity;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.PeopleButtService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;

@Service
public class PeopleButtServiceImpl implements PeopleButtService{

	@Resource
	BasePeopleDao basePeopleDao;
	
	@Resource
	PeopleDataService peopleService;
	
	@Resource
	ABCExecuteService abcService;
	
	@Override
	public List<TBasePeopleItemEntity> fieldList(String field) {
		return basePeopleDao.fieldList(field);
	}

	@Override
	public void updatePeople(String peopleCode, Map<String, String> map) {
		PeopleData people = peopleService.getPeople(peopleCode);
		bindProperties(people, map);
		abcService.savePeople(people);
	}

	private void bindProperties(PeopleData people, Map<String, String> map) {
		WebDataBinder binder = new WebDataBinder(people);
		binder.bind(new MutablePropertyValues(map));
	}
}
