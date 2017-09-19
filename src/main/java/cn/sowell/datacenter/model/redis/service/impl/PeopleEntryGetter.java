package cn.sowell.datacenter.model.redis.service.impl;

import javax.annotation.Resource;

import com.abc.people.People;

import cn.sowell.copframe.jedica.EntryGetter;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;

public class PeopleEntryGetter implements EntryGetter<PeopleEntry>{

	@Resource
	ABCExecuteService abcService;

	@Override
	public PeopleEntry get(String key) {
		People people = abcService.getPeople(key);
		PeopleEntry entry = new PeopleEntry(key, people.getPeopleRecord());
		return entry;
	}

	public void setAbcService(ABCExecuteService abcService) {
		this.abcService = abcService;
	}

}
