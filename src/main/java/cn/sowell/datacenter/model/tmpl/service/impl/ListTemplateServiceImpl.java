package cn.sowell.datacenter.model.tmpl.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;
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
			if(tmpl.getId() == null){
				//创建
				Long tmplId = nDao.save(tmpl);
				Set<TemplateListColumn> columns = tmpl.getColumns();
				for (TemplateListColumn column : columns) {
					column.setTemplateId(tmplId);
					nDao.save(column);
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
			Map<Long, TemplateListColumn> originColumnMap = CollectionUtils.toMap(origin.getColumns(), column->column.getId());
			Set<Long> toRemove = new HashSet<Long>(originColumnMap.keySet());
			
			Date now = new Date();
			if(tmpl.getColumns() != null){
				for (TemplateListColumn column : tmpl.getColumns()) {
					if(column.getId() != null){
						toRemove.remove(column.getId());
						//修改
						TemplateListColumn originColumn = originColumnMap.get(column.getId());
						originColumn.setTitle(column.getTitle());
						originColumn.setFieldKey(column.getFieldKey());
						originColumn.setFieldId(column.getFieldId());
						originColumn.setOrder(column.getOrder());
						originColumn.setOrderable(column.getOrderable());
						originColumn.setSpecialField(column.getSpecialField());
						originColumn.setUpdateTime(now);
						originColumn.setViewOption(column.getViewOption());
						nDao.update(originColumn);
					}else{
						//添加
						column.setCreateTime(now);
						column.setUpdateTime(now);
						column.setTemplateId(origin.getId());
						nDao.save(column);
					}
				}
			}
			toRemove.forEach(columnId->{
				nDao.remove(originColumnMap.get(columnId));
			});
			
			
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
	public List<PeopleData> queryPeopleList(Object object, PageInfo pageInfo) {
		return peopleService.query(new PeopleDataCriteria(), pageInfo);
	}
	
	
	@Override
	public void removeListTemplate(Long ltmplId) {
		TemplateListTmpl ltmpl = new TemplateListTmpl();
		ltmpl.setId(ltmplId);
		nDao.remove(ltmpl);
	}
	
	
}
