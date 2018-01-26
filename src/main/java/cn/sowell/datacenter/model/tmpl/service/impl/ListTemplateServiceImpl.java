package cn.sowell.datacenter.model.tmpl.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.dao.ListTemplateDao;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;

@Service
public class ListTemplateServiceImpl implements ListTemplateService{
	@Resource
	ListTemplateDao lDao;

	@Resource
	NormalOperateDao nDao;
	
	@Resource
	SystemAdminService adminService;
	
	@Override
	public List<TemplateListTmpl> queryLtmplList(UserIdentifier user) {
		return lDao.queryLtmplList(user.getId(), null);
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
	public TemplateListTmpl getDefaultListTemplate(UserIdentifier user) {
		SystemAdmin admin = adminService.getSystemAdminByUserId(user.getId());
		Long ltmplId = admin.getDefaultListTemplateId();
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
	
	
}
