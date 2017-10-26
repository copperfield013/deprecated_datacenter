package cn.sowell.datacenter.model.basepeople.service;

import java.util.List;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.dto.FieldDataDto;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;

import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleItemEntity;
import com.alibaba.fastjson.JSONArray;

public interface BasePeopleService {
	
	/**
	 * 根据条件对象查询分页
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<BasePeople> queryList(BasePeopleCriteria criteria, PageInfo pageInfo);
	
	/**
	 * 创建一个人口对象
	 * @param people
	 */
	void create(BasePeople people);
	
	BasePeople getPeopleById(Long id);
	
	/**
	 * 更新一个人口对象
	 * @param people
	 */
	void update(BasePeople people);
	
	/**
	 * 从数据库中删除一个demo对象
	 * @param id
	 */
	void delete(Long id);



	/**
	 * 使用ES 返回前端模糊查询字段 返回对象为Json
	 * @param title
	 * @return
	 */
	
	JSONArray titleSearchByEs(String title);


	List<FieldDataDto> queryFieldList(PageInfo pageInfo);

	void addField(FieldDataDto field);


	List<TBasePeopleItemEntity> FieldList(String field);

}
