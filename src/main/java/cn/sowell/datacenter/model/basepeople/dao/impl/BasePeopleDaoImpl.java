package cn.sowell.datacenter.model.basepeople.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.sowell.copframe.dao.deferedQuery.HibernateRefrectResultTransformer;
import cn.sowell.datacenter.model.basepeople.BasePeopleDictionaryCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleItemEntity;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.dao.BasePeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;

@Repository
public class BasePeopleDaoImpl implements BasePeopleDao{
	@Resource
	SessionFactory sFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BasePeople> queryList(BasePeopleCriteria bpCriteria, PageInfo pageInfo) {
		String hql = "from BasePeople p";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		if(TextUtils.hasText(bpCriteria.getName())){
			dQuery.appendCondition(" and p.name like :name")
					.setParam("name", "%" + bpCriteria.getName() + "%");
		}
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), true, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		return new ArrayList<BasePeople>();
	}
	
	@Override
	public void insert(Object pojo) {
		sFactory.getCurrentSession().save(pojo);
	}
	
	@Override
	public <T> T get(Class<T> clazz, Long id) {
		return sFactory.getCurrentSession().get(clazz, id);
	}


	@Override
	public void saveOrUpdate(Object pojo) {
		sFactory.getCurrentSession().saveOrUpdate(pojo);
	}
	@Override
	public void update(Object pojo) {
		sFactory.getCurrentSession().update(pojo);
	}
	
	@Override
	public void delete(Object pojo) {
		sFactory.getCurrentSession().delete(pojo);
	}

	@Override
	public List<TBasePeopleItemEntity> fieldList(String field) {
		String sql ="SELECT" +
				" i.*" +
				" FROM" +
				" t_base_people_dictionary d" +
				" LEFT JOIN t_base_people_item i ON i.c_dictionaryid = d.c_id";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		dQuery.appendCondition(" and d.c_cn_english = :field")
				.setParam("field", field);

		SQLQuery query = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, null);
		query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(TBasePeopleItemEntity.class));
		return query.list();
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<TBasePeopleDictionaryEntity> querydicList(BasePeopleDictionaryCriteria bpCriteria, PageInfo pageInfo) {
		String hql = "from TBasePeopleDictionaryEntity p";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		if(TextUtils.hasText(bpCriteria.getcCnName())){
			dQuery.appendCondition(" and p.cCnName like :name")
					.setParam("name", "%" + bpCriteria.getcCnName() + "%");
		}
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), true, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		return new ArrayList<TBasePeopleDictionaryEntity>();
	}

	@Override
	public void updateBasePeople(Map<String, String> map, String id) {
			String mapsql ="";
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
				mapsql += entry.getKey() + "='" + entry.getValue() + "',";
			}
		String sql = "update t_base_people set  "+mapsql.substring(0,mapsql.length()-1)+" where id = :id ";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		query.setString("id",id).executeUpdate();
	}

	@Override
	public List<TBasePeopleDictionaryEntity> searchList(String txt) {
		String hql = "from TBasePeopleDictionaryEntity p";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		dQuery.appendCondition(" and p.cCnName like :name").setParam("name", "%" + txt + "%");
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), true, null);
			return query.list();
		}
		return new ArrayList<TBasePeopleDictionaryEntity>();
	}


	@Override
	public <T> T getDicById(Class<T> clazz, String id) {
		return sFactory.getCurrentSession().get(clazz, id);
	}
}
