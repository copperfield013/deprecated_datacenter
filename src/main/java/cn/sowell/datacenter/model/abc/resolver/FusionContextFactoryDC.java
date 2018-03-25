package cn.sowell.datacenter.model.abc.resolver;

import java.util.HashMap;
import java.util.Map;

import com.abc.application.BizFusionContext;

import cn.sowell.datacenter.DataCenterConstants;

public class FusionContextFactoryDC {
	public static final String KEY_BASE = "base";
	public static final String KEY_IMPORT_BASE = "importBase";
	public static final String KEY_IMPORT_HANDICAPPED = "importHandicapped";
	public static final String KEY_IMPORT_LOWINCOME = "importLowincome";
	public static final String KEY_IMPORT_FAMILYPLANNING = "importFamilyPlanning";
	public static final String KEY_ADDRESS_BASE = "addressBase";
	
	
	private Map<String, FusionContextConfig> configMap = new HashMap<String, FusionContextConfig>();
	
	private Map<String, String> defaultModuleEntityConfigMap = new HashMap<String, String>();
	
	public FusionContextFactoryDC() {
		defaultModuleEntityConfigMap.put(DataCenterConstants.MODULE_KEY_PEOPLE, KEY_BASE);
		defaultModuleEntityConfigMap.put(DataCenterConstants.MODULE_KEY_ADDRESS, KEY_ADDRESS_BASE);
	}
	
	public FusionContextConfig getConfig(String configName){
		return configMap.get(configName);
	}
	
	public BizFusionContext getContext(String configName){
		if(configMap.containsKey(configName)){
			return getContext(configMap.get(configName));
		}else{
			throw new RuntimeException("没有配置FusitionContextConfig[name=" + configName + "]");
		}
	}
	
	public BizFusionContext getContext(FusionContextConfig config){
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(config.getMappingName());
		return context;
	}
	public Map<String, FusionContextConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<String, FusionContextConfig> configMap) {
		this.configMap = configMap;
	}

	public String mapDefaultModuleEntityConfig(String module) {
		return defaultModuleEntityConfigMap.get(module);
	}

	public FusionContextConfig getDefaultConfig(String module) {
		return getConfig(mapDefaultModuleEntityConfig(module));
	}
}
