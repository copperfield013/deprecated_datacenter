package cn.sowell.datacenter.model.config.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.sowell.copframe.spring.properties.PropertyPlaceholder;
import cn.sowell.datacenter.entityResolver.FusionContextConfig;
import cn.sowell.datacenter.entityResolver.FusionContextConfigFactory;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.datacenter.model.config.service.ConfigUserService;
import cn.sowell.datacenter.model.config.service.NonAuthorityException;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Service
public class ConfigUserServiceImpl implements ConfigUserService {

	@Resource
	FusionContextConfigFactory fFactory;
	
	@Resource
	ModulesService mService;
	
	@Resource
	TemplateService tService;
	
	@Override
	public ModuleEntityPropertyParser getUserEntity(ABCUser user, Long historyId) {
		String moduleName = getUserModuleName();
		String code = user.getCode();
		
		
		ModuleEntityPropertyParser entity = null;
		EntityHistoryItem lastHistory = mService.getLastHistoryItem(moduleName, code, user);
		if(historyId != null) {
			if(lastHistory != null && !historyId.equals(lastHistory.getId())) {
				entity = mService.getHistoryEntityParser(moduleName, code, historyId, user);
			}
        }
        if(entity == null) {
        	entity = mService.getEntity(moduleName, code, null, user);
        }
        
        return entity;
		
		
	}

	private String getUserModuleName() {
		return PropertyPlaceholder.getProperty("user_module_name");
	}
	
	@Override
	public FusionContextConfig getUserModuleConfig() {
		String moduleName = getUserModuleName();
		return fFactory.getModuleConfig(moduleName);
	}
	
	
	@Override
	public TemplateDetailTemplate getUserDetailTemplate(Long tmplId) {
		String userModuleName = getUserModuleName();
		if(tmplId == null) {
			tmplId = Long.valueOf(PropertyPlaceholder.getProperty("default_user_dtmpl_id"));
		}
		TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplId);
		Assert.state(userModuleName.equals(dtmpl.getModule()), "详情模板[id=" + tmplId + ", moduleName=" + dtmpl.getModule() + "]不是用户信息详情模板[moduleName=" + userModuleName + "]");
		return dtmpl;
		
	}
	
	@Override
	public void validateUserAuthentication(String moduleName) {
		if(moduleName != null) {
			if(moduleName.equals(getUserModuleName())) {
				return;
			}
		}
		throw new NonAuthorityException("请求的moduleName[" + moduleName + "]与设置的用户的moduleName[" + getUserModuleName() + "]不相同，拒绝访问");
	}
	
	
	@Override
	public void mergeUserEntity(Map<String, Object> map, ABCUser operateUser) {
		//TODO: 验证操作用户的权限
		mService.mergeEntity(getUserModuleName(), map, operateUser);
		
	}

}
