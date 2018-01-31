package cn.sowell.datacenter.model.peopledata.service;

import java.util.List;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateData;


public interface PeopleDictionaryService {

	
	List<PeopleCompositeDictionaryItem> getAllInfo(String code);

	/**
	 * 保存模板
	 * @param data
	 */
	void mergeTemplate(PeopleTemplateData data);
	/**
	 * 获得当前用户的默认模板
	 * @param user
	 * @return
	 */
	PeopleTemplateData getDefaultTemplate(UserIdentifier user);

	/**
	 * 根据模板id获得模板
	 * @param tmplId
	 * @return
	 */
	PeopleTemplateData getTemplate(Long tmplId);

	/**
	 * 加载所有模板数据
	 * @param user
	 * @param pageInfo 分页数据
	 * @param loadDetail 是否加载模板的详情数据
	 * @return
	 */
	List<PeopleTemplateData> getAllTemplateList(UserIdentifier user, PageInfo pageInfo, boolean loadDetail);

	/**
	 * 删除一个模板
	 * @param tmplId
	 */
	void removeTemplate(UserIdentifier user, Long tmplId);

	/**
	 * 获取所有枚举对象
	 * @return
	 */
	List<DictionaryOption> getAllEnumList();


}
