package cn.sowell.datacenter.model.tmpl.service;

import java.util.List;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.model.tmpl.pojo.AbstractTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateAdminDefaultTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;

public interface TemplateService {

	/**
	 * 根据详情模板id和获得详情模板对象
	 * @param tmplId
	 * @return
	 */
	TemplateDetailTemplate getDetailTemplate(long tmplId);
	
	/**
	 * 根据模板id获得列表模板对象
	 * @param tmplId
	 * @return
	 */
	TemplateListTempalte getListTemplate(long tmplId);
	
	/**
	 * 获得用户在某个模块某个类型的默认模板
	 * @param userId 用户id
	 * @param module 模块名({@linkplain DataCenterConstants#TEMPLATE_MODULE_PEOPLE people}, 
	 * {@linkplain DataCenterConstants#TEMPLATE_MODULE_ADDRESS address})
	 * @param type 模板类型({@linkplain DataCenterConstants#TEMPLATE_TYPE_LIST list}, 
	 * {@linkplain DataCenterConstants#TEMPLATE_TYPE_DETAIL detail})
	 * @return
	 */
	TemplateAdminDefaultTemplate getAdminDefaultTemplate(long adminId, String module, String type);
	
	/**
	 * 根据用户和模块获得对应的默认详情模板对象
	 * @param user
	 * @param module
	 * @return
	 */
	TemplateDetailTemplate getDefaultDetailTemplate(UserIdentifier user, String module);
	
	
	/**
	 * 根据用户和模块获得对应的默认列表模板对象
	 * @param user
	 * @param module
	 * @return
	 */
	TemplateListTempalte getDefaultListTemplate(UserIdentifier user, String module);

	/**
	 * 移除列表模板
	 * @param user
	 * @param tmplId
	 */
	void removeTemplate(UserIdentifier user, Long tmplId);
	
	/**
	 * 将某个详情模板设置为用户的默认
	 * @param tmplId
	 * @param user
	 */
	void setTemplateAsDefault(UserIdentifier user, long tmplId);

	/**
	 * 创建或者更新详情模板
	 * @param data
	 */
	public <T extends AbstractTemplate> void mergeTemplate(T template);

	/**
	 * 
	 * @param module
	 * @param user
	 * @param pageInfo
	 * @param loadDetail
	 * @return
	 */
	List<TemplateDetailTemplate> getAllDetailTemplateList(String module,
			UserIdentifier user,
			PageInfo pageInfo,
			boolean loadDetail);

	/**
	 * 根据模板id获得模板对象。该方法适用于在不知道模板的类型的前提下使用。
	 * 如果已经知道模板的类型，请使用对应的方法{@link #getDetailTemplate(long)}或{@link #getListTemplate(long)}
	 * @param tmplId
	 * @return
	 */
	AbstractTemplate getTemplate(long tmplId);

}
