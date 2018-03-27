package cn.sowell.datacenter.model.abc.resolver;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import cn.sowell.datacenter.model.abc.resolver.impl.ABCNodeFusionContextConfigResolver;
import cn.sowell.datacenter.model.dict.service.FieldObserverService;

public class FusionContextConfig implements InitializingBean{
	private String mappingName;
	private String module;
	private String codeAttributeName = "code";
	private String titleAttributeName = "name";
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
	public String getCodeAttributeName() {
		return codeAttributeName;
	}
	public void setCodeAttributeName(String codeAttributeName) {
		this.codeAttributeName = codeAttributeName;
	}
	public String getTitleAttributeName() {
		return titleAttributeName;
	}
	public void setTitleAttributeName(String titleAttributeName) {
		this.titleAttributeName = titleAttributeName;
	}
	
}