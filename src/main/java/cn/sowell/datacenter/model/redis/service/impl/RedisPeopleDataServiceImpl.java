package cn.sowell.datacenter.model.redis.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.jedica.JedicaFactory;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.redis.service.RedisPeopleDataService;

import com.abc.people.People;
import com.abc.record.impl.Record;

@Repository
public class RedisPeopleDataServiceImpl implements RedisPeopleDataService{

	//RedisTemplate<String, People> rTemplate;
	
	@Resource
	ABCExecuteService abcService;
	
	@Resource
	JedicaFactory jFactory;

	private Logger logger = Logger.getLogger(RedisPeopleDataService.class);
	
	
	@Override
	public People getPeople(String peopleCode) {
		jFactory.runWithJedica(jedica->{
			PeopleEntry entry;
			try {
				entry = (PeopleEntry) jedica.get(peopleCode);
				Record record = entry.getValue();
				logger.debug(record.toJson());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, JedicaFactory.READ);
		return null;
	}

}
