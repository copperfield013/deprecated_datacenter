package cn.sowell.datacenter.model.specialposition.dao.impl;

import org.springframework.stereotype.Repository;

import cn.sowell.datacenter.model.specialposition.dao.SpecialPositionDao;

@Repository
public class SpecialPositionDaoImpl implements SpecialPositionDao {
	/*
	@Resource
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecialPosition> getSpecialPositionList(SpecialPositionCriteria specialPositionCriteria, PageInfo pageInfo) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SpecialPosition @mainWhere order by modifyTime desc";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		DeferedParamSnippet mainWhere = dQuery.createConditionSnippet("mainWhere");
		if(specialPositionCriteria.getSpecialPositionId() != null) {
			mainWhere.append(" and id=:id");
			dQuery.setParam("id", specialPositionCriteria.getSpecialPositionId());
		}
		if(TextUtils.hasText(specialPositionCriteria.getName())) {
			mainWhere.append(" and name like  :name");
			dQuery.setParam("name", "%" + specialPositionCriteria.getName() + "%");
		}
		if(TextUtils.hasText(specialPositionCriteria.getCommonName())) {
			mainWhere.append(" and commonName like :commonName");
			dQuery.setParam("commonName", "%" + specialPositionCriteria.getCommonName() + "%");
		}
		Query countQuery = dQuery.createQuery(session, false, new WrapForCountFunction());
		pageInfo.setCount(FormatUtils.toInteger(countQuery.uniqueResult()));
		if(pageInfo.getCount() > 0) {
			Query query = dQuery.createQuery(session, false, null);
			QueryUtils.setPagingParamWithCriteria(query, pageInfo);
			return query.list();
		}
		return new ArrayList<SpecialPosition>();
	}

	@Override
	public void save(SpecialPosition specialPosition) {
		Session session = sessionFactory.getCurrentSession();
		session.save(specialPosition);
	}

	@Override
	public void delete(SpecialPosition specialPosition) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(specialPosition);
	}

	@Override
	public SpecialPosition query(Long specialPositionId) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(SpecialPosition.class, specialPositionId);
	}

	@Override
	public void update(SpecialPosition specialPosition) {
		Session session = sessionFactory.getCurrentSession();
		session.update(specialPosition);;
	}
*/
}
