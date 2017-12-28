package cn.sowell.datacenter.model.peopledata.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.peopledata.dao.PeopleDictionaryDao;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateData;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateField;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateGroup;

@Repository
public class PeopleDictionaryDaoImpl implements PeopleDictionaryDao{

	@Resource
	SessionFactory sFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeopleCompositeDictionaryItem> queryAllInfo(String code) {
		String hql = "from PeopleCompositeDictionaryItem c";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeopleFieldDictionaryItem> queryAllField(Set<Long> compositeIds) {
		if(compositeIds != null && !compositeIds.isEmpty()){
			String hql = "from PeopleFieldDictionaryItem f where f.compositeId in (:compositeIds)";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("compositeIds", compositeIds);
			return query.list();
		}else{
			return new ArrayList<PeopleFieldDictionaryItem>();
		}
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public List<PeopleDictionaryField> queryAllField(String code) {
		String sql = 
				"	SELECT" +
				"		d.c_id field_id," +
				"		d.c_cn_english field_name," +
				"		d.c_cn_name field_cname," +
				"		d.type field_type," +
				"		info.t_id c_id," +
				"		info.t_info_enname c_name," +
				"		info.t_info_cnname c_cname" +
				"	FROM" +
				"		t_base_people_dictionary d" +
				"	LEFT JOIN t_base_people_information info ON d.c_info_id = info.t_id" +
				"	WHERE" +
				"		info.t_id IS NOT NULL";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(PeopleDictionaryField.class));
		return query.list();
		
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeopleTemplateGroup> getTemplateGroups(Long tmplId) {
		String hql = "from PeopleTemplateGroup g where g.tmplId = :tmplId order by g.order asc";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setLong("tmplId", tmplId);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, List<PeopleTemplateField>> getTemplateFieldsMap(
			Set<Long> groupIdSet) {
		if(groupIdSet != null && !groupIdSet.isEmpty()){
			String hql = "from PeopleTemplateField f where f.groupId in (:groupIds) order by f.order asc";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("groupIds", groupIdSet);
			List<PeopleTemplateField> fieldList = query.list();
			return CollectionUtils.toListMap(fieldList, field->field.getGroupId());
		}else{
			return new HashMap<Long, List<PeopleTemplateField>>();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeopleTemplateData> getTemplateList(UserIdentifier user, PageInfo pageInfo) {
		String hql = "from PeopleTemplateData t ";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		QueryUtils.setPagingParamWithCriteria(query, pageInfo);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, PeopleFieldDictionaryItem> getFieldMap(Set<Long> fieldIds) {
		if(fieldIds != null && !fieldIds.isEmpty()){
			String hql = "from PeopleFieldDictionaryItem f where f.id in (:fieldIds)";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("fieldIds", fieldIds, StandardBasicTypes.LONG);
			List<PeopleFieldDictionaryItem> list = query.list();
			return CollectionUtils.toMap(list, item->item.getId());
		}else{
			return new HashMap<Long, PeopleFieldDictionaryItem>();
		}
	}
	
	@Override
	public boolean removeTemplate(Long tmplId) {
		PeopleTemplateData data = new PeopleTemplateData();
		data.setTmplId(tmplId);
		sFactory.getCurrentSession().delete(data);
		return true;
	}

}

