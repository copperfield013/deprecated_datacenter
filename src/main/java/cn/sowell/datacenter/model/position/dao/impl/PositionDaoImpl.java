package cn.sowell.datacenter.model.position.dao.impl;

import org.springframework.stereotype.Repository;

import cn.sowell.datacenter.model.position.dao.PositionDao;

@Repository
public class PositionDaoImpl implements PositionDao {
	/*
	@Resource
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getPositionList(PositionCriteria positionCriteria, PageInfo pageInfo) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from Position @mainWhere order by modifyTime desc";
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
*/
}
