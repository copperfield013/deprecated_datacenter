package cn.sowell.datacenter.model.redis.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.jedica.JedicaFactory;
import cn.sowell.copframe.jedica.RedisEntry;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.redis.service.RedisPeopleDataService;

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
	public Record getRecord(String peopleCode) {
		return (Record) jFactory.returnByJedica(jedica->{
			try {
				RedisEntry<?> entry = jedica.get(peopleCode);
				if(entry != null){
					return entry.getValue();
				}
			} catch (Exception e) {
			}
			return null;
		}, JedicaFactory.READ);
		
	}

}
