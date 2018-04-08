package cn.sowell.datacenter.model.abc.resolver;

import java.util.Map;

import com.abc.mapping.entity.Entity;

public interface FusionContextConfigResolver {
	/**
	 * 根据实体属性的name-value创建实体对象，当属性没有配置时，<b>将会报错</b>
	 * @param map
	 * @return
	 */
	Entity createEntity(Map<String, Object> map);
	/**
	 * 根据实体属性的name-value创建实体对象，当属性没有配置时，<b>不会报错</b>
	 * @param map
	 * @return
	 */
	Entity createEntityIgnoreUnsupportedElement(Map<String, Object> map);
	
	/**
	 * 创建实体的字段解析器对象
	 * @param entity
	 * @return
	 */
	ModuleEntityPropertyParser createParser(Entity entity);
	
	/**
	 * 
	 * @param fieldId
	 * @return
	 */
	FieldParserDescription getFieldParserDescription(Long fieldId);
	
}
