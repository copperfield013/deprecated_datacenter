package cn.sowell.datacenter.model.abc.resolver.impl;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.abc.dto.ErrorInfomation;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.abc.resolver.EntityBindContext;
import cn.sowell.datacenter.model.abc.resolver.EntityPropertyParser;
import cn.sowell.datacenter.model.abc.resolver.FieldParserDescription;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfig;
import cn.sowell.datacenter.model.abc.resolver.PropertyNamePartitions;

class CommonEntityPropertyParser implements EntityPropertyParser{

	private Map<String, FieldParserDescription> fieldMap;
	private List<ErrorInfomation> errors;
	private FusionContextConfig config;
	
	private EntityBindContext context;
	
	
	CommonEntityPropertyParser(FusionContextConfig config, EntityBindContext context, Set<FieldParserDescription> fieldMap) {
		Assert.notNull(fieldMap);
		Assert.notNull(config);
		Assert.notNull(context);
		Assert.notNull(context.getEntity());
		this.context = context;
		this.fieldMap = CollectionUtils.toMap(fieldMap, desc->desc.getFullKey());
		this.config = config;
	}
	
	
	
	@Override
	public String getCode() {
		return this.getId();
	}
	
	@Override
	public String getId() {
		return (String) getProperty(config.getCodeAttributeName());
	}
	
	@Override
	public String getTitle() {
		return (String) getProperty(config.getTitleAttributeName());
	}
	
	@Override
	public Map<String, Object> getMap(){
		return this.propertyGetMap;
	}
	
	@Override
	public Object getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}
	
	@Override
	public Object getProperty(String propertyName, String propType) {
		String[] names = TextUtils.splitToArray(propertyName, "\\.");
		
		EntityBindContext thisContext = this.context;
		for (int i = 0; i < names.length - 1; i++) {
			PropertyNamePartitions namePartitions = new PropertyNamePartitions(names[i]);
			thisContext = thisContext.getElement(namePartitions);
		}
		if(propType == null) {
			FieldParserDescription field = getPropertyField(propertyName);
			if(field != null) {
				propType = field.getAbcType();
			}else {
				propType = "string";
			}
		}
		return thisContext.getValue(names[names.length - 1], propType);
	}


	private FieldParserDescription getPropertyField(String propertyName) {
		String replacedName = propertyName.replaceAll("\\[\\d+\\]", "");
		return fieldMap.get(replacedName);
	}

	@Override
	public List<ErrorInfomation> getErrors() {
		return errors;
	}
	
	@Override
	public void setErrors(List<ErrorInfomation> errors) {
		this.errors = errors;
	}
	
	private Map<String, Object> propertyGetMap = new Map<String, Object>(){
		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean containsKey(Object key) {
			return fieldMap.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object get(Object key) {
			return getProperty((String) key);
		}

		@Override
		public Object put(String key, Object value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object remove(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void putAll(Map<? extends String, ? extends Object> m) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<String> keySet() {
			return fieldMap.keySet();
		}

		@Override
		public Collection<Object> values() {
			return null;
		}

		@Override
		public Set<Entry<String, Object>> entrySet() {
			return new HashSet<Entry<String, Object>>();
		}
	};

}
