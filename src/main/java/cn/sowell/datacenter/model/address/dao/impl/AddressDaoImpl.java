package cn.sowell.datacenter.model.address.dao.impl;

import org.springframework.stereotype.Repository;

@Repository
public class AddressDaoImpl/* implements AddressDao*/ {
/*
	@Resource
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SplitedAddressEntity> getAddressStrList(String addressStr, PageInfo pageInfo) {
		String hql = "from SplitedAddressEntity @mainWhere order by modifyTime desc";
		Session session = sessionFactory.getCurrentSession();
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		DeferedParamSnippet mainWhere = dQuery.createConditionSnippet("mainWhere");
		if(addressStr != null && !addressStr.equals("")) {
			mainWhere.append(" and name like :name");
			dQuery.setParam("name", "%" + addressStr + "%");
		}
		Query countQuery = dQuery.createQuery(session, false, new WrapForCountFunction());
		pageInfo.setCount(FormatUtils.toInteger(countQuery.uniqueResult()));
		if(pageInfo.getCount() > 0) {
			Query query = dQuery.createQuery(session, false, null);
			QueryUtils.setPagingParamWithCriteria(query, pageInfo);
			return query.list();
		}
		return new ArrayList<SplitedAddressEntity>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SplitedAddressEntity> getTheSameAddress(String addressCode, PageInfo pageInfo) {
		String hql = "from SplitedAddressEntity @mainWhere";
		Session session = sessionFactory.getCurrentSession();
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		DeferedParamSnippet mainWhere = dQuery.createConditionSnippet("mainWhere");
		if(addressCode != null && !addressCode.equals("")) {
			mainWhere.append(" and code = :code");
			dQuery.setParam("code", addressCode);
		}
		Query countQuery = dQuery.createQuery(session, false, new WrapForCountFunction());
		pageInfo.setCount(FormatUtils.toInteger(countQuery.uniqueResult()));
		if(pageInfo.getCount() > 0) {
			Query query = dQuery.createQuery(session, false, null);
			QueryUtils.setPagingParamWithCriteria(query, pageInfo);
			return query.list();
		}
		SQLQuery query = dQuery.createSQLQuery(session, false, null);
		query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(AddressEntity.class));
		return query.list();
		return new ArrayList<SplitedAddressEntity>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SplitedAddressEntity> getNotTheSameAddressList(String addressStr, String addressCode, PageInfo pageInfo) {
		String hql = "from SplitedAddressEntity @mainWhere order by modifyTime desc";
		Session session = sessionFactory.getCurrentSession();
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		DeferedParamSnippet mainWhere = dQuery.createConditionSnippet("mainWhere");
		if(addressStr != null && !addressStr.equals("")) {
			mainWhere.append(" and name like :name");
			dQuery.setParam("name", "%" + addressStr + "%");
		}
		if(addressCode != null && !addressCode.equals("")) {
			mainWhere.append(" and code <> :code");
			dQuery.setParam("code", addressCode);
		}
		Query countQuery = dQuery.createQuery(session, false, new WrapForCountFunction());
		pageInfo.setCount(FormatUtils.toInteger(countQuery.uniqueResult()));
		if(pageInfo.getCount() > 0) {
			Query query = dQuery.createQuery(session, false, null);
			QueryUtils.setPagingParamWithCriteria(query, pageInfo);
			return query.list();
		}
		return new ArrayList<SplitedAddressEntity>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AddressCode> getAddressCodeList() {
		String sql = "select ABP0002, ABC0918, ABC0917 from t_address_code";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(AddressCode.class));
		return query.list();
	}

	@Override
	public void delete(SplitedAddressEntity splitedAddressEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(splitedAddressEntity);
	}*/

}
