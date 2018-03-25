package cn.sowell.datacenter.model.tmpl.service;

import java.util.List;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;

public interface ListTemplateService {

	/**
	 * 
	 * @param module 
	 * @param user
	 * @return
	 */
	List<TemplateListTempalte> queryLtmplList(String module, UserIdentifier user);

}
