package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.abc.dto.ErrorInfomation;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.datacenter.model.abc.resolver.EntityPropertyParser;
import cn.sowell.datacenter.model.abc.resolver.FieldParserDescription;

abstract class AbstractEntityPropertyParser implements EntityPropertyParser {
	
	
	private List<ErrorInfomation> errors;
	protected Map<String, FieldParserDescription> fieldMap;

	
	
	public AbstractEntityPropertyParser(Map<String, FieldParserDescription> fieldMap) {
		super();
		Assert.notNull(fieldMap);
		this.fieldMap = fieldMap;
	}

	@Override
	public Map<String, Object> getPmap() {
		return propertyGetMap;
	}
	
	@Override
	public Map<String, String> getSmap() {
		return propertyFormatMap;
	}
	
	@Override
	public Object getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}
	
	@Override
	public String getFormatedProperty(String propertyName, String propType) {
		return getFormatedProperty(propertyName, propType, null);
	}



	@Override
	public String getFormatedProperty(String propertyName) {
		return getFormatedProperty(propertyName, null);
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
	
	private Map<String, String> propertyFormatMap = new Map<String, String>(){

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
		public String get(Object key) {
			return getFormatedProperty((String) key);
		}

		@Override
		public String put(String key, String value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String remove(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void putAll(Map<? extends String, ? extends String> m) {
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
		public Collection<String> values() {
			return null;
		}

		@Override
		public Set<Entry<String, String>> entrySet() {
			return new HashSet<Entry<String, String>>();
		}
		
	};
}
