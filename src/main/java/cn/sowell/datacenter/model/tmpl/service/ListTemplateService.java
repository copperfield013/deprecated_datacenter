package cn.sowell.datacenter.model.tmpl.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
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
