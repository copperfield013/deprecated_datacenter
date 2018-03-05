package cn.sowell.datacenter.model.tmpl.strategy;

import cn.sowell.datacenter.model.tmpl.pojo.AbstractTemplate;

public interface TemplateUpdateStrategy<T extends AbstractTemplate> {

	void update(T template);

	void create(T template);
	
}
