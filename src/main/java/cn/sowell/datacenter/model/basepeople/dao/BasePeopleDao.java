package cn.sowell.datacenter.model.basepeople.dao;

import java.util.List;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.People;

public interface BasePeopleDao {
	/**
	 * 从数据库中根据条件分页查询basepeople列表
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<People> queryList(BasePeopleCriteria criteria, PageInfo pageInfo);
	
	/**
	 * 对象插入到数据表中
	 * @param pojo
	 */
	void insert(Object pojo);

	/**
	 * 从数据库中查找对应的pajo对象
	 * @param clazz
	 * @param id
	 * @return
	 */
	<T> T get(Class<T> clazz, Long id);

	/**
	 * 更新一个pojo对象
	 * @param pojo
	 */
	void update(Object pojo);

	/**
	 * 从数据库中删除一个对象
	 * @param pojo
	 */
	void delete(Object pojo);
}
