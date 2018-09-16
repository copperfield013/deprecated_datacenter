package cn.sowell.datacenter.test.abc.zhsq;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.application.BizFusionContext;
import com.abc.application.FusionContext;
import com.abc.mapping.entity.Entity;
import com.abc.panel.Discoverer;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;
@ContextConfiguration(locations = "classpath*:spring-config/spring-datacenter-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PeopleTest {
	
	private static Logger logger = Logger.getLogger(PeopleTest.class);
	protected String mapperName = "人口信息";

	
	@Test
	public void readData() {

		long startTime = System.currentTimeMillis();
		BizFusionContext context=new BizFusionContext();
		context.setSource(FusionContext.SOURCE_COMMON);
		context.setUserCode("e10adc3949ba59abbe56e057f28888u5");
		Integration integration=PanelFactory.getIntegration();
		Entity entity=createEntity(mapperName);
		logger.debug(entity.toJson());
		String code=integration.integrate(entity, context);
		Discoverer discoverer=PanelFactory.getDiscoverer(context);
		Entity result=discoverer.discover(code);
		logger.debug(code + " : "+ result.toJson());
		
		long endTime = System.currentTimeMillis();// 记录结束时间
		logger.debug((float) (endTime - startTime) / 1000);
	}

	private Entity createEntity(String mappingName) {
		Entity entity = new Entity(mappingName);
		entity.putValue("唯一编码", "a7eede54319c4da0bfxerc7248883311");
		entity.putValue("姓名", "刘保全1");
		entity.putValue("身份证号码", "6321238209270511");
		entity.putValue("人口类型", "户籍人口");
//		entity.putValue("密码", "aaa45624");
		return entity;
	}

}
