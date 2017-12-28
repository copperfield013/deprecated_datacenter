package cn.sowell.datacenter.model.common.dao;

import java.util.Set;

public interface NormalOperateDao {
	Long save(Object pojo);
	void update(Object pojo);
	<T> T get(Class<T> pojoClass, Long id);
	int remove(Class<?> pojoClass, Set<Long> pojoIds);
	void remove(Object pojo);
	
}
