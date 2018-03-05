package cn.sowell.datacenter.model.tmpl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import com.abc.application.FusionContext;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.basepeople.service.impl.FusionContextFactoryDC;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.dao.ListTemplateDao;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Service
public class ListTemplateServiceImpl implements ListTemplateService{
	@Resource
	ListTemplateDao lDao;

	@Resource
	NormalOperateDao nDao;
	
	@Resource
	SystemAdminService adminService;
	
	@Resource
	TemplateService tService;
	
	@Override
	public List<TemplateListTempalte> queryLtmplList(String module, UserIdentifier user) {
		return lDao.queryLtmplList(module, user.getId(), null);
	}
	
	
	@Resource
	PeopleDataService peopleService;

	@Resource
	FusionContextFactoryDC fFactory;
	
	@Override
	public List<PeopleData> queryPeopleList(Set<NormalCriteria> criterias, PageInfo pageInfo) {
		return peopleService.query(criterias, pageInfo);
	}
	
	
	@Override
	public Map<Long, NormalCriteria> getCriteriasFromRequest(
			MutablePropertyValues pvs, Map<Long, TemplateListCriteria> criteriaMap) {
		 Map<Long, NormalCriteria> map = new HashMap<Long, NormalCriteria>();
		 pvs.getPropertyValueList().forEach(pv->{
			 Long criteriaId = FormatUtils.toLong(pv.getName());
			 if(criteriaId != null){
				 TemplateListCriteria criteria = criteriaMap.get(criteriaId);
				 if(criteria != null){
					 NormalCriteria ncriteria = new NormalCriteria(criteria);
					 //TODO: 需要将fieldKey转换成attributeName
					 ncriteria.setAttributeName(criteria.getFieldKey());
					 ncriteria.setValue(FormatUtils.toString(pv.getValue()));
					 map.put(criteriaId, ncriteria);
				 }
			 }
		 });
		 criteriaMap.forEach((criteriaId, criteria)->{
			 if(TextUtils.hasText(criteria.getDefaultValue()) && !map.containsKey(criteriaId)){
				 NormalCriteria nCriteria = new NormalCriteria(criteria);
				 //TODO: 需要将fieldKey转换成attributeName
				 nCriteria.setAttributeName(criteria.getFieldKey());
				 nCriteria.setValue(criteria.getDefaultValue());
				 map.put(criteriaId, nCriteria);
			 }
		 });;
		return map;
	}
	
	@Override
	public ListTemplateParameter exractTemplateParameter(Long tmplId,
			String module,
			HttpServletRequest request) {
		TemplateListTempalte ltmpl = null;
		UserIdentifier user = UserUtils.getCurrentUser();
		if(tmplId == null){
			ltmpl = tService.getDefaultListTemplate(user, module);
		}else{
			ltmpl = tService.getListTemplate(tmplId);
		}
		Map<Long, NormalCriteria> vCriteriaMap = getCriteriasFromRequest(
				new ServletRequestParameterPropertyValues(request, "criteria", "_"), 
				CollectionUtils.toMap(ltmpl.getCriterias(), c->c.getId())); 
		ListTemplateParameter param = new ListTemplateParameter();
		param.setListTemplate(ltmpl);
		param.setNormalCriteriaMap(vCriteriaMap);
		param.setUser(user);
		return param;
	}
	
	@Override
	public List<Criteria> toCriterias(Set<NormalCriteria> nCriterias){
		FusionContext context = fFactory.getContext(FusionContextFactoryDC.KEY_BASE);
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);
		ArrayList<Criteria> cs = new ArrayList<Criteria>();
		nCriterias.forEach(nCriteria->{
			TemplateListCriteria criteria = nCriteria.getCriteria();
			if(TextUtils.hasText(nCriteria.getValue())){
				String comparator = criteria.getComparator();
				if("t1".equals(comparator)){
					cs.add(criteriaFactory.createLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("t2".equals(comparator)){
					cs.add(criteriaFactory.createLeftLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("t3".equals(comparator)){
					cs.add(criteriaFactory.createRightLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("t4".equals(comparator)){
					cs.add(criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("s1".equals(comparator)){
					cs.add(criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}
			}
		});
		return cs;
	}
	
}
