package cn.sowell.datacenter.model.common.utils;

import java.util.HashMap;

public enum EntityPropertyType {
	Select("select"),
	Text("text"),
	Integer("int"),
	Date("date");

	private static final HashMap<String, EntityPropertyType> fieldTypeEntityPropertyTypeMap = new HashMap<String, EntityPropertyType>();
	
	static{
		fieldTypeEntityPropertyTypeMap.put("select", Select);
		fieldTypeEntityPropertyTypeMap.put("text", Text);
		fieldTypeEntityPropertyTypeMap.put("int", Integer);
		fieldTypeEntityPropertyTypeMap.put("date", Date);
	}
	
	
	public static final EntityPropertyType getEntityPropertyTypeByFieldType(String fieldType){
		return fieldTypeEntityPropertyTypeMap.get(fieldType);
	}
	
	private String typeName;
	private EntityPropertyType(String typeName){
		this.typeName = typeName;
	}
	
	public String getTypeName() {
		return this.typeName;
	}

}
