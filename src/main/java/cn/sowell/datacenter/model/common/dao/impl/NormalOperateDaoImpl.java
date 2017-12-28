package cn.sowell.datacenter.model.common.dao.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import cn.sowell.datacenter.model.common.dao.NormalOperateDao;

@Repository
public class NormalOperateDaoImpl implements NormalOperateDao{

	@Resource
	SessionFactory sFactory;
	
	@Override
	public Long save(Object pojo) {
		return (Long) sFactory.getCurrentSession().save(pojo);
	}

	@Override
	public void update(Object pojo) {
		sFactory.getCurrentSession().update(pojo);
	}

	@Override
	public <T> T get(Class<T> pojoClass, Long id) {
		return sFactory.getCurrentSession().get(pojoClass, id);
	}
	
	@Override
	public int remove(Class<?> pojoClass,
			Set<Long> pojoId) {
		if(pojoClass != null && pojoId != null && !pojoId.isEmpty()){
			String hql = "delete from " + pojoClass.getName() + " p where p.id in (:ids)";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("ids", pojoId, StandardBasicTypes.LONG);
			return query.executeUpdate();
		}
		return 0;
	}
	
	@Override
	public void remove(Object pojo){
		sFactory.getCurrentSession().delete(pojo);
	}
	
}
