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

import com.abc.people.People;

@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestABCExecuteService {
	@Resource
	ABCExecuteService abcService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	@Test
	public void testEdit() throws IOException {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("name", "张三");
		data.put("idcode", "350581199108203536");
		//data.put("birthday", "1991-08-20");
		data.put("contact1", "15657198552");
		
		People people = abcService.editPeople(data);
		System.out.println(people);
	}
}
