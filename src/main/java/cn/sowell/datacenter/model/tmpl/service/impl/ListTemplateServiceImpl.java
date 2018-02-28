package cn.sowell.datacenter.model.tmpl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.basepeople.service.impl.FusionContextFactoryDC;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.dao.ListTemplateDao;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;

import com.abc.application.FusionContext;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;

@Service
public class ListTemplateServiceImpl implements ListTemplateService{
	@Resource
	ListTemplateDao lDao;

	@Resource
	NormalOperateDao nDao;
	
	@Resource
	SystemAdminService adminService;
	
	@Override
	public List<TemplateListTmpl> queryLtmplList(String module, UserIdentifier user) {
		return lDao.queryLtmplList(module, user.getId(), null);
	}
	
	@Override
	public void saveListTemplate(TemplateListTmpl tmpl) {
		if(tmpl != null){
			Date now = new Date();
			if(tmpl.getId() == null){
				//创建
				tmpl.setCreateTime(now);
				tmpl.setUpdateTime(now);
				Long tmplId = nDao.save(tmpl);
				Set<TemplateListColumn> columns = tmpl.getColumns();
				for (TemplateListColumn column : columns) {
					column.setTemplateId(tmplId);
					column.setCreateTime(now);
					column.setUpdateTime(now);
					nDao.save(column);
				}
				Set<TemplateListCriteria> criterias = tmpl.getCriterias();
				for (TemplateListCriteria criteria : criterias) {
					criteria.setTemplateId(tmplId);
					criteria.setCreateTime(now);
					criteria.setUpdateTime(now);
					nDao.save(criteria);
				}
			}else{
				//修改
				updateListTempate(tmpl);
			}
			
		}
	}

	private void updateListTempate(TemplateListTmpl tmpl) {
		TemplateListTmpl origin = getListTemplate(tmpl.getId());
		if(origin != null){
			origin.setTitle(tmpl.getTitle());
			origin.setUnmodifiable(tmpl.getUnmodifiable());
			origin.setDefaultPageSize(tmpl.getDefaultPageSize());
			origin.setDefaultOrderFieldId(tmpl.getDefaultOrderFieldId());
			origin.setDefaultOrderDirection(tmpl.getDefaultOrderDirection());
			
			Date now = new Date();
			
			NormalDaoSetUpdateStrategy.build(
					TemplateListColumn.class, nDao,
					column->column.getId(),
					(oColumn, column)->{
						oColumn.setTitle(column.getTitle());
						oColumn.setFieldKey(column.getFieldKey());
						oColumn.setFieldId(column.getFieldId());
						oColumn.setOrder(column.getOrder());
						oColumn.setOrderable(column.getOrderable());
						oColumn.setSpecialField(column.getSpecialField());
						oColumn.setUpdateTime(now);
						oColumn.setViewOption(column.getViewOption());
					},column->{
						column.setCreateTime(now);
						column.setUpdateTime(now);
						column.setTemplateId(origin.getId());
					})
				.doUpdate(origin.getColumns(), tmpl.getColumns());
			
			NormalDaoSetUpdateStrategy.build(
				TemplateListCriteria.class, nDao, 
				criteria->criteria.getId(), 
				(originCriteria, criteria)->{
					originCriteria.setTitle(criteria.getTitle());
					originCriteria.setFieldId(criteria.getFieldId());
					originCriteria.setFieldKey(criteria.getFieldKey());
					originCriteria.setRelation(criteria.getRelation());
					originCriteria.setQueryShow(criteria.getQueryShow());
					originCriteria.setComparator(criteria.getComparator());
					originCriteria.setInputType(criteria.getInputType());
					originCriteria.setOrder(criteria.getOrder());
					originCriteria.setViewOption(criteria.getViewOption());
					originCriteria.setDefaultValue(criteria.getDefaultValue());
					originCriteria.setPlaceholder(criteria.getPlaceholder());
					originCriteria.setUpdateTime(now);
				}, criteria->{
					criteria.setCreateTime(now);
					criteria.setUpdateTime(now);
					criteria.setTemplateId(origin.getId());
				})
			.doUpdate(origin.getCriterias(), tmpl.getCriterias());
		}else{
			throw new RuntimeException("列表模板[id=" + tmpl.getId() + "]不存在");
		}
	}

	@Override
	public TemplateListTmpl getDefaultListTemplate(UserIdentifier user, String module) {
		SystemAdmin admin = adminService.getSystemAdminByUserId((long) user.getId());
		Long ltmplId = adminService.getDefaultTemplateId(admin.getId(), module, DataCenterConstants.TEMPLATE_TYPE_LIST);
		return getListTemplate(ltmplId);
	}
	
	@Override
	public TemplateListTmpl getListTemplate(Long ltmplId) {
		TemplateListTmpl tmpl = nDao.get(TemplateListTmpl.class, ltmplId);
		if(tmpl != null){
			Set<TemplateListColumn> columns = lDao.getColumnsByTmplId(tmpl.getId());
			tmpl.setColumns(columns);
			Set<TemplateListCriteria> criterias = lDao.getCriteriaByTmplId(tmpl.getId());
			tmpl.setCriterias(criterias);
		}
		return tmpl;
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
	public void removeListTemplate(Long ltmplId) {
		TemplateListTmpl ltmpl = new TemplateListTmpl();
		ltmpl.setId(ltmplId);
		nDao.remove(ltmpl);
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
			HttpServletRequest request) {
		TemplateListTmpl ltmpl = null;
		UserIdentifier user = UserUtils.getCurrentUser();
		if(tmplId == null){
			ltmpl = getDefaultListTemplate(user, );
		}else{
			ltmpl = getListTemplate(tmplId);
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
