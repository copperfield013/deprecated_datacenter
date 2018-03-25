package cn.sowell.datacenter.model.modules;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RecordEntity;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfig;
import cn.sowell.datacenter.model.abc.resolver.PropertyNamePartitions;

public class EntityPropertyParser{

	private Entity entity;
	private Map<String, FieldParserDescription> fieldMap;
	private List<ErrorInfomation> errors;
	private FusionContextConfig config;
	
	
	
	public EntityPropertyParser(FusionContextConfig config, Entity entity, Set<FieldParserDescription> fieldMap) {
		Assert.notNull(entity);
		Assert.notNull(fieldMap);
		Assert.notNull(config);
		this.entity = entity;
		this.fieldMap = CollectionUtils.toMap(fieldMap, desc->desc.getFullKey());
		this.config = config;
	}
	
	
	
	
	public String getCode() {
		return this.getId();
	}
	
	public String getId() {
		return (String) getProperty(config.getCodeAttributeName());
	}
	
	public String getTitle() {
		return (String) getProperty(config.getTitleAttributeName());
	}
	
	public Map<String, Object> getMap(){
		return this.propertyGetMap;
	}
	
	public Object getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}
	
	public Object getProperty(String propertyName, String propType) {
		String[] names = TextUtils.splitToArray(propertyName, "\\.");
		Entity thisEntity = entity;
		for (int i = 0; i < names.length - 1; i++) {
			PropertyNamePartitions namePartitions = new PropertyNamePartitions(names[i]);
			List<RecordEntity> records = thisEntity.getRelations(namePartitions.getMainPartition());
			Integer entityIndex = FormatUtils.coalesce(namePartitions.getIndex(), 0);
			thisEntity = records.get(entityIndex).getEntity();
		}
		if(propType == null) {
			FieldParserDescription field = getPropertyField(propertyName);
			if(field != null) {
				propType = field.getAbcType();
			}else {
				propType = "string";
			}
		}
		return thisEntity.getTypeValue(names[names.length - 1], propType);
	}


	private FieldParserDescription getPropertyField(String propertyName) {
		String replacedName = propertyName.replaceAll("\\[\\d+\\]", "");
		return fieldMap.get(replacedName);
	}

	
	public List<ErrorInfomation> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorInfomation> errors) {
		this.errors = errors;
	}
	
	private Map<String, Object> propertyGetMap = new Map<String, Object>(){
		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<Entry<String, Object>> entrySet() {
			return new HashSet<Entry<String, Object>>();
		}
	};

}
