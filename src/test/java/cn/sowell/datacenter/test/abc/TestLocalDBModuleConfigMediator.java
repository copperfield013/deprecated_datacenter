package cn.sowell.datacenter.test.abc;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.datacenter.entityResolver.config.ModuleConfigureMediator;

@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestLocalDBModuleConfigMediator {
	@Resource
	ModuleConfigureMediator mediator;
	
	@Test
	public void test() {
		mediator.createModule("人口", "p");
	}
	
}
