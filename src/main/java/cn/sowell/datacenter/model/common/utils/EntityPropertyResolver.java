package cn.sowell.datacenter.model.common.utils;

import java.util.Map;

import com.abc.mapping.entity.Entity;

import cn.sowell.dataserver.model.dict.pojo.DictionaryField;

public class EntityPropertyResolver {
	private Entity entity;
	private Map<String, DictionaryField> fieldMap;
	
	public Object get(String propertyName, EntityPropertyType type){
		String typeName = EntityPropertyType.Text.getTypeName();
		if(type != null){
			typeName = type.getTypeName();
		}
		return entity.getTypeValue(propertyName, typeName);
	}
	
	public Object get(String propertyName){
		EntityPropertyType type = null;
		if(fieldMap != null){
			DictionaryField field = fieldMap.get(propertyName);
			if(field != null){
				String fieldType = field.getType();
				type = EntityPropertyType.getEntityPropertyTypeByFieldType(fieldType);
			}
		}
		return get(propertyName, type);
	}

	
	
	
}
