package cn.sowell.datacenter.model.basepeople.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.abc.application.FusitionContext;

public class FusitionContextFactoryDC {
	public static final String KEY_BASE = "base";
	public static final String KEY_IMPORT = "import";
	
	
	private Map<String, FusitionContextConfig> configMap = new HashMap<String, FusitionContextConfig>();
	
	public FusitionContextConfig getConfig(String configName){
		return configMap.get(configName);
	}
	
	public FusitionContext getContext(String configName){
		if(configMap.containsKey(configName)){
			return getContext(configMap.get(configName));
		}else{
			throw new RuntimeException("没有配置FusitionContextConfig[name=" + configName + "]");
		}
	}
	
	public FusitionContext getContext(FusitionContextConfig config){
		FusitionContext context = new FusitionContext();
		context.setMappingName(config.getMappingName());
		context.setDictionaryMappingName(config.getDictionaryMappingName());
		return context;
	}
	public Map<String, FusitionContextConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<String, FusitionContextConfig> configMap) {
		this.configMap = configMap;
	}
}
