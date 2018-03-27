package cn.sowell.datacenter.test.abc;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.business.MappingContainer;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.node.ABCNode;
import com.abc.mapping.node.AttributeNode;

import cn.sowell.datacenter.model.abc.resolver.EntityPropertyParser;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfig;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfigResolver;
import cn.sowell.datacenter.model.abc.resolver.FusionContextFactoryDC;
import cn.sowell.datacenter.model.abc.service.ABCExecuteService;
import cn.sowell.datacenter.model.dict.service.DictionaryService;


@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestEntityBinder {
	@Resource
	FusionContextFactoryDC fFactory;
	
	@Resource
	DictionaryService dictService;
	
	@Resource
	ABCExecuteService abcService;
	
	public void testNode() {
		FusionContextConfig config = fFactory.getConfig(FusionContextFactoryDC.KEY_BASE);
		ABCNode rootNode = MappingContainer.getABCNode(config.getMappingName());
		AttributeNode node = rootNode.getAttribute("birthday");
		System.out.println("getTitle--" + node.getTitle());
		System.out.println("getParentcode--" + node.getParentcode());
		System.out.println("getOpsTypeName--" + node.getOpsTypeName());
		System.out.println("getAbcattr--" + node.getAbcattr());
		System.out.println("getAbcattrName--" + node.getAbcattrName());
		System.out.println("getFullAbcattr--" + node.getFullAbcattr());
		System.out.println("getDatatype--" + node.getDatatype());
		System.out.println("getAttrsubtype--" + node.getAttrsubtype());
		
		
		/*getTitle--birthday
		getParentcode--ABCE010
		getOpsTypeName--WRITE
		getAbcattr--SW0068
		getAbcattrName--出生日期
		getFullAbcattr--ABCE010_SW0068
		getDatatype--DATE
		getAttrsubtype--null*/
	}
	
	
	public void testABCNodeResolver() {
		FusionContextConfig config = fFactory.getConfig(FusionContextFactoryDC.KEY_BASE);
		try {
			testResolver(config);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void testResolver(FusionContextConfig config) {
		Map<String, Object> map = getDataMap();
		FusionContextConfigResolver resolver = config.getConfigResolver();
		Entity entity = resolver.createEntity(map);
		EntityPropertyParser parser = resolver.createParser(entity);
		System.out.println(parser.getProperty("name"));
		System.out.println(parser.getProperty("code"));
		System.out.println(parser.getProperty("lowIncomeInsureType"));
		System.out.println(parser.getProperty("家庭关系[0].姓名"));
		System.out.println(parser.getProperty("家庭关系[1].姓名"));
		System.out.println(parser.getProperty("workExperience[0].companyName"));
		System.out.println(parser.getProperty("workExperience[0].salary"));
		System.out.println(parser.getProperty("workExperience[1].companyName"));
		System.out.println(parser.getProperty("workExperience[1].salary"));
	}
	
	
	@Test
	public void testMerge() {
		Map<String, Object> map = getDataMap();
		try {
			String code = abcService.mergeEntity("people", map);
			EntityPropertyParser parser = abcService.getModuleEntityParser("people", code);
			System.out.println(parser.getProperty("家庭关系.姓名"));
			System.out.println(parser.getProperty("workExperience.companyName"));
			System.out.println(parser.getProperty("workExperience.workAddress"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testMergeStudent() {
		Map<String, Object> map = getStudentDataMap();
		try {
			String code = abcService.mergeEntity("student", map);
			EntityPropertyParser parser = abcService.getModuleEntityParser("student", code);
			System.out.println(parser.getProperty("家庭关系.姓名"));
			System.out.println(parser.getProperty("workExperience.companyName"));
			System.out.println(parser.getProperty("workExperience.workAddress"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, Object> getStudentDataMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "白熊学生");
		map.put("培养联系人.姓名", "白熊老师");
		map.put("培养联系人.$$label$$", "入党联系人");
		map.put("干部情况.任职名称", "杭州设维信息技术有限公司");
		map.put("workExperience.workAddress", "东部软件园");
		return map;
	}


	public void testGet() {
		Entity entity = abcService.getModuleEntity("people", "234f3a52528c491a8c129d713bdfda99");
		System.out.println(entity.getStringValue("name"));
	}
	
	
	
	private Map<String, Object> getDataMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "白熊");
		map.put("lowIncomeInsureType", "低保人员");
		map.put("家庭关系.姓名", "大白熊");
		map.put("家庭关系.$$label$$", "父母");
		map.put("workExperience.companyName", "杭州设维信息技术有限公司");
		map.put("workExperience.workAddress", "东部软件园");
		return map;
	}

}
