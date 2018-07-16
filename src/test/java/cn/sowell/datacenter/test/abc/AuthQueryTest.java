package cn.sowell.datacenter.test.abc;

import java.util.ArrayList;
import java.util.List;

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
import com.abc.query.criteria.QueryCriteria;
import com.abc.query.entity.impl.EntitySortedPagedQuery;

@ContextConfiguration(locations = "classpath*:spring-config/spring-datacenter-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthQueryTest {
	private static Logger logger = Logger
			.getLogger(AuthQueryTest.class);

	protected String mapperName = "测试实体";
//	protected String familyDoctorMapper = "familydoctor";
//	protected String dictionaryMappingName = "default_dm";

	@Test
	public void selectNomal() {
		select("u1");
	}
	@Test
	public void selectNonUser() {
		select("u-1");
	}

	protected void select(String userCode) {
		
		BizFusionContext context = new BizFusionContext();
		context.setUserCode(userCode);
		context.setMappingName(mapperName);
		
		context.setSource(FusionContext.SOURCE_COMMON);
//		context.setDictionaryMappingName("SWZHSQ");
		context.setToEntityRange(FusionContext.ENTITY_CONTENT_RANGE_INTERSECTION);
		List<Criteria> criterias = new ArrayList<Criteria>();
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);

		QueryCriteria common = criteriaFactory.createLikeQueryCriteria("名称","测试");
		criterias.add(common);
		
		select(criterias, "名称",context);
	}
	
	@Test
	public void selectWithCode() {
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mapperName);
		context.setUserCode("u2");
		context.setSource(FusionContext.SOURCE_COMMON);
//		context.setDictionaryMappingName("SWZHSQ");
		context.setToEntityRange(FusionContext.ENTITY_CONTENT_RANGE_INTERSECTION);
		List<Criteria> criterias = new ArrayList<Criteria>();
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);
		QueryCriteria common = criteriaFactory.createQueryCriteria("唯一编码","t1");
		criterias.add(common);
		
		select(criterias, "名称",context);

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
