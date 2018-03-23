package cn.sowell.datacenter.test.abc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfigResolver;
import cn.sowell.datacenter.model.abc.resolver.impl.ABCNodeFusionContextConfigResolver;
import cn.sowell.datacenter.model.abc.service.ABCExecuteService;
import cn.sowell.datacenter.model.abc.service.impl.FusionContextConfig;
import cn.sowell.datacenter.model.abc.service.impl.FusionContextFactoryDC;
import cn.sowell.datacenter.model.dict.service.DictionaryService;
import cn.sowell.datacenter.model.modules.EntityPropertyParser;
import cn.sowell.datacenter.model.modules.FieldParserDescription;
import cn.sowell.datacenter.model.peopledata.pojo.FamilyInfo;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.WorkExperience;
import cn.sowell.datacenter.model.peopledata.service.impl.EntityTransfer;


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
		FusionContextConfigResolver resolver = new ABCNodeFusionContextConfigResolver(config);
		try {
			testResolver(resolver);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void testResolver(FusionContextConfigResolver resolver) {
		Map<String, Object> map = getDataMap();
		Entity entity = resolver.createEntity(map);
		EntityPropertyParser parser = new EntityPropertyParser(entity, CollectionUtils.toSet(dictService.getAllFields("people"), item->new FieldParserDescription(item)));
		System.out.println(parser.getProperty("name"));
		System.out.println(parser.getProperty("code"));
		System.out.println(parser.getProperty("家庭关系[0].姓名"));
		System.out.println(parser.getProperty("家庭关系[1].姓名"));
	}
	
	@Test
	public void testMerge() {
		Map<String, Object> map = getDataMap();
		try {
			String code = abcService.mergeEntity("people", map);
			EntityPropertyParser parser = abcService.getModuleEntityParser("people", code);
			System.out.println(parser.getProperty("家庭关系.姓名"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testGet() {
		Entity entity = abcService.getModuleEntity("people", "234f3a52528c491a8c129d713bdfda99");
		System.out.println(entity.getStringValue("name"));
	}
	
	
	
	private Map<String, Object> getDataMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "白熊");
		map.put("家庭关系.姓名", "大白熊");
		map.put("家庭关系.$$label$$", "父母");
		return map;
	}

	
	EntityTransfer transfer = new EntityTransfer();
	
	public void test() {
		PeopleData data = new PeopleData();
		data.setName("张荣波");
		
		FamilyInfo family = new FamilyInfo();
		family.setFamilyAddress("福建省");
		data.setFamilyInfo(family);
		
		List<WorkExperience> workExperiences = new ArrayList<>();
		WorkExperience work = new WorkExperience();
		work.setCompanyName("杭州设维");
		workExperiences.add(work);
		work = new WorkExperience();
		work.setCompanyName("未知");
		workExperiences.add(work);
		data.setWorkExperiences(workExperiences);
		
		Entity target = new Entity("baseinfoImport");
		transfer.bind(data, target);
		System.out.println(target.getStringValue("name"));
		
		/*
		List<RecordEntity> fRecord = target.getRecords("家庭信息");
		System.out.println(fRecord);
		System.out.println(fRecord.size());
		System.out.println(fRecord.get(0).getEntity().getStringValue("家庭地址"));
		
		
		List<RecordEntity> wRecord = target.getRecords("workExperience");
		wRecord.forEach(wr->{
			System.out.println(wr.getEntity().getStringValue("companyName"));
		});*/
		
	}
}
