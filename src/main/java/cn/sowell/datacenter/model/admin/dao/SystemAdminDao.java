package cn.sowell.datacenter.model.admin.dao;

import cn.sowell.datacenter.model.system.pojo.SystemAdmin;


public interface SystemAdminDao {

	void setTmplAsDefault(long tmplId, long userId);

	
	SystemAdmin getSystemAdminByUserId(long userId);

}
