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
import com.abc.mapping.entity.SimpleEntity;
import com.abc.panel.Discoverer;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;

@ContextConfiguration(locations = "classpath*:spring-config/spring-datacenter-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FusionWriteTest {

	private static Logger logger = Logger.getLogger(FusionWriteTest.class);
	protected String mapperName = "people";
//	protected String dictionaryMappingName="default_dm";
	
	@Resource
	SessionFactory sessionFactory;
	@Test
	public void readData() {
//		Session session = sessionFactory.getCurrentSession();
		long startTime = System.currentTimeMillis();
		BizFusionContext context=new BizFusionContext();
		context.setSource(FusionContext.SOURCE_COMMON);
//		context.setDictionaryMappingName(dictionaryMappingName);
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
		entity.putValue("唯一编码", "a7eede54319c4da0bf47fc72488233e8");
		entity.putValue("name", "施连心9");
		entity.putValue("身份证号码", "330105196906281912");
		entity.putValue("性别", "男");
		entity.putValue("出生日期", "1996-09-06");
		entity.putValue("级联省", "浙江省->杭州");
//		entity.putValue("寝室号", "5-607");
//		entity.putValue("申请时间", "2015-04-01");
//		entity.putValue("所在支部", "第二党支部");
//		entity.putValue("志愿时数", 50);
		
		SimpleEntity sentity = new SimpleEntity("证件信息");
		sentity.putValue("证件类型", "1");
		sentity.putValue("证件号码", "223");
		entity.putMultiAttrEntity(sentity);
		sentity = new SimpleEntity("证件信息");
		sentity.putValue("证件类型", "2");
		sentity.putValue("证件号码", "223");
		entity.putMultiAttrEntity(sentity);
		sentity = new SimpleEntity("证件信息");
		sentity.putValue("证件类型", "3");
		sentity.putValue("证件号码", "223");
		entity.putMultiAttrEntity(sentity);
//		
//		entity.putValue("类别", "入党申请人");
		
//		//multi attribute
//		//干部情况
//		SimpleEntity sentity = new SimpleEntity("干部情况");
//		sentity.putValue("任职起始时间", "2015-04-01");
//		sentity.putValue("任职名称", "班长");
//		sentity.putValue("任职级别", "班级");
//		entity.putMultiAttrEntity(sentity);
//		
//		sentity = new SimpleEntity("workExperience");
//		sentity.putValue("companyName", "测试公司");
//		sentity.putValue("workAddress", "浙江杭州西湖区八达关路软件园");
//		entity.putMultiAttrEntity(sentity);
//		
//		Entity relationentity = new Entity("培养联系人");
//		relationentity.putValue("姓名", "刘志华");
//		entity.putRelationEntity("培养联系人","入党联系人", relationentity);
//		
//		relationentity = new Entity("地址");
//		relationentity.putValue("全名", "浙江杭州西湖区八达关路软件园");
//		entity.putRelationEntity("居住地址","居住地址", relationentity);
//		
//		relationentity = new Entity("地址");
//		relationentity.putValue("全名", "浙江杭州西湖区八达关路软件园B座");
//		entity.putRelationEntity("户籍地址","户籍地址", relationentity);
//		
		return entity;
	}

}
