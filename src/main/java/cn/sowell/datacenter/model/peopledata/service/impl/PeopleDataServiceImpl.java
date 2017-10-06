package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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
				like.setValue("%" + criteria.getName() + "%");
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getAddress())){
				LikeQueryCriteria like = CriteriaFactory.createLikeQueryCriteria(mapperName);
				like.setName("address");
				like.setValue("%" + criteria.getAddress() + "%");
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getIdcode())){
				LikeQueryCriteria like = CriteriaFactory.createLikeQueryCriteria(mapperName);
				like.setName("idcode");
				like.setValue("%" + criteria.getIdcode() + "%");
				cs.add(like);
			}
			return cs;
		}, pageInfo);
		List<PeopleData> result = new ArrayList<PeopleData>();
		list.forEach(p->result.add(transfer(p)));
		return result;
	}


	private PeopleData transfer(Entity entity) {
		PeopleData people = new PeopleData();
		eTransfer.bind(entity, people);
		return people;
	}


	@Override
	public PeopleData getPeople(String peopleCode) {
		Entity p = abcService.getPeople(peopleCode);
		return transfer(p);
	}

	@Override
	public PeopleData getHistoryPeople(String peopleCode, Date date) {
		if(date == null){
			return getPeople(peopleCode);
		}else{
			Entity people = abcService.getHistoryPeople(peopleCode, date);
			return transfer(people);
		}
	}
}
