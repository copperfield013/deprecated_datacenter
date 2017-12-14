package cn.sowell.datacenter.model.basepeople.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.dao.deferedQuery.HibernateRefrectResultTransformer;
import cn.sowell.datacenter.model.basepeople.dao.PropertyDictionaryDao;
import cn.sowell.datacenter.model.basepeople.pojo.PeoplePropertyDictionary;

@Repository
public class PropertyDictionaryDaoImpl implements PropertyDictionaryDao{

	@Resource
	SessionFactory sFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeoplePropertyDictionary> queryAllPropertyDictionary(Long appId) {
		String sql = 
				"	SELECT" +
				"		p.c_id p_id," +
				"		p.c_cn_english p_name," +
				"		p.c_cn_name p_cname," +
				"		p.type p_type," +
				"		p.check_rule p_check_rule," +
				"		a.t_id a_id," +
				"		a.t_info_enname a_name," +
				"		a.t_info_cnname a_cname" +
				"	FROM" +
				"		t_base_people_dictionary p" +
				"	LEFT JOIN t_base_people_information a ON p.c_info_id = a.t_id";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(PeoplePropertyDictionary.class));
		return query.list();
	}

}
