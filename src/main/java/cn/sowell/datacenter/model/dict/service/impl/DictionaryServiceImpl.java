package cn.sowell.datacenter.model.dict.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.dict.dao.DictionaryDao;
import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;
import cn.sowell.datacenter.model.dict.pojo.DictionaryField;
import cn.sowell.datacenter.model.dict.service.DictionaryService;

@Service
public class DictionaryServiceImpl implements DictionaryService{

	@Resource
	DictionaryDao dictDao;
	
	@Override
	public List<DictionaryComposite> getAllComposites(String module) {
		List<DictionaryComposite> composites = dictDao.getAllComposites(module);
		Map<Long, List<DictionaryField>> fieldMap = dictDao.getAllFields(CollectionUtils.toSet(composites, composite->composite.getId()));
		
		composites.forEach(composite->composite.setFields(fieldMap.get(composite.getId())));
		return composites;
	}

}
