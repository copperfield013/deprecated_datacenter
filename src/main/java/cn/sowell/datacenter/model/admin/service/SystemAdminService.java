package cn.sowell.datacenter.model.admin.service;

import java.io.Serializable;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;

public interface SystemAdminService {

	/**
	 * 将模板设置为用户的默认模板
	 * @param tmplId
	 * @param user
	 */
	void setTmplAsDefault(Long tmplId, UserIdentifier user);

	/**
	 * 
	 * @param serializable
	 * @return
	 */
	SystemAdmin getSystemAdminByUserId(Serializable serializable);

}
