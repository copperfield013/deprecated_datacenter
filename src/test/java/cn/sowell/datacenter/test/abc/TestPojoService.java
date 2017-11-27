package cn.sowell.datacenter.test.abc;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.PojoService;
import cn.sowell.datacenter.model.peopledata.service.impl.PropertyParser;

@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPojoService {
	
	@Resource
	PojoService pojoService;
	
	@Test
	public void testObtain() {
		PeopleData people  = new PeopleData();
		people.setName("name");
		PropertyParser parser = pojoService.createPropertyParser(people);
		System.out.println(parser.get("name"));
	}
}
