package cn.sowell.datacenter.model.admin.dao;

import cn.sowell.datacenter.model.system.pojo.SystemAdmin;


public interface SystemAdminDao {

	void setTmplAsDefault(long tmplId, long userId);

	void setListTmplAsDefault(Long ltmplId, long id);
	
	SystemAdmin getSystemAdminByUserId(long userId);

	Long getDefaultTemplateId(long userId, String module, String type);


	

}
