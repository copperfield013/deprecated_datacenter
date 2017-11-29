package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;
import com.abc.query.criteria.LikeQueryCriteria;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.spring.binder.FieldRefectUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;

@Service
public class PeopleDataServiceImpl implements PeopleDataService{

	@Resource
	ABCExecuteService abcService;
	FieldRefectUtils<PeopleData> fieldUtils;
	
	Logger logger = Logger.getLogger(PeopleDataServiceImpl.class);
	
	EntityTransfer eTransfer = new EntityTransfer();
	
	public PeopleDataServiceImpl() {
	}
	
	
	@Override
	public List<PeopleData> query(PeopleDataCriteria criteria, PageInfo pageInfo) {
		List<Entity> list = abcService.queryPeopleList(mapperName->{
			ArrayList<Criteria> cs = new ArrayList<Criteria>();
			if(TextUtils.hasText(criteria.getName())){
				LikeQueryCriteria like = CriteriaFactory.createLikeQueryCriteria(mapperName);
				like.setName("name");
				like.setValue(criteria.getName());
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getAddress())){
				LikeQueryCriteria like = CriteriaFactory.createLikeQueryCriteria(mapperName);
				like.setName("address");
				like.setValue(criteria.getAddress());
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getIdcode())){
				LikeQueryCriteria like = CriteriaFactory.createLikeQueryCriteria(mapperName);
				like.setName("idcode");
				like.setValue(criteria.getIdcode());
				cs.add(like);
			}
			return cs;
		}, pageInfo);
		List<PeopleData> result = new ArrayList<PeopleData>();
		list.forEach(p->result.add(transfer(p)));
		return result;
	}


	private PeopleData transfer(Entity entity) {
		if(entity != null){
			PeopleData people = new PeopleData();
			eTransfer.bind(entity, people);
			return people;
		}
		return null;
	}


	@Override
	public PeopleData getPeople(String peopleCode) {
		Entity p = abcService.getPeople(peopleCode);

		return transfer(p);
	}

	@Override
	public PeopleData getHistoryPeople(String peopleCode, Date date) {
		if(date == null){
			date = new Date();
		}
		List<ErrorInfomation> errors = new ArrayList<ErrorInfomation>();
		Entity people = abcService.getHistoryPeople(peopleCode, date, errors);
		PeopleData peopleData = transfer(people);
		if(peopleData != null){
			peopleData.setErrors(errors);
		}
		return peopleData;
	}


	@Override
	public List<Map<String, Object>> queryMap(PeopleDataCriteria criteria, PageInfo pageInfo) {
		List<PeopleData> list = query(criteria, pageInfo);
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "text");
        listmap.add(map);
        PeopleData data = null;
        for (int i = 0; i < list.size(); i++) {
        	data = list.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("name", data.getName());
            mapValue.put("idCode", data.getIdcode());
            mapValue.put("gender", data.getGender());
            mapValue.put("address", data.getAddress());
            listmap.add(mapValue);
        }
		return listmap;
	}
}
