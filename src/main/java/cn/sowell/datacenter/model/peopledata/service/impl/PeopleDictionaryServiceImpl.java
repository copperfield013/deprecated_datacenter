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
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeopleItem;
import cn.sowell.datacenter.model.common.dao.NormalOperateDao;
import cn.sowell.datacenter.model.peopledata.dao.PeopleDictionaryDao;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateData;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateField;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateGroup;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;

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
	public void mergeTemplate(PeopleTemplateData data) {
		if(data.getTmplId() != null){
			updateTemplate(data);
		}else{
			createTemplate(data);
		}
		
	}
	
	private void updateTemplate(PeopleTemplateData data) {
		PeopleTemplateData origin = getTemplate(data.getTmplId());
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
			Map<Long, PeopleFieldDictionaryItem> fieldMap = dictDao.getFieldMap(addFieldIds);
			
			Date now = new Date();
			origin.setName(data.getName());
			origin.setUpdateTime(now);
			nDao.update(origin);
			Set<Long> toDeleteGroupId = CollectionUtils.toSet(origin.getGroups(), group->group.getId());
			Set<Long> toDeleteFieldId = new HashSet<Long>(originGroupFieldIds);
			Map<Long, PeopleTemplateGroup> originGroupMap = CollectionUtils.toMap(origin.getGroups(), group->group.getId());
			for (PeopleTemplateGroup group : data.getGroups()) {
				if(group.getId() != null){
					if(originGroupMap.containsKey(group.getId())){
						PeopleTemplateGroup originGroup = originGroupMap.get(group.getId());
						toDeleteGroupId.remove(group.getId());
						originGroup.setTitle(group.getTitle());
						originGroup.setOrder(group.getOrder());
						originGroup.setUpdateTime(now);
						nDao.update(originGroup);
						
						Map<Long, PeopleTemplateField> originFieldMap = CollectionUtils.toMap(originGroup.getFields(), field->field.getId());
						for (PeopleTemplateField field : group.getFields()) {
							if(field.getId() != null){
								if(originFieldMap.containsKey(field.getId())){
									toDeleteFieldId.remove(field.getId());
									PeopleTemplateField originField = originFieldMap.get(field.getId());
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
									field.setFieldName(fieldMap.get(field.getFieldId()).getName());
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
					for (PeopleTemplateField field : group.getFields()) {
						if(fieldMap.containsKey(field.getFieldId())){
							field.setFieldName(fieldMap.get(field.getFieldId()).getName());
							field.setGroupId(groupId);
							field.setUpdateTime(now);
							nDao.save(field);
						}else{
							throw new RuntimeException("找不到fieldId为[" + field.getFieldId() + "]的字段");
						}
					}
				}
			}
			nDao.remove(PeopleTemplateGroup.class, toDeleteGroupId);
			nDao.remove(PeopleTemplateField.class, toDeleteFieldId);
		}else{
			throw new RuntimeException("找不到id为[" + data.getTmplId() + "]的模板，无法更新");
		}
	}

	private void createTemplate(PeopleTemplateData data) {
		Set<Long> fieldIds = new HashSet<Long>();
		data.getGroups().forEach(group->group.getFields().forEach(field->fieldIds.add(field.getFieldId())));
		Map<Long, PeopleFieldDictionaryItem> fieldMap = dictDao.getFieldMap(fieldIds);
		Date now = new Date();
		data.setCreateTime(now);
		data.setUpdateTime(now);
		Long tmplId = nDao.save(data);
		for (PeopleTemplateGroup group : data.getGroups()) {
			group.setTmplId(tmplId);
			group.setUpdateTime(now);
			Long groupId = nDao.save(group);
			for (PeopleTemplateField field : group.getFields()) {
				if(fieldMap.containsKey(field.getFieldId())){
					field.setFieldName(fieldMap.get(field.getFieldId()).getName());
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
	public PeopleTemplateData getDefaultTemplate(UserIdentifier user) {
		SystemAdmin admin = aService.getSystemAdminByUserId((long)user.getId());
		Long defaultTmplId = admin.getDefaultTemplateId();
		if(defaultTmplId != null){
			return getTemplate(defaultTmplId);
		}else{
			return null;
		}
	}
	
	@Override
	public PeopleTemplateData getTemplate(Long tmplId) {
		PeopleTemplateData data = nDao.get(PeopleTemplateData.class, tmplId);
		if(data != null){
			List<PeopleTemplateGroup> groups = getTemplateGroup(data.getTmplId());
			if(groups != null){
				data.setGroups(groups);
			}
		}
		return data;
	}
	
	@Override
	public List<PeopleTemplateData> getAllTemplateList(UserIdentifier user,
			PageInfo pageInfo,
			boolean loadDetail) {
		List<PeopleTemplateData> list = dictDao.getTemplateList(user, pageInfo);
		if(loadDetail){
			list.forEach(data->{
				data.setGroups(getTemplateGroup(data.getTmplId()));
			});
		}
		return list;
	}

	private List<PeopleTemplateGroup> getTemplateGroup(Long tmplId) {
		List<PeopleTemplateGroup> groups = dictDao.getTemplateGroups(tmplId);
		Map<Long, List<PeopleTemplateField>> fieldMap = dictDao.getTemplateFieldsMap(CollectionUtils.toSet(groups, group->group.getId()));
		groups.forEach(group->{
			List<PeopleTemplateField> fields = fieldMap.get(group.getId());
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
	public List<BasePeopleItem> getAllEnumList() {
		return dictDao.getAllEnumList();
	}
	
}

