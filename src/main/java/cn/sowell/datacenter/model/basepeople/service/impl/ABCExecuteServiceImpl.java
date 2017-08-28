package cn.sowell.datacenter.model.basepeople.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;

import com.abc.application.PeopleFusion;
import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.entity.SocialEntity;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.record.AttributeFactory;
import com.abc.record.constant.AttributeMatedata;

@Service
public class ABCExecuteServiceImpl implements ABCExecuteService{

	private String mapperName = "baseinfoImport";
	private Logger logger = Logger.getLogger(ABCExecuteService.class);
	@Resource
	private PeopleFusion peopleFusion;
	
	@Resource
	MappingNodeAnalysis analysis;
	
	private ABCNode abcNode;
	private ABCNode getABCNode() throws IOException{
		if(abcNode == null){
			ClassPathResource resouce = new ClassPathResource("mapping/baseinfoImport.xml");
			abcNode = analysis.analysis(resouce.getInputStream());
		}
		return abcNode;
	}
	
	
	@Override
	public SocialEntity createSocialEntity(Map<String, String> data) {
		Assert.notNull(data);
		SocialEntity entity = new SocialEntity(mapperName);
		for (Entry<String, String> entry : data.entrySet()) {
			entity.setValue(entry.getKey(), entry.getValue());
		}
		return entity;
	}
	
	@Override
	public People editPeople(Map<String, String> data) throws IOException {
		SocialEntity socialEntity = createSocialEntity(data);
		People people = createPeople(getABCNode(), socialEntity);
		people = peopleFusion.edit(people);
		logger.debug(people.getJson(getABCNode().getName()));
		return people;
	}
	
	private People createPeople(ABCNode abcNode, SocialEntity socialEntity) {
		People people = new People();
		people.addMapping(abcNode);
		people.addEntity(socialEntity);
		people.getPeopleRecord().putAttribute(AttributeFactory.newInstance(
				AttributeMatedata.ABC_PEOPLECODE,
				"a526bd2fa93b4375a5b76506b8651a37"));
		logger.debug(people.getJson(mapperName));
		return people;
	}
	

}
