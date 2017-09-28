package com.abc.application.seven;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.application.ApplicationInfo;
import com.abc.panel.PanelFactory;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RemovePeopleTest {
	private static Logger logger = Logger.getLogger(RemovePeopleTest.class);

	@Test
	public void removePeople() {
		ApplicationInfo appInfo=new ApplicationInfo("a526bd2fa93b4375a5b76506b8651a33", null, "test");
		PanelFactory.getIntegration().remove(appInfo);
		
	}
}
