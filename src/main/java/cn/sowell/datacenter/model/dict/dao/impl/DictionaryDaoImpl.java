package cn.sowell.datacenter.model.dict.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.dict.dao.DictionaryDao;
import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;
import cn.sowell.datacenter.model.dict.pojo.DictionaryField;


@Repository
public class DictionaryDaoImpl implements DictionaryDao{

	@Resource
	SessionFactory sFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryComposite> getAllComposites(String module) {
		String hql = "from DictionaryComposite c where c.module = :module";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setString("module", module);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, List<DictionaryField>> getAllFields(Set<Long> compositeIds) {
		if(compositeIds != null && compositeIds.size() > 0){
			String hql = "from DictionaryField f where f.compositeId in (:compositeIds)";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("compositeIds", compositeIds);
			List<DictionaryField> list = query.list();
			return CollectionUtils.toListMap(list, field->field.getCompositeId());
		}else{
			return new HashMap<Long, List<DictionaryField>>();
		}
	}

}
