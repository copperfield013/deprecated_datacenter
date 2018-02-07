package cn.sowell.datacenter.model.peopledata.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.spring.binder.FieldRefectUtils;
import cn.sowell.copframe.spring.propTranslator.PropertyParser;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.people.ExportDataPageInfo;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.EntityPagingQueryProxy;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.service.impl.FusionContextFactoryDC;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.peopledata.service.PojoService;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;

import com.abc.application.FusionContext;
import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;
import com.abc.query.criteria.LikeQueryCriteria;

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
	
	
	@Override
	public List<PeopleData> query(Set<NormalCriteria> nCriterias,
			PageInfo pageInfo) {
		List<Criteria> cs = toCriterias(nCriterias);
		List<Entity> list = abcService.queryPeopleList(cs, pageInfo);
		List<PeopleData> result = new ArrayList<PeopleData>();
		list.forEach(p->result.add(transfer(p)));
		return result;
	}
	
	private List<Criteria> toCriterias(Set<NormalCriteria> nCriterias){
		FusionContext context = fFactory.getContext(FusionContextFactoryDC.KEY_BASE);
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);
		ArrayList<Criteria> cs = new ArrayList<Criteria>();
		nCriterias.forEach(nCriteria->{
			TemplateListCriteria criteria = nCriteria.getCriteria();
			if(TextUtils.hasText(nCriteria.getValue())){
				String comparator = criteria.getComparator();
				if("s1".equals(comparator)){
					cs.add(criteriaFactory.createLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("s2".equals(comparator)){
					cs.add(criteriaFactory.createLeftLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("s3".equals(comparator)){
					cs.add(criteriaFactory.createRightLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("s4".equals(comparator)){
					cs.add(criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}
			}
		});
		return cs;
	}
	
	@Override
	public PeopleDataPagingIterator queryIterator(Set<NormalCriteria> nCriterias,
			ExportDataPageInfo ePageInfo) {
		PageInfo pageInfo = ePageInfo.getPageInfo();
		List<Criteria> cs = toCriterias(nCriterias);
		EntityPagingQueryProxy proxy = abcService.getQueryProxy(cs, ePageInfo);
		Function<Integer, Set<PeopleData>> dataGetter = new Function<Integer, Set<PeopleData>>() {

			@Override
			public Set<PeopleData> apply(Integer pageNo) {
				Set<Entity> entities = proxy.load(pageNo);
				return CollectionUtils.toSet(entities, entity->transfer(entity));
			}
		};
		int dataCount = pageInfo.getPageSize();
		int startPageNo = pageInfo.getPageNo();
		int totalCount = proxy.getTotalCount();
		int ignoreCount = 0;
		if("all".equals(ePageInfo.getScope())){
			dataCount = proxy.getTotalCount();
			startPageNo = 1;
			if(ePageInfo.getRangeStart() != null){
				ignoreCount = ePageInfo.getRangeStart() - 1;
				if(ePageInfo.getRangeEnd() != null){
					dataCount = ePageInfo.getRangeEnd() - ePageInfo.getRangeStart() + 1;
				}else{
					dataCount -= ePageInfo.getRangeStart() - 1;
				}
			}else if(ePageInfo.getRangeEnd() != null){
				dataCount = ePageInfo.getRangeEnd();
			}
		}
		return new PeopleDataPagingIterator(totalCount, dataCount, ignoreCount, startPageNo, dataGetter);
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
