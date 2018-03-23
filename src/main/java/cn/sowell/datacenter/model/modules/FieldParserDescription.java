package cn.sowell.datacenter.model.modules;

import cn.sowell.datacenter.model.dict.pojo.DictionaryField;

public class FieldParserDescription {
	private DictionaryField field;
	
	public FieldParserDescription(DictionaryField field) {
		super();
		this.field = field;
	}

	public Long getFieldId() {
		return field.getId();
	}
	
	public String getFieldTitle() {
		return field.getTitle();
	}
	
	public String getAbcType() {
		return field.getAbcType();
	}
	
	public String getFullKey() {
		return field.getFullKey();
	}
	
	
	
}
