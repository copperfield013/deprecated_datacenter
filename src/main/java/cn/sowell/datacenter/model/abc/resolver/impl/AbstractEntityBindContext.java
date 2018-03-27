package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.abc.resolver.EntityBindContext;
import cn.sowell.datacenter.model.abc.resolver.EntityElement;
import cn.sowell.datacenter.model.abc.resolver.EntityProxy;
import cn.sowell.datacenter.model.abc.resolver.PropertyTranslator;
import cn.sowell.datacenter.model.abc.resolver.translator.StringDateTranslator;
import cn.sowell.datacenter.model.abc.resolver.translator.StringFloatTranslator;
import cn.sowell.datacenter.model.abc.resolver.translator.StringIntTranslator;

public abstract class AbstractEntityBindContext implements EntityBindContext {
	protected EntityProxy entity;
	
	
	
	public AbstractEntityBindContext(EntityProxy entity) {
		super();
		this.entity = entity;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object transfer(Object propValue, String dataType) {
		PropertyTranslator translator = getTranslator(propValue, dataType);
		if(translator != null) {
			return translator.transfer(propValue);
		}else {
			return propValue;
		}
	}
	
	@Override
	public EntityProxy getEntity() {
		return entity;
	}
	@SuppressWarnings("rawtypes")
	static Map<PropertyTranslator, String> tranlastorMap = new HashMap<>();
	static {
		tranlastorMap.put(new StringDateTranslator(), "date");
		tranlastorMap.put(new StringIntTranslator(), "int");
		tranlastorMap.put(new StringFloatTranslator(), "float");
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static PropertyTranslator getTranslator(Object propValue, String dataType) {
		for (Entry<PropertyTranslator, String> entry : tranlastorMap.entrySet()) {
			if(entry.getValue().equalsIgnoreCase(dataType)
				&& entry.getKey().check(propValue)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	@Override
	public Object getValue(String propName, String abcAttr) {
		return getEntity().getEntity().getTypeValue(propName, abcAttr);
	}
	
	@Override
	public void setValue(String propName, Object propValue) {
		if(entity.preprocessValue(propName, propValue)) {
			EntityElement eElement = getEntityElement(propName);
			if(filterEntityElement(eElement, propValue)) {
				if(eElement instanceof EntityAttrElement) {
					Object val = transfer(propValue, ((EntityAttrElement) eElement).getDataType());
					entity.putValue(propName, val);
				}else if(eElement instanceof EntityLabelElement) {
					String val = FormatUtils.toString(propValue);
					if(((EntityLabelElement) eElement).getSubdomain().contains(val)) {
						entity.putValue(propName, propValue);
					}
				}
			}
		}
		
	}

	private boolean filterEntityElement(EntityElement eElement, Object propValue) {
		if("唯一编码".equals(eElement.getAbcattr()) && propValue instanceof String && !TextUtils.hasText((String) propValue)) {
			return false;
		}
		return true;
	}


	protected boolean prevSetValue(String propName, Object propValue) {
		return false;
	}


	/**
	 * 根据直接属性名获得该属性的节点信息
	 * @param propName
	 * @return
	 */
	protected abstract EntityElement getEntityElement(String propName);
}
