package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.Map;
import java.util.Set;

import com.abc.mapping.entity.Entity;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.datacenter.model.abc.resolver.EntityBindContext;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfigResolver;
import cn.sowell.datacenter.model.abc.service.impl.FusionContextConfig;
import cn.sowell.datacenter.model.modules.EntityPropertyParser;
import cn.sowell.datacenter.model.modules.FieldParserDescription;

public abstract class AbstractFusionContextConfigResolver implements FusionContextConfigResolver{
	protected FusionContextConfig config;
	private Set<FieldParserDescription> fieldSet;
	public AbstractFusionContextConfigResolver(FusionContextConfig config) {
		super();
		Assert.notNull(config);
		this.config = config;
	}
	
	protected abstract EntityBindContext buildRootContext(Entity entity);
	
	public synchronized void setFieldSet(Set<FieldParserDescription> fieldSet) {
		this.fieldSet = fieldSet;
	}
	
	
	@Override
	public Entity createEntity(Map<String, Object> map) {
		Entity entity = new Entity(config.getMappingName());
		EntityBindContext rootContext = buildRootContext(entity);
		if(rootContext != null) {
			map.forEach((propName, propValue)->{
				bindElement(rootContext, propName, propValue);
			});
			((EntitiesContainedEntityProxy)rootContext.getEntity()).commit();
			return entity;
		}
		return null;
	}
	
	

	private void bindElement(EntityBindContext context, String propName, Object propValue) {
		String[] split = propName.split("\\.", 2);
		String prefix = split[0];
		if(split.length == 1) {
			context.setValue(propName, propValue);
		}else {
			EntityBindContext elementContext = context.getElement(prefix);
			bindElement(elementContext, split[1], propValue);
		}
	}
	
	@Override
	public EntityPropertyParser createParser(Entity entity) {
		Assert.notNull(this.fieldSet);
		EntityPropertyParser parser = new EntityPropertyParser(entity, this.fieldSet);
		return parser ;
	}

}
