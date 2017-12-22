package cn.sowell.datacenter.model.peopledata.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.datacenter.model.peopledata.dao.PeopleDictionaryDao;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;

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

}
