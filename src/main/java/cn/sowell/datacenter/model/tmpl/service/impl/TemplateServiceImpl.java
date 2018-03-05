package cn.sowell.datacenter.model.tmpl.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.peopledata.pojo.TemplateDetailField;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;
import cn.sowell.datacenter.model.tmpl.dao.DetailTemplateDao;
import cn.sowell.datacenter.model.tmpl.dao.ListTemplateDao;
import cn.sowell.datacenter.model.tmpl.dao.TemplateDao;
import cn.sowell.datacenter.model.tmpl.pojo.AbstractTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateAdminDefaultTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;
import cn.sowell.datacenter.model.tmpl.strategy.TemplateUpdateStrategy;

@Service
public class TemplateServiceImpl implements TemplateService{

	@Resource
	NormalOperateDao nDao;
	
	@Resource
	ListTemplateDao lDao;
	
	@Resource
	DetailTemplateDao dDao;
	
	@Resource
	TemplateDao tDao;

	@Resource
	SystemAdminService aService;
	
	@Resource
	TemplateUpdateStrategyFactory tmplUpdateStrategyFactory;
	
	@Override
	public AbstractTemplate getTemplate(long tmplId) {
		AbstractTemplate tmpl = getDetailTemplate(tmplId);
		if(tmpl == null) {
			tmpl = getListTemplate(tmplId);
		}
		return tmpl;
	}
	
	@Override
	public TemplateDetailTemplate getDetailTemplate(long tmplId) {
		TemplateDetailTemplate data = nDao.get(TemplateDetailTemplate.class, tmplId);
		if(data != null){
			List<TemplateDetailFieldGroup> groups = getTemplateGroups(data.getId());
			if(groups != null){
				data.setGroups(groups);
			}
		}
		return data;
	}

	@Override
	public TemplateListTempalte getListTemplate(long tmplId) {
		TemplateListTempalte tmpl = nDao.get(TemplateListTempalte.class, tmplId);
		if(tmpl != null){
			Set<TemplateListColumn> columns = lDao.getColumnsByTmplId(tmpl.getId());
			tmpl.setColumns(columns);
			Set<TemplateListCriteria> criterias = lDao.getCriteriaByTmplId(tmpl.getId());
			tmpl.setCriterias(criterias);
		}
		return tmpl;
	}

	
	@Override
	public TemplateAdminDefaultTemplate getAdminDefaultTemplate(long adminId, String module, String type) {
		return tDao.getAdminDefaultTempalte(adminId, module, type);
	}
	
	@Override
	public TemplateDetailTemplate getDefaultDetailTemplate(UserIdentifier user, String module) {
		return (TemplateDetailTemplate) getDefaultTemplate(user, module, DataCenterConstants.TEMPLATE_TYPE_DETAIL);
	}

	@Override
	public TemplateListTempalte getDefaultListTemplate(UserIdentifier user, String module) {
		return (TemplateListTempalte) getDefaultTemplate(user, module, DataCenterConstants.TEMPLATE_TYPE_LIST);
	}
	
	private AbstractTemplate getDefaultTemplate(UserIdentifier user, String module, String templateType) {
		SystemAdmin admin = aService.getSystemAdminByUserId((long) user.getId());
		if(admin != null) {
			TemplateAdminDefaultTemplate t = tDao.getAdminDefaultTempalte(admin.getId(), module, templateType);
			if(t != null) {
				return getTemplate(t.getTmplId());
			}
		}
		return null;
	}

	@Override
	public void removeTemplate(UserIdentifier user, Long tmplId) {
		AbstractTemplate template = getTemplate(tmplId);
		if(template != null) {
			nDao.remove(template);
		}
	}

	@Override
	public void setTemplateAsDefault(UserIdentifier user, long tmplId) {
		SystemAdmin admin = aService.getSystemAdminByUserId((long) user.getId());
		if(admin != null) {
			AbstractTemplate template = getTemplate(tmplId);
			if(template != null) {
				tDao.setTemplateAsDefault(
						admin.getId(), 
						template.getModule(), 
						DataCenterConstants.mapTemplateType(template.getClass()), 
						tmplId);
			}
		}
	}

	@Override
	@Transactional
	public <T extends AbstractTemplate> void mergeTemplate(T template) {
		TemplateUpdateStrategy<T> strategy = tmplUpdateStrategyFactory.getStrategy(template);
		if(template.getId() != null) {
			strategy.update(template);
		}else {
			strategy.create(template);
		}
	}
	

	
	@Override
	public List<TemplateDetailTemplate> getAllDetailTemplateList(String module, UserIdentifier user, PageInfo pageInfo,
			boolean loadDetail) {
		List<TemplateDetailTemplate> list = dDao.getTemplateList(module, user, pageInfo);
		if(loadDetail){
			list.forEach(data->{
				data.setGroups(getTemplateGroups(data.getId()));
			});
		}
		return list;
	}
	
	private List<TemplateDetailFieldGroup> getTemplateGroups(Long tmplId) {
		List<TemplateDetailFieldGroup> groups = dDao.getTemplateGroups(tmplId);
		Map<Long, List<TemplateDetailField>> fieldMap = dDao.getTemplateFieldsMap(CollectionUtils.toSet(groups, group->group.getId()));
		groups.forEach(group->{
			List<TemplateDetailField> fields = fieldMap.get(group.getId());
			if(fields != null){
				group.setFields(fields);
			}
		});
		return groups;
	}

}
