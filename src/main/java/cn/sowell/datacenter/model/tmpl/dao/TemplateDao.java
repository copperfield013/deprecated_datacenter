package cn.sowell.datacenter.model.tmpl.dao;

import cn.sowell.datacenter.model.tmpl.pojo.TemplateAdminDefaultTemplate;

public interface TemplateDao {

	void setTemplateAsDefault(Long id, String module, String type, long tmplId);

	TemplateAdminDefaultTemplate getAdminDefaultTempalte(long adminId, String module, String type);
	
}
