package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.dao.PeopleDictionaryDao;
import cn.sowell.datacenter.model.peopledata.pojo.OptionItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;

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
	public List<DictionaryOption> getAllEnumList() {
		return dictDao.getAllEnumList();
	}
	
	@Override
	public Map<Long, List<OptionItem>> getOptionsMap(Set<Long> fieldIds) {
		return dictDao.getFieldOptionsMap(fieldIds);
	}
	
}

