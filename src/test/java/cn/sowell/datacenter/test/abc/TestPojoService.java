package cn.sowell.datacenter.test.abc;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.datacenter.model.peopledata.service.PojoService;

@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPojoService {
	
	@Resource
	SessionFactory sFactory;
	
	@Resource
	PojoService pojoService;
	
}
