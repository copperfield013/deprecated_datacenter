package cn.sowell.datacenter.model.admin.service;

import java.io.Serializable;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;

public interface SystemAdminService {

	/**
	 * 将模板设置为用户的默认模板
	 * @param tmplId
	 * @param user
	 */
	void setTmplAsDefault(Long tmplId, UserIdentifier user);

	/**
	 * 将列表模板设置为用户的默认模板
	 * @param ltmplId
	 * @param currentUser
	 */
	void setListTemplateAsDefault(Long ltmplId, UserIdentifier currentUser);
	
	/**
	 * 获得用户在某个模块某个类型的默认模板
	 * @param userId 用户id
	 * @param module 模块名({@linkplain DataCenterConstants#TEMPLATE_MODULE_PEOPLE people}, 
	 * {@linkplain DataCenterConstants#TEMPLATE_MODULE_ADDRESS address})
	 * @param type 模板类型({@linkplain DataCenterConstants#TEMPLATE_TYPE_LIST list}, 
	 * {@linkplain DataCenterConstants#TEMPLATE_TYPE_DETAIL detail})
	 * @return
	 */
	Long getDefaultTemplateId(long adminId, String module, String type);

	SystemAdmin getSystemAdminByUserId(long userId);

	

}
