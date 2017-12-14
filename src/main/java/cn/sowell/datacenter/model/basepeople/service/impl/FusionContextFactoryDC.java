package cn.sowell.datacenter.model.basepeople.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.abc.application.FusionContext;

public class FusionContextFactoryDC {
	public static final String KEY_BASE = "base";
	public static final String KEY_IMPORT_BASE = "importBase";
	public static final String KEY_IMPORT_HANDICAPPED = "importHandicapped";
	public static final String KEY_IMPORT_LOWINCOME = "importLowincome";
	public static final String KEY_IMPORT_FAMILYPLANNING = "importFamilyPlanning";
	
	
	private Map<String, FusionContextConfig> configMap = new HashMap<String, FusionContextConfig>();
	
	public FusionContextConfig getConfig(String configName){
		return configMap.get(configName);
	}
	
	public FusionContext getContext(String configName){
		if(configMap.containsKey(configName)){
			return getContext(configMap.get(configName));
		}else{
			throw new RuntimeException("没有配置FusitionContextConfig[name=" + configName + "]");
		}
	}
	
	public FusionContext getContext(FusionContextConfig config){
		FusionContext context = new FusionContext();
		context.setMappingName(config.getMappingName());
		context.setDictionaryMappingName(config.getDictionaryMappingName());
		return context;
	}
	public Map<String, FusionContextConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<String, FusionContextConfig> configMap) {
		this.configMap = configMap;
	}
}
