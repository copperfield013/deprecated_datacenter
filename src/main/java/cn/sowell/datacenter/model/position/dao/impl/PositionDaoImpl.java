package cn.sowell.datacenter.model.position.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.abc.extface.dto.Position;

import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.copframe.dao.deferedQuery.DeferedParamSnippet;
import cn.sowell.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.position.dao.PositionDao;
import cn.sowell.datacenter.model.position.pojo.criteria.PositionCriteria;

@Repository
public class PositionDaoImpl implements PositionDao {
	
	@Resource
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getPositionList(PositionCriteria positionCriteria, PageInfo pageInfo) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from Position @mainWhere";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		DeferedParamSnippet mainWhere = dQuery.createConditionSnippet("mainWhere");
		if(positionCriteria.getCode() != null) {
			mainWhere.append(" and code=:code");
			dQuery.setParam("code", positionCriteria.getCode());
		}
		if(TextUtils.hasText(positionCriteria.getName())) {
			mainWhere.append(" and name like  :name");
			dQuery.setParam("name", "%" + positionCriteria.getName() + "%");
		}
		if(TextUtils.hasText(positionCriteria.getAlias())) {
			mainWhere.append(" and alias like :alias");
			dQuery.setParam("alias", "%" + positionCriteria.getAlias() + "%");
		}
		Query countQuery = dQuery.createQuery(session, false, new WrapForCountFunction());
		pageInfo.setCount(FormatUtils.toInteger(countQuery.uniqueResult()));
		if(pageInfo.getCount() > 0) {
			Query query = dQuery.createQuery(session, false, null);
			QueryUtils.setPagingParamWithCriteria(query, pageInfo);
			return query.list();
		}
		return new ArrayList<Position>();
	}

	@Override
	public void save(Position position) {
		Session session = sessionFactory.getCurrentSession();
		session.save(position);
	}

	@Override
	public void delete(Position position) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(position);
	}

	@Override
	public Position query(Long positionId) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(Position.class, positionId);
	}

	@Override
	public void update(Position position) {
		Session session = sessionFactory.getCurrentSession();
		session.update(position);;
	}

}
