package cn.sowell.datacenter.model.redis.service;

import com.abc.record.impl.Record;

public interface RedisPeopleDataService {
	Record getRecord(String peopleCode);
	
	
}
