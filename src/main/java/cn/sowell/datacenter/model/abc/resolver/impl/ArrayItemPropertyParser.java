package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import cn.sowell.datacenter.model.abc.resolver.FieldParserDescription;
import cn.sowell.datacenter.model.abc.resolver.ModuleEntityPropertyParser;

public class ArrayItemPropertyParser extends AbstractEntityPropertyParser{

	
	private final ModuleEntityPropertyParser moduleParser;
	private final String compositeName;
	private final int itemIndex;

	public ArrayItemPropertyParser(ModuleEntityPropertyParser moduleParser, String compositeName, 
			int itemIndex,
			Map<String, FieldParserDescription> fieldMap) {
		super(fieldMap);
		Assert.notNull(moduleParser);
		Assert.hasText(compositeName);
		this.moduleParser = moduleParser;
		this.compositeName = compositeName;
		this.itemIndex = itemIndex;
	}

	/**
	 * 当propertyName不以compositeName开头时，则强制添加compositeName
	 */
	@Override
	public Object getProperty(String propertyName, String propType) {
		return moduleParser.getProperty(getFieldName(propertyName), propType);
	}
	
	static Pattern pattern = Pattern.compile("^\\[\\d+\\](.+)$");
	private String getFieldName(String propertyName) {
		if(propertyName.startsWith(compositeName)) {
			String suffix = propertyName.substring(compositeName.length());
			Matcher matcher = pattern.matcher(suffix);
			if(matcher.matches()) {
				propertyName = compositeName + matcher.group(1);
			}
		}else {
			propertyName = compositeName + "." + propertyName;
		}
		FieldParserDescription fieldDesc = fieldMap.get(propertyName);
		if(fieldDesc != null) {
			return fieldDesc.getArrayFieldNameFormat(this.itemIndex);
		}else {
			return compositeName + propertyName;
		}
	}
	
	/*
	public String getFieldName(String propertyName) {
		return getFieldNameFormat(propertyName).replace(INDEX_REPLACEMENT, String.valueOf(this.itemIndex));
	}
	
	private static final String INDEX_REPLACEMENT = "ARRAY_INDEX_REPLACEMENT";
	public String getFieldNameFormat(String propertyName) {
		String suffix = propertyName;
		if(propertyName.startsWith(compositeName)) {
			suffix = propertyName.substring(compositeName.length());
			if(!suffix.matches("^\\[\\d+\\].+$")) {
				suffix = "[" + INDEX_REPLACEMENT + "]" + suffix;
			}
		}
		return compositeName + suffix;
	}
	
	private GetonlyMap<String, String> fieldNameMap = new GetonlyMap<String, String>() {

		@Override
		public String get(Object key) {
			return getFieldName((String) key);
		}
	};
	
	public Map<String, String> getFieldName(){
		return fieldNameMap;
	}
	
	private GetonlyMap<String, String> fieldNameFormatMap = new GetonlyMap<String, String>() {

		@Override
		public String get(Object key) {
			return getFieldNameFormat((String) key);
		}
	};
	
	public Map<String, String> getFieldNameFormat(){
		return fieldNameFormatMap;
	}
*/

	

}
