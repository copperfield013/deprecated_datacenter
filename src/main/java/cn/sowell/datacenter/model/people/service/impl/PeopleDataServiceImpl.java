package cn.sowell.datacenter.model.people.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.people.pojo.PeopleData;
import cn.sowell.datacenter.model.people.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.people.service.PeopleService;

import com.abc.mapping.entity.Entity;
import com.abc.people.People;
import com.abc.query.querypeople.Criteria;
import com.abc.query.querypeople.criteria.LikeQueryCriteria;

@Service
public class PeopleDataServiceImpl implements PeopleService{

	@Resource
	ABCExecuteService abcService;
	
	
	@Override
	public List<PeopleData> query(PeopleDataCriteria criteria, PageInfo pageInfo) {
		List<People> list = abcService.queryPeopleList(abcNode->{
			ArrayList<Criteria> cs = new ArrayList<Criteria>();
			if(TextUtils.hasText(criteria.getName())){
				LikeQueryCriteria like = new LikeQueryCriteria(abcNode);
				like.setName("name");
				like.setValue("%" + criteria.getName() + "%");
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getAddress())){
				LikeQueryCriteria like = new LikeQueryCriteria(abcNode);
				like.setName("address");
				like.setValue("%" + criteria.getAddress() + "%");
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getIdcode())){
				LikeQueryCriteria like = new LikeQueryCriteria(abcNode);
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


	private PeopleData transfer(People p) {
		if(p != null){
			PeopleData people = new PeopleData();
			Entity entity = p.getEntity("baseinfoImport");
			if(entity != null){
				people.setPeopleCode(entity.getStringValue("peoplecode"));
				people.setName(entity.getStringValue("name"));
				people.setIdcode(entity.getStringValue("idcode"));
				people.setContact(entity.getStringValue("contact1"));
				people.setAddress(entity.getStringValue("address"));
				people.setGender(entity.getStringValue("gender"));
				people.setBirthday(entity.getDateValue("birthday"));
				people.setNativePlace(entity.getStringValue("nativePlace"));
				people.setHouseholdPlace(entity.getStringValue("householdPlace"));
				people.setNation(entity.getStringValue("nation"));
				people.setPoliticalStatus(entity.getStringValue("politicalStatus"));
				people.setMaritalStatus(entity.getStringValue("maritalStatus"));
				people.setReligion(entity.getStringValue("religion"));
				people.setHealthCondition(entity.getStringValue("healthCondition"));
				people.setPeopleType(entity.getStringValue("peopleType"));
				return people;
			}
		}
		return null;
	}


	@Override
	public PeopleData getPeople(String peopleCode) {
		People p = abcService.getPeople(peopleCode);
		return transfer(p);
	}

}
