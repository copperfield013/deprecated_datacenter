package cn.sowell.datacenter.model.basepeople.dao;

import java.sql.Savepoint;
import java.util.List;
import java.util.Map;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.BasePeopleDictionaryCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.*;

public interface BasePeopleDao {
	/**
	 * 从数据库中根据条件分页查询basepeople列表
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<BasePeople> queryList(BasePeopleCriteria criteria, PageInfo pageInfo);
	
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


	List<TBasePeopleItemEntity> fieldList (String field);

	List<TBasePeopleDictionaryEntity> querydicList(BasePeopleDictionaryCriteria bpCriteria, PageInfo pageInfo) ;

	void updateBasePeople(Map<String,String> map, String id);

	List<TBasePeopleDictionaryEntity> searchList(String txt) ;

	void saveOrUpdate (Object pojo);

	public <T> T getDicById(Class<T> clazz, Long id);


	/**
	 * 测试导出全国省市区数据
	 */

	List<CityEntiy> getbystatus (String status);


	/**
	 * 根据用户读取字段*/
	  List<TBasePeopleDictionaryEntity> dicListByUser();


	List<BasePeopleItem> dicItemByUser();
}
