package cn.sowell.datacenter.model.basepeople.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.annotation.Resource;

import cn.sowell.copframe.dao.deferedQuery.HibernateRefrectResultTransformer;
import cn.sowell.datacenter.model.basepeople.BasePeopleDictionaryCriteria;
import cn.sowell.datacenter.model.basepeople.ExcelModelCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.*;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;

import org.apache.commons.lang.StringEscapeUtils;
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
import cn.sowell.datacenter.model.basepeople.pojo.ExcelModel;
import cn.sowell.datacenter.model.basepeople.pojo.ExcelModelOrder;

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
	public Long insert(Object pojo) {
		Long id = (Long) sFactory.getCurrentSession().save(pojo);
		return id;
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
				" t_base_people_item i" +
				" LEFT JOIN t_base_people_dictionary d ON i.c_dictionaryid = d.c_id";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		dQuery.appendCondition(" and d.c_cn_english = :field")
				.setParam("field", field);
		Query countQuery = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		if(count > 0){
			SQLQuery query = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, null);
			query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(TBasePeopleItemEntity.class));
			return query.list();
		}
		return new ArrayList<TBasePeopleItemEntity>();
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
	public <T> T getDicById(Class<T> clazz, Long id) {
		return sFactory.getCurrentSession().get(clazz, id);
	}
	
	@Override
	public List<TBasePeopleDictionaryEntity> getColumns(Long modelId) {
		String sql = "SELECT d.*"
				+ " FROM t_base_people_dictionary d"
				+ " LEFT JOIN t_excel_model_order e ON d.c_id = e.c_dictionaryid";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		dQuery.appendCondition(" and e.c_modelid = :id ORDER BY e.c_orderid ASC").setParam("id",modelId);
		Query countQuery = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		if(count > 0){
			Query query = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, null);
			query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(TBasePeopleDictionaryEntity.class));
			return query.list();
		}
		return new ArrayList<TBasePeopleDictionaryEntity>();
	}

	@Override
	public List<ExcelModel> queryExcelModel(ExcelModelCriteria criteria, PageInfo pageInfo) {
		String hql = "from ExcelModel e";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		if(TextUtils.hasText(criteria.getModelName())){
			dQuery.appendCondition(" and e.modelName like :name")
					.setParam("name", "%" + criteria.getModelName() + "%");
		}
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), true, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		return new ArrayList<ExcelModel>();
	}



	@SuppressWarnings("unlocked")
	@Override
	public List<CityEntiy> getbystatus(String status) {
		String sql ="SELECT\n" +
				"\tSUBSTR(id,1,9) as city,\n" +
				"\tt_position. NAME AS name\n" +
				"FROM\n" +
				"\tt_position";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);

		dQuery.appendCondition("where id like '6%'");
		dQuery.appendCondition(" and level =:level")
				.setParam("level", status);

		SQLQuery query = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, null);
		query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(CityEntiy.class));
		return query.list();
	}

	@Override
	public void deleteExcelOrder(Long modelId) {
		String sql ="DELETE FROM t_excel_model_order WHERE c_modelid = :modelId";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		query.setLong("modelId", modelId).executeUpdate();
	}

	@Override
	public List<TBasePeopleDictionaryEntity> getDicByModelId(Long modelId) {
		// TODO Auto-generated method stub
		String sql = "SELECT d.* FROM t_base_people_dictionary d"
				+ " LEFT JOIN t_excel_model_order o ON d.c_id = o.c_dictionaryid"
				+ " LEFT JOIN t_excel_model m ON o.c_modelid = m.c_id";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		dQuery.appendCondition(" and m.c_id = :id ORDER BY o.c_orderid ASC").setParam("id",modelId);
		Query countQuery = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		if(count > 0){
			Query query = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, null);
			query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(TBasePeopleDictionaryEntity.class));
			return query.list();
		}
		return new ArrayList<TBasePeopleDictionaryEntity>();
	}


	public  List<TBasePeopleDictionaryEntity> dicListByUser(){
		String hql = "from TBasePeopleDictionaryEntity p";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), true, null);
			return query.list();
		}
		return new ArrayList<TBasePeopleDictionaryEntity>();
	}

	@Override
	public List<BasePeopleItem> dicItemByUser() {
		String hql = "from BasePeopleItem p";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), true, null);
			return query.list();
		}
		return new ArrayList<BasePeopleItem>();
	}
}
