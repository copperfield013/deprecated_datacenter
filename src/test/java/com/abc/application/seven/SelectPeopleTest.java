package com.abc.application.seven;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.InequalQueryCriteria;
import com.abc.query.criteria.LikeQueryCriteria;
import com.abc.query.criteria.QueryCriteria;
import com.abc.query.people.impl.PeopleSortedPagedQuery;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SelectPeopleTest {
	private static Logger logger = Logger.getLogger(SelectPeopleTest.class);

	@Resource(name = "MappingNodeAnalysis")
	MappingNodeAnalysis analysis;

	protected String mapperName = "baseinfoImport";
	protected String familyDoctorMapper = "familydoctor";
	protected String mappingfilepath = getClass().getResource("/").getFile()
			+ "../classes/mapping/baseinfoImport.xml";

	@Test
	public void selectPeople() {
		ABCNode abcNode;
		try {
			abcNode = analysis.analysis(mappingfilepath);
			List<Criteria> criterias = new ArrayList<Criteria>();
			LikeQueryCriteria like = new LikeQueryCriteria(abcNode);
			like.setName("姓名");
			like.setValue("张");
			criterias.add(like);
			QueryCriteria common = new QueryCriteria(abcNode);
			common.setName("性别");
			common.setValue("男");
			criterias.add(common);
			InequalQueryCriteria inequal = new InequalQueryCriteria(abcNode);
			inequal.setName("身份证号码");
			inequal.setValue("");
			criterias.add(inequal);
			select(criterias,"出生日期");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void selectPeopleNonCriteria() {
		select(null,null);
	}
	
	@Test
	public void selectPeopleByPeopleCode() {
		PeopleSortedPagedQuery sortedPagedQuery = new PeopleSortedPagedQuery(null,
				null, null);
		try {
			ABCNode abcNode = analysis.analysis(mappingfilepath);
			People people=sortedPagedQuery.visit("012946554702469ea2d5446f2c995b0c");
			people.addMapping(abcNode);
			logger.debug(people.getJson(abcNode.getTitle()));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void select(List<Criteria> criterias,String colName) {
		ABCNode abcNode;
		long startTime = System.currentTimeMillis();
		try {
			abcNode = analysis.analysis(mappingfilepath);
			PeopleSortedPagedQuery sortedPagedQuery = new PeopleSortedPagedQuery(criterias,
					abcNode, colName);
			sortedPagedQuery.setPageSize(30);
			
			for (int i = 1; i < 4; i++) {
				logger.debug("第" + i + "页,共" + sortedPagedQuery.getAllCount()
						+ "条数据,每页" + sortedPagedQuery.getPageSize() + "条");
//				abcNode.selectAliasAsTitle();
				for (People people : sortedPagedQuery.visit(i)) {
//					people.addMapping(abcNode);
					logger.debug(people.getJson(abcNode.getTitle()));
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
