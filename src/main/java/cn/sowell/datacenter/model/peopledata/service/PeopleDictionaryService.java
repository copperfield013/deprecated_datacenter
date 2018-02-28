package cn.sowell.datacenter.model.peopledata.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.pojo.OptionItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;


public interface PeopleDictionaryService {

	
	List<PeopleCompositeDictionaryItem> getAllInfo(String code);

	/**
	 * 保存模板
	 * @param data
	 */
	void mergeTemplate(TemplateDetailTemplate data);
	/**
	 * 获得当前用户的默认模板
	 * @param user 用户对象
	 * @param module 模块名({@linkplain DataCenterConstants#TEMPLATE_MODULE_PEOPLE people}, 
	 * {@linkplain DataCenterConstants#TEMPLATE_MODULE_ADDRESS address})
	 * @param type 模板类型({@linkplain DataCenterConstants#TEMPLATE_TYPE_LIST list}, 
	 * {@linkplain DataCenterConstants#TEMPLATE_TYPE_DETAIL detail})
	 * @return
	 */
	public TemplateDetailTemplate getDefaultTemplate(UserIdentifier user, String module, String type);

	/**
	 * 根据模板id获得模板
	 * @param tmplId
	 * @return
	 */
	TemplateDetailTemplate getTemplate(Long tmplId);

	/**
	 * 加载所有模板数据
	 * @param module 模块
	 * @param user
	 * @param pageInfo 分页数据
	 * @param loadDetail 是否加载模板的详情数据
	 * @return
	 */
	List<TemplateDetailTemplate> getAllTemplateList(String module,
			UserIdentifier user, PageInfo pageInfo, boolean loadDetail);

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

	Map<Long, List<OptionItem>> getOptionsMap(Set<Long> fieldIds);


}
