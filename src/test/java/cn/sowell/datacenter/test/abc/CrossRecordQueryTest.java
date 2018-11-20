package cn.sowell.datacenter.test.abc;

import java.util.Collection;
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
import com.abc.rrc.query.criteria.EntityCriteriaFactory;
import com.abc.rrc.query.entity.impl.EntitySortedPagedQuery;
import com.abc.rrc.query.queryrecord.criteria.Criteria;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
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
		context.setSource(FusionContext.SOURCE_COMMON);
//		context.setDictionaryMappingName("SWZHSQ");
		context.setToEntityRange(FusionContext.ENTITY_CONTENT_RANGE_INTERSECTION);
		
		EntityCriteriaFactory criteriaFactory = new EntityCriteriaFactory(context);
		criteriaFactory.addCriteria("name","施",EntityCriteriaFactory.CommonSymbol.LIKE);
		criteriaFactory.addCriteria("身份证","330105196906281912",EntityCriteriaFactory.CommonSymbol.EQUAL);
		criteriaFactory.addRelationCriteria("关系人口", "介绍的入党人,入党联系人",  createSubCriteria(context,"关系人口"));
		List<Criteria> criterias=criteriaFactory.getCriterias();
		select(criterias, "编辑时间",context);

	}
	
	public Collection<Criteria> createSubCriteria(BizFusionContext pcontext,String relatioName){
		String mapperName=pcontext.getABCNode().getRelation(relatioName).getFullTitle();
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mapperName);
		context.setSource(FusionContext.SOURCE_COMMON);
		context.setToEntityRange(FusionContext.ENTITY_CONTENT_RANGE_INTERSECTION);
		EntityCriteriaFactory criteriaFactory = new EntityCriteriaFactory(context);
		criteriaFactory.addCriteria("低保信息.申请人","你好",EntityCriteriaFactory.CommonSymbol.LIKE);
		criteriaFactory.addCriteria("姓名","刘",EntityCriteriaFactory.CommonSymbol.LIKE);
		List<Criteria> criterias=criteriaFactory.getCriterias();
		return criterias; 
	} 

	public void select(List<Criteria> criterias, String colName,BizFusionContext context) {
		long startTime = System.currentTimeMillis();
		try {
			
			Discoverer discoverer = PanelFactory.getDiscoverer(context);

			EntitySortedPagedQuery sortedPagedQuery = discoverer.discover(
					criterias, colName);
			sortedPagedQuery.setPageSize(5);

			for (int i = 1; i < 3; i++) {
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
