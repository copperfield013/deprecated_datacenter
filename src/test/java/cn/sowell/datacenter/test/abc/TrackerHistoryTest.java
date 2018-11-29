package cn.sowell.datacenter.test.abc;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.application.BizFusionContext;
import com.abc.dto.ErrorInfomation;
import com.abc.extface.dto.RecordHistory;
import com.abc.panel.Discoverer;
import com.abc.panel.PanelFactory;
import com.abc.record.HistoryTracker;
import com.abc.record.Tracker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring-config/spring-datacenter-context.xml")
public class TrackerHistoryTest {

	private static Logger logger = Logger.getLogger(TrackerHistoryTest.class);
	private String mappingName = "人口信息";

	private String peopleCode= "a7eede54319c4da0bfxerc7248883306";

	@Test
	public void trackerHistory() {
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mappingName);
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		List<RecordHistory> rhs=
				discoverer.trackHistory(peopleCode,1,30);
		if(rhs==null){
			logger.warn("没有找到任何历史数据.code:"+peopleCode);
		}
		for(RecordHistory rh:rhs){
			logger.debug(rh.toSimpleString());
		}
		
	}
	
	@Test
	public void discoverTrack() {
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mappingName);
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		Tracker tracker=
				discoverer.discoverTrack(peopleCode);
		
		logger.debug(tracker.getEntity().toJson());
		
		//处理文件
//		Entity entity=tracker.getEntity();
//		BytesInfoVO bytesInfoVO=entity.getBytesInfoVO("大照片");
//
//		BytesInfoVO newOne=discoverer.trackBytesInfo(bytesInfoVO);
//		logger.debug("btye[] name is "+newOne.getName());
//		logger.debug("btye[] size is "+newOne.getBody().length);
//		logger.debug("btye[] suffix is "+newOne.getSuffix());
//		logger.debug("btye[] Size_k  is "+newOne.getSize_k());
		
		List<ErrorInfomation> errorInfomatios = tracker.getErrorInfomations();
		if(errorInfomatios!=null){
			for(ErrorInfomation errorInfomation:errorInfomatios){
				logger.debug(errorInfomation.toString());
			}
		}
		
	}
	
	@Test
	public void trackerHistoryInfo() {
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mappingName);
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		HistoryTracker tracker=
				discoverer.track(peopleCode,new Date());
		if(tracker.getEntity()==null){
			logger.debug("没有找到任何历史数据");
		}else{
			logger.debug(tracker.getEntity().toJson());
		}
	}
	
	
	@Test
	public void trackerById() {
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mappingName);
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		HistoryTracker tracker=
				discoverer.track(new BigInteger("11"));
		if(tracker==null){
			logger.debug("没有找到");
			return;
		}
		List<ErrorInfomation> errorInfomatios = tracker.getErrorInfomations();
		if(errorInfomatios==null){
			logger.debug("没有找到");
			return;
		}
		logger.debug(tracker.getEntity().toJson());
		for(ErrorInfomation errorInfomation:errorInfomatios){
			logger.debug(errorInfomation.toString());
		}
		
	}
	
	
}
