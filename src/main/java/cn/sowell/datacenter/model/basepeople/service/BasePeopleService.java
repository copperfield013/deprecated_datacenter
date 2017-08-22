package cn.sowell.datacenter.model.basepeople.service;

import java.util.List;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.People;

public interface BasePeopleService {
	
	/**
	 * 根据条件对象查询分页
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<People> queryList(BasePeopleCriteria criteria, PageInfo pageInfo);
	
	/**
	 * 创建一个人口对象
	 * @param people
	 */
	void create(People people);
	
	People getPeople(Long id);
	
	/**
	 * 更新一个人口对象
	 * @param people
	 */
	void update(People people);
	
	/**
	 * 从数据库中删除一个demo对象
	 * @param id
	 */
	void delete(Long id);
}
