package cn.sowell.datacenter.test.abc;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCopyTemplate {
	
	@Resource
	TemplateService tService;
	
	
	public void test() {
		Long dtmplId = 42l;
		String targetModuleName = "八二四新";
		Long newId = tService.copyDetailTemplate(dtmplId, targetModuleName);
		System.out.println("newId========" + newId);
	}
	
	@Test
	public void testListCopy() {
		Long ltmplId = 43l;
		String targetModuleName = "八二四新";
		Long newId = tService.copyListTemplate(ltmplId, targetModuleName);
		System.out.println("newId========" + newId);
	}
}
