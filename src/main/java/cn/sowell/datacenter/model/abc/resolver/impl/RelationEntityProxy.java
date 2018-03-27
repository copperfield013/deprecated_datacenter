package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.HashSet;
import java.util.Set;

import com.abc.mapping.entity.Entity;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.datacenter.model.abc.resolver.EntityProxy;

public class RelationEntityProxy extends EntitiesContainedEntityProxy{
	private static final Object LABEL_KEY = "$$label$$";
	private String label;
	private Entity sourceEntity;
	
	public RelationEntityProxy(Entity sourceEntity) {
		super();
		Assert.notNull(sourceEntity);
		this.sourceEntity = sourceEntity;
	}

	@Override
	public void putValue(String propName, Object val) {
		sourceEntity.putValue(propName, val);
	}

	@Override
	protected Entity getSourceEntity() {
		return sourceEntity;
	}
	
	Set<RelationEntityProxy> copyEntities = new HashSet<>();
	@Override
	public EntityProxy createEmptyEntity() {
		Entity entity = new Entity(sourceEntity.getName());
		RelationEntityProxy proxy = new RelationEntityProxy(entity);
		proxy.setLabel(this.label);
		copyEntities.add(proxy);
		return proxy;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		copyEntities.forEach(proxy->proxy.setLabel(label));
	}

	@Override
	public boolean preprocessValue(String propName, Object propValue) {
		super.preprocessValue(propName, propValue);
		if(LABEL_KEY.equals(propName)) {
			this.setLabel(String.valueOf(propValue));
			return false;
		}
		return true;
	}
	
}