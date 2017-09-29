package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.spring.binder.FieldRefectUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.peopledata.ABCAttribute;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleDataRelation;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;

import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RecordEntity;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;
import com.abc.query.criteria.LikeQueryCriteria;

@Service
public class PeopleDataServiceImpl implements PeopleDataService{

	@Resource
	ABCExecuteService abcService;
	FieldRefectUtils<PeopleData> fieldUtils;
	
	Logger logger = Logger.getLogger(PeopleDataServiceImpl.class);
	
	public PeopleDataServiceImpl() {
		fieldUtils = new FieldRefectUtils<PeopleData>(PeopleData.class, (property)->{
			ABCAttribute attribute = property.getFieldAnno(ABCAttribute.class);
			if(attribute != null && TextUtils.hasText(attribute.value())){
				return attribute.value();
			}
			return property.getFieldName();
		});
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
		if(entity != null){
			PeopleData people = new PeopleData();
			fieldUtils.iterateField((propName, prop)->{
				ABCAttribute anno = prop.getFieldAnno(ABCAttribute.class);
				if(anno != null && anno.ignored()){
					return;
				}
				if(PeopleDataRelation.class.isAssignableFrom(prop.getFieldType())){
					List<RecordEntity> recordEntities = entity.getRelations(anno.entityName());
					if(recordEntities != null && recordEntities.size() > 0){
						PeopleDataRelation relation = new PeopleDataRelation();
						relation.setName(recordEntities.get(0).getEntity().getStringValue(propName));
						try {
							prop.setValue(people, relation);
						} catch (Exception e) {
							logger.error("", e);
						}
					}
				}else if(Date.class.isAssignableFrom(prop.getFieldType())){
					try {
						prop.setValue(people, entity.getDateValue(propName));
					} catch (Exception e) {
						logger.error("", e);
					}
				}else{
					try {
						prop.setValue(people, entity.getStringValue(propName));
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});
			/*
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
			people.setLowIncomeInsuredCode(entity.getStringValue("lowIncomeInsuredCode"));
			people.setHandicappedCode(entity.getStringValue("handicappedCode"));
			people.setLowIncomeInsuredType(entity.getStringValue("lowIncomeInsuredType"));*/
			
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
			return getPeople(peopleCode);
		}else{
			Entity people = abcService.getHistoryPeople(peopleCode, date);
			return transfer(people);
		}
	}
}
