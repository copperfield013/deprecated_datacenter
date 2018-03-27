package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.abc.mapping.entity.Entity;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.datacenter.model.abc.resolver.EntityBindContext;
import cn.sowell.datacenter.model.abc.resolver.EntityPropertyParser;
import cn.sowell.datacenter.model.abc.resolver.FieldParserDescription;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfig;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfigResolver;
import cn.sowell.datacenter.model.abc.resolver.PropertyNamePartitions;
import cn.sowell.datacenter.model.abc.resolver.exception.UnsupportedEntityElementException;

public abstract class AbstractFusionContextConfigResolver implements FusionContextConfigResolver{
	protected FusionContextConfig config;
	private Set<FieldParserDescription> fieldSet;
	
	Logger logger = Logger.getLogger(AbstractFusionContextConfigResolver.class);
	
	public AbstractFusionContextConfigResolver(FusionContextConfig config) {
		super();
		Assert.notNull(config);
		this.config = config;
	}
	
	protected abstract EntityBindContext buildRootContext(Entity entity);
	
	public synchronized void setFieldSet(Set<FieldParserDescription> fieldSet) {
		this.fieldSet = fieldSet;
	}
	
	
	private Entity createEntity(Map<String, Object> map, boolean ignoreUnsupportedElement) {
		Entity entity = new Entity(config.getMappingName());
		EntityBindContext rootContext = buildRootContext(entity);
		if(rootContext != null) {
			map.forEach((propName, propValue)->{
				try {
					bindElement(rootContext, propName, propValue);
				} catch (UnsupportedEntityElementException e) {
					if(!ignoreUnsupportedElement) {
						throw e;
					}
				}
			});
			((EntitiesContainedEntityProxy)rootContext.getEntity()).commit();
			return entity;
		}
		return null;
	}
	
	@Override
	public Entity createEntity(Map<String, Object> map) {
		logger.debug("==============创建Entity");
		Entity entity = createEntity(map, false);
		logger.debug(entity.toJson());
		return entity;
	}
	
	@Override
	public Entity createEntityIgnoreUnsupportedElement(Map<String, Object> map) {
		return createEntity(map, true);
	}
	
	

	private void bindElement(EntityBindContext context, String propName, Object propValue) throws UnsupportedEntityElementException {
		String[] split = propName.split("\\.", 2);
		String prefix = split[0];
		if(split.length == 1) {
			context.setValue(propName, propValue);
		}else {
			EntityBindContext elementContext = context.getElement(new PropertyNamePartitions(prefix));
			bindElement(elementContext, split[1], propValue);
		}
	}
	
	@Override
	public EntityPropertyParser createParser(Entity entity) {
		Assert.notNull(this.fieldSet);
		EntityBindContext rootContext = buildRootContext(entity);
		CommonEntityPropertyParser parser = new CommonEntityPropertyParser(config, rootContext, this.fieldSet);
		return parser ;
	}

}
