package cn.sowell.datacenter.test.abc;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
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
public class DatacentercRelationTest {

	private static Logger logger = Logger
			.getLogger(DatacentercRelationTest.class);
	protected String mapperName = "新的测试";
	// protected String dictionaryMappingName="default_dm";

	@Resource
	SessionFactory sessionFactory;

	@Test
	public void readData() {
		// Session session = sessionFactory.getCurrentSession();
		long startTime = System.currentTimeMillis();
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mapperName);
		context.setUserCode("u5");

		context.setSource(FusionContext.SOURCE_COMMON);
		// context.setDictionaryMappingName(dictionaryMappingName);
		Integration integration = PanelFactory.getIntegration();
		Entity entity = createEntity(mapperName);

		logger.debug(entity.toJson());
		String code = integration.integrate(entity, context);
		// context.setMappingName(mapperName);
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		Entity result = discoverer.discover(code);
		logger.debug(code + " : " + result.toJson());
		long endTime = System.currentTimeMillis();// 记录结束时间
		logger.debug((float) (endTime - startTime) / 1000);
	}

	private Entity createEntity(String mappingName) {
		Entity entity = new Entity(mappingName);
		entity.putValue("唯一编码", "5d914e41b5a64a90b17e0ac8de92147l");
		entity.putValue("姓名", "关于8");
		entity.putValue("性别", "女");
		entity.putValue("行政区域", "浙江省->杭州->西湖区->西溪街道");
		Entity relationentity = new Entity("新测试关系");
		relationentity.putValue("名字", "刘志华5");
		entity.putRelationEntity("t关系", "新测试关系", relationentity);

		relationentity = new Entity("新测试关系");
		relationentity.putValue("名字", "刘志华6");
		entity.putRelationEntity("t关系", "新测试关系", relationentity);

		relationentity = new Entity("新测试关系");
		relationentity.putValue("名字", "刘志华4");
		entity.putRelationEntity("t关系", "旧测试关系", relationentity);

		return entity;
	}

	@Test
	public void test() {
	}

}
