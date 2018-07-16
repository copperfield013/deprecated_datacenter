package cn.sowell.datacenter.test.abc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.application.BizFusionContext;
import com.abc.application.FusionContext;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RelationEntity;
import com.abc.panel.Discoverer;
import com.abc.panel.PanelFactory;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;
import com.abc.query.criteria.IncludeQueryCriteria;
import com.abc.query.criteria.QueryCriteria;
import com.abc.query.entity.impl.EntitySortedPagedQuery;

@ContextConfiguration(locations = "classpath*:spring-config/spring-datacenter-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CrossRecordQueryTest {
	private static Logger logger = Logger
			.getLogger(CrossRecordQueryTest.class);

	protected String mapperName = "people";
//	protected String familyDoctorMapper = "familydoctor";
//	protected String dictionaryMappingName = "default_dm";

	@Test
	public void select() {
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mapperName);
		context.setUserCode("u1");
		context.setSource(FusionContext.SOURCE_COMMON);
//		context.setDictionaryMappingName("SWZHSQ");
		context.setToEntityRange(FusionContext.ENTITY_CONTENT_RANGE_INTERSECTION);
		List<Criteria> criterias = new ArrayList<Criteria>();
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);
//		QueryCriteria common = criteriaFactory.createQueryCriteria("性别","男");
//		criterias.add(common);
//		InequalQueryCriteria inequal = criteriaFactory
//				.createInequalQueryCriteria("身份证号码","");
//		criterias.add(inequal);
		
//		QueryCriteria common = criteriaFactory.createLikeQueryCriteria("居住地址","居住地址","全名","杭州");
//		criterias.add(common);
		QueryCriteria common = criteriaFactory.createLikeQueryCriteria("name","施连");
		criterias.add(common);
		common = criteriaFactory.createQueryCriteria("性别","女");
		criterias.add(common);
		
		Set<String> set=new HashSet<String>();
		set.add("流动人口");
		
		IncludeQueryCriteria icommon = criteriaFactory.createIncludeQueryCriteria("人口类型",set);
		criterias.add(icommon);
//		BetweenQueryCriteria bcommon = criteriaFactory.createBetweenQueryCriteria("现状表现.内容","left","right");
//		criterias.add(bcommon);
		common = criteriaFactory.createLikeQueryCriteria("现状表现.内容","2");
		criterias.add(common);
//		common = criteriaFactory.createLikeQueryCriteria("培养联系人","入党联系人","人力资源证书.名称","资源人口");
//		criterias.add(common);
//		common = criteriaFactory.createLikeQueryCriteria("培养联系人","入党联系人,子女","姓名","刘");
//		criterias.add(common);
		select(criterias, "name",context);

	}

	public void select(List<Criteria> criterias, String colName,BizFusionContext context) {
		long startTime = System.currentTimeMillis();
		try {
			
			Discoverer discoverer = PanelFactory.getDiscoverer(context);

			EntitySortedPagedQuery sortedPagedQuery = discoverer.discover(
					criterias, colName);
			sortedPagedQuery.setPageSize(30);

			for (int i = 1; i < 2; i++) {
				logger.debug("第" + i + "页,共" + sortedPagedQuery.getAllCount()
						+ "条数据,每页" + sortedPagedQuery.getPageSize() + "条");
				// abcNode.selectAliasAsTitle();
				for (Entity entity : sortedPagedQuery.visit(i)) {
					// people.addMapping(abcNode);
					for(String name:entity.getRelationNames()){
						List<RelationEntity> rel=entity.getRelations(name);
						for(RelationEntity re:rel){
							logger.debug(re.getFullName()+"-e:"+re.getFullName());
							logger.debug(re.getFullName()+"-e:"+re.getEntity().getFullName());
						}
						
					}
					logger.debug(entity.toJson());
				}
			}
			long endTime = System.currentTimeMillis();// 记录结束时间
			logger.debug((float) (endTime - startTime) / 1000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
