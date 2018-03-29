package cn.sowell.datacenter.model.tmpl.dao;

import java.util.List;

import cn.sowell.datacenter.model.tmpl.pojo.TemplateGroup;

public interface TempalteGroupDao {

	List<TemplateGroup> queryGroups(String module);

	TemplateGroup getGroup(Long groupId);

	TemplateGroup getTemplateGroup(String module, String templateGroupKey);

}
