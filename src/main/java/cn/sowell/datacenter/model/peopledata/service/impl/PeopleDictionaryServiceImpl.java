package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.Date;
import java.util.HashSet;
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
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.dict.pojo.DictionaryField;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.dao.PeopleDictionaryDao;
import cn.sowell.datacenter.model.peopledata.pojo.OptionItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.TemplateDetailField;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;

@Service
public class PeopleDictionaryServiceImpl implements PeopleDictionaryService{

	@Resource
	PeopleDictionaryDao dictDao;
	
	@Resource
	NormalOperateDao nDao;
	
	@Resource
	SystemAdminService aService;
	
	@Override
	public List<PeopleCompositeDictionaryItem> getAllInfo(String code) {
		List<PeopleCompositeDictionaryItem> compositeList = dictDao.queryAllInfo(code);
		List<PeopleFieldDictionaryItem> fieldList = dictDao.queryAllField(CollectionUtils.toSet(compositeList, c->c.getId()));
		Map<Object, List<PeopleFieldDictionaryItem>> fieldsMap = CollectionUtils.toListMap(fieldList, field->field.getCompositeId());
		for (PeopleCompositeDictionaryItem composite : compositeList) {
			composite.setFields(fieldsMap.get(composite.getId()));
		}
		return compositeList;
	}
	
	@Override
	@Transactional
	public void mergeTemplate(TemplateDetailTemplate data) {
		if(data.getTmplId() != null){
			updateTemplate(data);
		}else{
			createTemplate(data);
		}
		
	}
	
	private void updateTemplate(TemplateDetailTemplate data) {
		TemplateDetailTemplate origin = getTemplate(data.getTmplId());
		if(origin != null){
			Set<Long> addFieldIds = new HashSet<Long>();
			data.getGroups().forEach(group->
				group.getFields().forEach(field->{
					if(field.getId() == null){
						addFieldIds.add(field.getFieldId());
					}
				})
			);
			Set<Long> originGroupFieldIds = new HashSet<Long>();
			origin.getGroups().forEach(group->group.getFields().forEach(field->originGroupFieldIds.add(field.getId())));
			Map<Long, DictionaryField> fieldMap = dictDao.getFieldMap(addFieldIds);
			
			Date now = new Date();
			origin.setName(data.getName());
			origin.setUpdateTime(now);
			nDao.update(origin);
			Set<Long> toDeleteGroupId = CollectionUtils.toSet(origin.getGroups(), group->group.getId());
			Set<Long> toDeleteFieldId = new HashSet<Long>(originGroupFieldIds);
			Map<Long, TemplateDetailFieldGroup> originGroupMap = CollectionUtils.toMap(origin.getGroups(), group->group.getId());
			for (TemplateDetailFieldGroup group : data.getGroups()) {
				if(group.getId() != null){
					if(originGroupMap.containsKey(group.getId())){
						TemplateDetailFieldGroup originGroup = originGroupMap.get(group.getId());
						toDeleteGroupId.remove(group.getId());
						originGroup.setTitle(group.getTitle());
						originGroup.setOrder(group.getOrder());
						originGroup.setUpdateTime(now);
						nDao.update(originGroup);
						
						Map<Long, TemplateDetailField> originFieldMap = CollectionUtils.toMap(originGroup.getFields(), field->field.getId());
						for (TemplateDetailField field : group.getFields()) {
							if(field.getId() != null){
								if(originFieldMap.containsKey(field.getId())){
									toDeleteFieldId.remove(field.getId());
									TemplateDetailField originField = originFieldMap.get(field.getId());
									originField.setTitle(field.getTitle());
									originField.setColNum(field.getColNum());
									originField.setOrder(field.getOrder());
									originField.setViewValue(field.getViewValue());
									originField.setUpdateTime(now);
									nDao.update(originField);
								}else{
									throw new RuntimeException("字段组[id=" + group.getId() + "]不能修改字段[id=" + field.getId() + "，因为字段不存在，可能是所在模板已经被修改]");
								}
							}else{
								if(fieldMap.containsKey(field.getFieldId())){
									field.setFieldName(fieldMap.get(field.getFieldId()).getTitle());
									field.setGroupId(group.getId());
									field.setUpdateTime(now);
									nDao.save(field);
								}else{
									throw new RuntimeException("找不到fieldId为[" + field.getFieldId() + "]的字段");
								}
							}
						}
					}else{
						throw new RuntimeException("模板[id=" + origin.getTmplId() + "]不能修改字段组[id=" + group.getId() + "，因为字段组不存在，可能是所在模板已经被修改]");
					}
				}else{
					group.setTmplId(origin.getTmplId());
					group.setUpdateTime(now);
					Long groupId = nDao.save(group);
					for (TemplateDetailField field : group.getFields()) {
						if(fieldMap.containsKey(field.getFieldId())){
							field.setFieldName(fieldMap.get(field.getFieldId()).getTitle());
							field.setGroupId(groupId);
							field.setUpdateTime(now);
							nDao.save(field);
						}else{
							throw new RuntimeException("找不到fieldId为[" + field.getFieldId() + "]的字段");
						}
					}
				}
			}
			nDao.remove(TemplateDetailFieldGroup.class, toDeleteGroupId);
			nDao.remove(TemplateDetailField.class, toDeleteFieldId);
		}else{
			throw new RuntimeException("找不到id为[" + data.getTmplId() + "]的模板，无法更新");
		}
	}

	private void createTemplate(TemplateDetailTemplate data) {
		Set<Long> fieldIds = new HashSet<Long>();
		data.getGroups().forEach(group->group.getFields().forEach(field->fieldIds.add(field.getFieldId())));
		Map<Long, DictionaryField> fieldMap = dictDao.getFieldMap(fieldIds);
		Date now = new Date();
		data.setCreateTime(now);
		data.setUpdateTime(now);
		Long tmplId = nDao.save(data);
		for (TemplateDetailFieldGroup group : data.getGroups()) {
			group.setTmplId(tmplId);
			group.setUpdateTime(now);
			Long groupId = nDao.save(group);
			for (TemplateDetailField field : group.getFields()) {
				if(fieldMap.containsKey(field.getFieldId())){
					field.setFieldName(fieldMap.get(field.getFieldId()).getTitle());
					field.setGroupId(groupId);
					field.setUpdateTime(now);
					nDao.save(field);
				}else{
					throw new RuntimeException("找不到fieldId为[" + field.getFieldId() + "]的字段");
				}
			}
		}
	}

	@Override
	public TemplateDetailTemplate getDefaultTemplate(UserIdentifier user, String module, String type) {
		Long defaultTmplId = aService.getDefaultTemplateId((long)user.getId(), module, type);
		if(defaultTmplId != null){
			return getTemplate(defaultTmplId);
		}else{
			return null;
		}
	}
	
	@Override
	public TemplateDetailTemplate getTemplate(Long tmplId) {
		TemplateDetailTemplate data = nDao.get(TemplateDetailTemplate.class, tmplId);
		if(data != null){
			List<TemplateDetailFieldGroup> groups = getTemplateGroup(data.getTmplId());
			if(groups != null){
				data.setGroups(groups);
			}
		}
		return data;
	}
	
	@Override
	public List<TemplateDetailTemplate> getAllTemplateList(
			String module,
			UserIdentifier user,
			PageInfo pageInfo,
			boolean loadDetail) {
		List<TemplateDetailTemplate> list = dictDao.getTemplateList(module, user, pageInfo);
		if(loadDetail){
			list.forEach(data->{
				data.setGroups(getTemplateGroup(data.getTmplId()));
			});
		}
		return list;
	}

	private List<TemplateDetailFieldGroup> getTemplateGroup(Long tmplId) {
		List<TemplateDetailFieldGroup> groups = dictDao.getTemplateGroups(tmplId);
		Map<Long, List<TemplateDetailField>> fieldMap = dictDao.getTemplateFieldsMap(CollectionUtils.toSet(groups, group->group.getId()));
		groups.forEach(group->{
			List<TemplateDetailField> fields = fieldMap.get(group.getId());
			if(fields != null){
				group.setFields(fields);
			}
		});
		return groups;
	}
	
	@Override
	public void removeTemplate(UserIdentifier user, Long tmplId) {
		if(!dictDao.removeTemplate(tmplId)){
			throw new RuntimeException("删除失败，没有找到id为[" + tmplId + "]的模板对象");
		}
	}

	@Override
	public List<DictionaryOption> getAllEnumList() {
		return dictDao.getAllEnumList();
	}
	
	@Override
	public Map<Long, List<OptionItem>> getOptionsMap(Set<Long> fieldIds) {
		return dictDao.getFieldOptionsMap(fieldIds);
	}
	
}

