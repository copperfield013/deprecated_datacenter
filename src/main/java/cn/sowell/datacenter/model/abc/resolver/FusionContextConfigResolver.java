package cn.sowell.datacenter.model.abc.resolver;

import java.util.Map;

import com.abc.mapping.entity.Entity;

import cn.sowell.datacenter.model.modules.EntityPropertyParser;

public interface FusionContextConfigResolver {
	
	Entity createEntity(Map<String, Object> map);
	EntityPropertyParser createParser(Entity entity);
	
	
}
