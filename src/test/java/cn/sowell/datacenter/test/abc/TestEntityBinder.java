package cn.sowell.datacenter.test.abc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.abc.auth.pojo.UserInfo;
import com.abc.mapping.entity.Entity;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.datacenter.entityResolver.FusionContextConfig;
import cn.sowell.datacenter.entityResolver.FusionContextConfigFactory;
import cn.sowell.datacenter.entityResolver.FusionContextConfigResolver;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.EntityComponent;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.dataserver.model.abc.service.ABCExecuteService;
import cn.sowell.dataserver.model.dict.service.DictionaryService;
import cn.sowell.dataserver.model.modules.service.ModulesService;


@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestEntityBinder {
	@Resource
	FusionContextConfigFactory fFactory;
	
	@Resource
	DictionaryService dictService;
	
	@Resource
	ABCExecuteService abcService;
	
	@Resource
	SessionFactory sFactory;
	
	@Resource
	ModulesService mService;
	
	
	
	static UserIdentifier getUser(){
		UserInfo u = new UserInfo();
		u.setCode("u5");
		ABCUser user = new ABCUser(u);
		return user;
	}
	
	@Test
	public void 残疾人() {
		String module = "Dvat2g4Gn9";
		UserIdentifier user = getUser();
		Map<String, Object> map = new HashMap<>();
		map.put("姓名", "puuuu");
		map.put("街道/镇", "");
		map.put("唯一编码", "18008ab6ac044212878b58f7675cd035");
		String code = mService.mergeEntity(module, map, user);
		
		System.out.println(code);
		ModuleEntityPropertyParser entity = mService.getEntity(module, code, null, user);
		String p = entity.getFormatedProperty("图骗");
		System.out.println(p);
	}
	
	
	@Test
	@Transactional
	public void testSession() {
		Session ss = sFactory.getCurrentSession();
		ss.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				String catalog = connection.getCatalog();
				String sql = "" + catalog;
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.executeUpdate();
			}
		});
	}
	
	
	
	
	public void testABCNodeResolver() {
		FusionContextConfig config = fFactory.getModuleConfig("people");
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
		EntityComponent entity = resolver.createEntity(map);
		ModuleEntityPropertyParser parser = resolver.createParser(entity.getEntity(), UserUtils.getCurrentUser(), null);
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
			String code = abcService.mergeEntity("people", map, UserUtils.getCurrentUser());
			ModuleEntityPropertyParser parser = abcService.getModuleEntityParser("people", code, UserUtils.getCurrentUser());
			System.out.println(parser.getProperty("家庭关系.姓名"));
			System.out.println(parser.getProperty("任职情况[0].任职名称"));
			System.out.println(parser.getProperty("任职情况[1].任职起始时间"));
			System.out.println(parser.getProperty("任职情况[1].任职名称"));
			System.out.println(parser.getProperty("任职情况[0].任职起始时间"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testMergeStudent() {
		Map<String, Object> map = getStudentDataMap();
		try {
			String code = abcService.mergeEntity("student", map, UserUtils.getCurrentUser());
			ModuleEntityPropertyParser parser = abcService.getModuleEntityParser("student", code, UserUtils.getCurrentUser());
			System.out.println(parser.getProperty("家庭关系.姓名"));
			System.out.println(parser.getProperty("workExperience.companyName"));
			System.out.println(parser.getProperty("workExperience.workAddress"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void 证件() {
		Map<String, Object> propMap = new HashMap<>();
		//propMap.put("peoplecode", "6338f82d4d8e47a5832079e02987628b");
		/*propMap.put("证件信息[0].证件类型", "身份证");
		propMap.put("证件信息[0].证件号码", "123456789");
		propMap.put("证件信息[1].证件类型", "市民卡");
		propMap.put("证件信息[1].证件号码", "1111");*/
		propMap.put("人口标签", "户籍人口");
		String peopleCode = abcService.mergeEntity("people", propMap, UserUtils.getCurrentUser());
		
		Entity entity = abcService.getModuleEntity("people", peopleCode, UserUtils.getCurrentUser());
		System.out.println(entity);
		
	}
	
	@Test
	public void 字段查询() {
		dictService.getAllFields("people");
	}
	
	@Test
	public void 唯一编码() {
		ModuleEntityPropertyParser parser = abcService.getModuleEntityParser("people", "6338f82d4d8e47a5832079e02987628b", UserUtils.getCurrentUser());
		System.out.println(parser.getProperty("证件信息[1].证件类型"));
		System.out.println(parser.getProperty("证件信息[1].唯一编码"));
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
		Entity entity = abcService.getModuleEntity("people", "234f3a52528c491a8c129d713bdfda99", UserUtils.getCurrentUser());
		System.out.println(entity.getStringValue("name"));
	}
	
	
	
	private Map<String, Object> getDataMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "白熊");
		map.put("lowIncomeInsureType", "低保人员");
		map.put("家庭关系.姓名", "大白熊");
		map.put("家庭关系.$$label$$", "父母");
		map.put("任职情况[1].任职名称", "杭州设维信息技术有限公司");
		map.put("任职情况[1].任职起始时间", "2015-7-3");
		map.put("任职情况[0].任职名称", "杭州设维");
		map.put("任职情况[0].任职起始时间", "2015-6-3");
		return map;
	}
	
	
	@Test
	public void testCreate() {
		Map<String, Object> map = new HashMap<>();
		map.put("姓名", "sssss");
		map.put("性别", "男性");
		String moduleName = "DSNc5a274h";
		mService.mergeEntity(moduleName, map, UserUtils.getCurrentUser());
	}

}
