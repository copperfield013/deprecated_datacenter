package cn.sowell.datacenter.model.abc.resolver;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import cn.sowell.datacenter.model.abc.resolver.impl.ABCNodeFusionContextConfigResolver;
import cn.sowell.datacenter.model.dict.service.DictionaryService;

public class FusionContextConfig implements InitializingBean{
	private String mappingName;
	private String module;
	private String codeAttributeName = "code";
	private String titleAttributeName = "name";
	private FusionContextConfigResolver configResolver;
	@Resource
	DictionaryService dictService;
	
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
		if(dictService != null) {
			resolver.setFields(dictService.getDynamicFieldDescriptionSet(this.module));
		}
		this.configResolver = resolver;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
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
	public void setDictService(DictionaryService dictService) {
		this.dictService = dictService;
	}
	
}
