package cn.sowell.datacenter.model.config.service;

import java.util.Map;

import cn.sowell.datacenter.entityResolver.FusionContextConfig;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;

public interface ConfigUserService {

	ModuleEntityPropertyParser getUserEntity(ABCUser user, Long historyId);

	TemplateDetailTemplate getUserDetailTemplate(Long tmplId);

	void validateUserAuthentication(String moduleName);

	void mergeUserEntity(Map<String, Object> map, ABCUser operateUser);

	FusionContextConfig getUserModuleConfig();

}
