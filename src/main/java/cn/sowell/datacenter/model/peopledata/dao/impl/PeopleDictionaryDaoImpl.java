package cn.sowell.datacenter.model.peopledata.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.deferedQuery.ColumnMapResultTransformer;
import cn.sowell.copframe.dao.deferedQuery.HibernateRefrectResultTransformer;
import cn.sowell.copframe.dao.deferedQuery.SimpleMapWrapper;
import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.dict.pojo.DictionaryField;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.dao.PeopleDictionaryDao;
import cn.sowell.datacenter.model.peopledata.pojo.OptionItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.TemplateDetailField;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;

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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TemplateDetailFieldGroup> getTemplateGroups(Long tmplId) {
		String hql = "from TemplateDetailFieldGroup g where g.tmplId = :tmplId order by g.order asc";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setLong("tmplId", tmplId);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, List<TemplateDetailField>> getTemplateFieldsMap(
			Set<Long> groupIdSet) {
		if(groupIdSet != null && !groupIdSet.isEmpty()){
			String sql = "select f.*, df.c_type from t_tmpl_detail_field f "
					+ "left join t_dictionary_field df on f.field_id = df.id where f.group_id in (:groupIds) order by f.c_order asc ";
			SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
			
			query.setParameterList("groupIds", groupIdSet);
			query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(TemplateDetailField.class));
			List<TemplateDetailField> fieldList = query.list();
			return CollectionUtils.toListMap(fieldList, field->field.getGroupId());
		}else{
			return new HashMap<Long, List<TemplateDetailField>>();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TemplateDetailTemplate> getTemplateList(String module, UserIdentifier user, PageInfo pageInfo) {
		String hql = "from TemplateDetailTemplate t where t.module = :module";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setString("module", module);
		QueryUtils.setPagingParamWithCriteria(query, pageInfo);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, DictionaryField> getFieldMap(Set<Long> fieldIds) {
		if(fieldIds != null && !fieldIds.isEmpty()){
			String hql = "from DictionaryField f where f.id in (:fieldIds)";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("fieldIds", fieldIds, StandardBasicTypes.LONG);
			List<DictionaryField> list = query.list();
			return CollectionUtils.toMap(list, item->item.getId());
		}else{
			return new HashMap<Long, DictionaryField>();
		}
	}
	
	@Override
	public boolean removeTemplate(Long tmplId) {
		TemplateDetailTemplate data = new TemplateDetailTemplate();
		data.setTmplId(tmplId);
		sFactory.getCurrentSession().delete(data);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryOption> getAllEnumList() {
		String hql = "from DictionaryOption";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		return query.list();
		
	}

	@Override
	public Map<Long, List<OptionItem>> getFieldOptionsMap(Set<Long> fieldIds) {
		String sql = 
				"	SELECT" +
				"		f.id field_id, o.c_title" +
				"	FROM" +
				"		t_dictionary_field f" +
				"	LEFT JOIN t_dictionary_optiongroup og ON f.optgroup_id = og.id" +
				"	LEFT JOIN t_dictionary_option o ON og.id = o.group_id" +
				"	where f.id in (:fieldIds)" +
				"	and o.c_disabled is null" +
				"	and o.c_deleted is NULL" +
				"	and og.c_deleted is null" +
				"	and og.c_disabled is null" +
				"	order by o.c_order asc";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameterList("fieldIds", fieldIds);
		Map<Long, List<OptionItem>> map = new HashMap<Long, List<OptionItem>>();
		query.setResultTransformer(new ColumnMapResultTransformer<byte[]>() {
			private static final long serialVersionUID = -392302880551548725L;

			@Override
			protected byte[] build(SimpleMapWrapper mapWrapper) {
				OptionItem item = new OptionItem();
				item.setTitle(mapWrapper.getString("c_title"));
				item.setValue(mapWrapper.getString("c_title"));
				Long fieldId = mapWrapper.getLong("field_id");
				if(!map.containsKey(fieldId)){
					map.put(fieldId, new ArrayList<OptionItem>());
				}
				map.get(fieldId).add(item);
				return null;
			}
		});
		query.list();
		return map;
	}
	
}

