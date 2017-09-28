package com.abc.application.seven;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.mapping.entity.Entity;
import com.abc.panel.Discoverer;
import com.abc.panel.PanelFactory;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;
import com.abc.query.criteria.InequalQueryCriteria;
import com.abc.query.criteria.LikeQueryCriteria;
import com.abc.query.criteria.QueryCriteria;
import com.abc.query.entity.impl.EntitySortedPagedQuery;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SelectPeopleCrossTest {
	private static Logger logger = Logger.getLogger(SelectPeopleCrossTest.class);

	protected String mapperName = "example";
	protected String familyDoctorMapper = "familydoctor";
	@Test
	public void selectPeople() {

			List<Criteria> criterias = new ArrayList<Criteria>();
			LikeQueryCriteria like = CriteriaFactory.createLikeQueryCriteria(mapperName);
			like.setName("姓名");
			like.setValue("张");
			criterias.add(like);
			QueryCriteria common = CriteriaFactory.createQueryCriteria(mapperName);
			common.setName("性别");
			common.setValue("男");
			criterias.add(common);
			InequalQueryCriteria inequal = CriteriaFactory.createInequalQueryCriteria(mapperName);
			inequal.setName("身份证号码");
			inequal.setValue("");
			criterias.add(inequal);
			
//			like = new LikeQueryCriteria(abcNode);
//			like.setName("school name");
//			like.setValue("北");
//			criterias.add(like);
			
			select(criterias,"出生日期");
		
	}

	public void select(List<Criteria> criterias,String colName) {
		long startTime = System.currentTimeMillis();
		try {

			Discoverer discoverer=PanelFactory.getDiscoverer(mapperName);
			
			EntitySortedPagedQuery sortedPagedQuery = discoverer.discover(criterias, colName);
			sortedPagedQuery.setPageSize(30);
			
			for (int i = 1; i < 4; i++) {
				logger.debug("第" + i + "页,共" + sortedPagedQuery.getAllCount()
						+ "条数据,每页" + sortedPagedQuery.getPageSize() + "条");
//				abcNode.selectAliasAsTitle();
				for (Entity entity : sortedPagedQuery.visit(i)) {
//					people.addMapping(abcNode);
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
