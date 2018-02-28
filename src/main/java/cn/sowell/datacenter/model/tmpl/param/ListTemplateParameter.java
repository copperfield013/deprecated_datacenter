package cn.sowell.datacenter.model.tmpl.param;

import java.util.Map;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;

public class ListTemplateParameter {
	private TemplateListTmpl listTemplate;
	private Map<Long, NormalCriteria> normalCriteriaMap;
	private UserIdentifier user;
	public TemplateListTmpl getListTemplate() {
		return listTemplate;
	}
	public void setListTemplate(TemplateListTmpl listTemplate) {
		this.listTemplate = listTemplate;
	}
	public Map<Long, NormalCriteria> getNormalCriteriaMap() {
		return normalCriteriaMap;
	}
	public void setNormalCriteriaMap(Map<Long, NormalCriteria> normalCriteriaMap) {
		this.normalCriteriaMap = normalCriteriaMap;
	}
	public UserIdentifier getUser() {
		return user;
	}
	public void setUser(UserIdentifier user) {
		this.user = user;
	}
}
