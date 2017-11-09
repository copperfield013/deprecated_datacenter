package cn.sowell.datacenter.test.abc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.peopledata.service.PeopleButtService;

@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestABCExecuteService {
	@Resource
	ABCExecuteService abcService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	PeopleButtService buttService;
	
	@Test
	public void testEdit() throws IOException {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("name", "张三");
		//data.put("poeplecode", "a526bd2fa93b4375a5b76506b8651a33");
		data.put("idcode", "350581199108203536");
		data.put("birthday", "1991-08-20");
		data.put("contact1", "15657198552");
		
		//People people = abcService.mergePeople(data);
		//System.out.println(people);
	}
	
	@Test
	public void testUpdate(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "修改后");
		String peopleCode = "765ddd7710d14b3fb807f01077df3c01";
		buttService.updatePeople(peopleCode, map);
	}
	
	
	
	
	
	
	
}
