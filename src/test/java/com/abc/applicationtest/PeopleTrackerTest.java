package com.abc.applicationtest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.dto.ErrorInfomation;
import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.people.PeopleTracker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/spring-core.xml")
public class PeopleTrackerTest {
	@Resource(name="MappingNodeAnalysis")
	MappingNodeAnalysis analysis;
	private static Logger logger = Logger.getLogger(PeopleTrackerTest.class);
	private String mapperName = "baseinfoImport";
	private String mappingfilepath = getClass().getResource("/").getFile()
			+ "../classes/mapping/baseinfoImport.xml";
	private String peopleCode= "1aa01951dfa14280955e3517be76c7bb";

	@Test
	public void trackerNow() {
		PeopleTracker peopleTracker=new PeopleTracker(peopleCode,new Date());
		tracker(peopleTracker);
		
	}

	protected void tracker(PeopleTracker peopleTracker) {
		ABCNode abcNode=null;
		try {
			abcNode = analysis.analysis(mappingfilepath);
			People people=peopleTracker.getPeople();
			if(people!=null){
				people.addMapping(abcNode);
				logger.debug(people.getJson(mapperName));
				List<ErrorInfomation> elist=peopleTracker.getErrorInfomations(abcNode);
				if(elist!=null){
					for(ErrorInfomation si:elist){
						logger.debug(si);
					}
				}
			}else{
				logger.debug("未查到任何数据");
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void trackerSetTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date;
		try {
			date = dateFormat.parse("2017-09-11 11:40:00");
			PeopleTracker peopleTracker=new PeopleTracker(peopleCode,date);
			tracker(peopleTracker);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
