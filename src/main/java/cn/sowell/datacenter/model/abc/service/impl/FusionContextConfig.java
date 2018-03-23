package cn.sowell.datacenter.model.abc.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import cn.sowell.datacenter.model.abc.resolver.FusionContextConfigResolver;
import cn.sowell.datacenter.model.abc.resolver.impl.ABCNodeFusionContextConfigResolver;
import cn.sowell.datacenter.model.dict.service.FieldObserverService;

public class FusionContextConfig implements InitializingBean{
	private String mappingName;
	private String module;
	private FusionContextConfigResolver configResolver;
	@Resource
	FieldObserverService foService;
	
	public String getMappingName() {
		return mappingName;
	}
	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}
	public FusionContextConfigResolver getConfigResolver() {
		return configResolver;
	}
	public void setConfigResolver(FusionContextConfigResolver configResolver) {
		this.configResolver = configResolver;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		ABCNodeFusionContextConfigResolver resolver = new ABCNodeFusionContextConfigResolver(this);
		if(foService != null) {
			resolver.setFieldSet(foService.getDynamicFieldDescriptionSet(this.module));
		}
		this.configResolver = resolver;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public void setFoService(FieldObserverService foService) {
		this.foService = foService;
	}
	
}
