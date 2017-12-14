package cn.sowell.datacenter.model.peopledata.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.application.FusionContext;
import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;
import com.abc.query.criteria.LikeQueryCriteria;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.spring.binder.FieldRefectUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.service.impl.FusionContextFactoryDC;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.peopledata.service.PojoService;

@Service
public class PeopleDataServiceImpl implements PeopleDataService{

	@Resource
	ABCExecuteService abcService;
	FieldRefectUtils<PeopleData> fieldUtils;
	
	@Resource
	PojoService pojoService;
	
	@Resource
	FusionContextFactoryDC fFactory;
	Logger logger = Logger.getLogger(PeopleDataServiceImpl.class);
	
	EntityTransfer eTransfer = new EntityTransfer();
	
	public PeopleDataServiceImpl() {
	}
	
	
	@Override
	public List<PeopleData> query(PeopleDataCriteria criteria, PageInfo pageInfo) {
		FusionContext context = fFactory.getContext(FusionContextFactoryDC.KEY_BASE);
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);
		List<Entity> list = abcService.queryPeopleList(mapperName->{
			ArrayList<Criteria> cs = new ArrayList<Criteria>();
			if(TextUtils.hasText(criteria.getName())){
				LikeQueryCriteria like = criteriaFactory.createLikeQueryCriteria("name", criteria.getName());
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getAddress())){
				LikeQueryCriteria like = criteriaFactory.createLikeQueryCriteria("address", criteria.getAddress());
				cs.add(like);
			}
			if(TextUtils.hasText(criteria.getIdcode())){
				LikeQueryCriteria like = criteriaFactory.createLikeQueryCriteria("idcode", criteria.getIdcode());
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
	public List<Map<String, Object>> queryMap(PeopleDataCriteria criteria, PageInfo pageInfo,
			List<TBasePeopleDictionaryEntity> keys) {
		pageInfo.setPageSize(10);
		long a=System.currentTimeMillis();
		List<PeopleData> list = query(criteria, pageInfo);
		System.out.println("查询耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        PeopleData data = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
        	data = list.get(i);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            for(int j = 0;j < keys.size();j++){
            	String cCnEnglish = keys.get(j).getcCnEnglish();
            	String cnType = keys.get(j).getType();
            	PropertyParser parser = pojoService.createPropertyParser(data);
            	Object value = parser.get(cCnEnglish);            	
            	if(cnType.equals("6")&&value!=null&&value!="")//判断是否为date类型
            		mapValue.put(cCnEnglish, sdf.format(value));
            	else
            		mapValue.put(cCnEnglish, value);            	         		
            }
            listmap.add(mapValue);
        }
		return listmap;
	}



}
