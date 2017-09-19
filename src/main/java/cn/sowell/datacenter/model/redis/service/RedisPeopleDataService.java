package cn.sowell.datacenter.model.redis.service;

import com.abc.people.People;

public interface RedisPeopleDataService {
	People getPeople(String peopleCode);
	
	
}
