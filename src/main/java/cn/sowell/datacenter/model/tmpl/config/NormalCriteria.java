package cn.sowell.datacenter.model.tmpl.config;

import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;

public class NormalCriteria {
	private TemplateListCriteria criteria;
	private String value;
	private String attributeName;
	
	public NormalCriteria(TemplateListCriteria criteria) {
		super();
		this.criteria = criteria;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public TemplateListCriteria getCriteria() {
		return criteria;
	}
}
