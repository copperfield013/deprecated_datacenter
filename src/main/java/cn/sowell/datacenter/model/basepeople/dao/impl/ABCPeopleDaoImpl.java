package cn.sowell.datacenter.model.basepeople.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.copframe.dao.deferedQuery.HibernateRefrectResultTransformer;
import cn.sowell.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.datacenter.model.basepeople.dao.ABCPeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;

@Repository
public class ABCPeopleDaoImpl implements ABCPeopleDao{

	@Resource
	SessionFactory sFactory;
	
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<BasePeople> queryABC(PeopleDataCriteria criteria, PageInfo pageInfo) {
		String sql ="SELECT" +
				"	p.id, p.people_code code," +
				"	p.abc0706 name," +
				"	c.ABC0790 idcode," +
				"	p.abc0068 birthday," +
				"	a.ABC0126 address," +
				"	p.ABC0705 gender" +
				" FROM" +
				"	t_people_baseinfo p" +
				" LEFT JOIN t_people_certificate c ON c.people_code = p.people_code" + 
				" left join t_people_address a on a.people_code = p.people_code";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		
		SQLQuery countQuery = dQuery.createSQLQuery(sFactory.getCurrentSession(), false, new WrapForCountFunction());
		
		pageInfo.setCount(FormatUtils.toInteger(countQuery.uniqueResult()));
		if(pageInfo.getCount() > 0){
			SQLQuery query = dQuery.createSQLQuery(sFactory.getCurrentSession(), false, null);
			
			query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(BasePeople.class));
			return query.list();
		}
		return new ArrayList<BasePeople>();
	}
	
	
	@Override
	public BasePeople getPeople(String peopleCode) {
		String sql ="SELECT" +
				"	p.id, p.people_code code," +
				"	p.abc0706 name," +
				"	c.ABC0790 idcode," +
				"	p.abc0068 birthday," +
				"	a.ABC0126 address," +
				"	p.ABC0705 gender" +
				" FROM" +
				"	t_people_baseinfo p" +
				" LEFT JOIN t_people_certificate c ON c.people_code = p.people_code" + 
				" left join t_people_address a on a.people_code = p.people_code";
		DeferedParamQuery dQuery = new DeferedParamQuery(sql);
		dQuery.appendCondition("and p.people_code = :peopleCode")
				.setParam("peopleCode", peopleCode)
				;
		SQLQuery query = dQuery.createSQLQuery(sFactory.getCurrentSession(), true, null);
		query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(BasePeople.class));
		return (BasePeople) query.uniqueResult();
		
	}
	
	
	

}
