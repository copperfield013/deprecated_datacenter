package cn.sowell.datacenter.model.peopledata.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.pojo.OptionItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleFieldDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateData;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateField;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateGroup;

public interface PeopleDictionaryDao {

	/**
	 * 根据条件查找所有人口条线（不包含字段）
	 */
	List<PeopleCompositeDictionaryItem> queryAllInfo(String code);

	/**
	 * 根据条线id获得对应的所有字段
	 * @param list
	 * @return
	 */
	List<PeopleFieldDictionaryItem> queryAllField(Set<Long> list);

	/**
	 * 根据模板id获得所有字段组（经过排序）
	 * @param tmplId
	 * @return
	 */
	List<PeopleTemplateGroup> getTemplateGroups(Long tmplId);

	/**
	 * 根据字段组的id集合获得对应的所有字段（经过排序）
	 * @param groupIdSet
	 * @return
	 */
	Map<Long, List<PeopleTemplateField>> getTemplateFieldsMap(
			Set<Long> groupIdSet);

	/**
	 * 获得所有模板
	 * @param user
	 * @param pageInfo 
	 * @return
	 */
	List<PeopleTemplateData> getTemplateList(UserIdentifier user, PageInfo pageInfo);

	/**
	 * 根据字段id集合获得所有字段映射
	 * @param fieldIds
	 * @return
	 */
	Map<Long, PeopleFieldDictionaryItem> getFieldMap(Set<Long> fieldIds);

	/**
	 * 从数据库中删除一个模板记录
	 * @param tmplId
	 * @return
	 */
	boolean removeTemplate(Long tmplId);

	/**
	 * 
	 * @return
	 */
	List<DictionaryOption> getAllEnumList();

	/**
	 * 根据字段id获得对应的所有选项
	 * @param fieldIds
	 * @return
	 */
	Map<Long, List<OptionItem>> getFieldOptionsMap(Set<Long> fieldIds);


}
