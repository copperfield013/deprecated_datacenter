package cn.sowell.datacenter.model.abc.resolver.impl;

import com.abc.business.MappingContainer;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.node.ABCNode;

import cn.sowell.datacenter.model.abc.resolver.EntityBindContext;
import cn.sowell.datacenter.model.abc.service.impl.FusionContextConfig;

public class ABCNodeFusionContextConfigResolver extends AbstractFusionContextConfigResolver{

	private ABCNode rootNode;
	
	public ABCNodeFusionContextConfigResolver(FusionContextConfig config) {
		super(config);
		rootNode = MappingContainer.getABCNode(config.getMappingName());
		if(rootNode == null) {
			throw new RuntimeException("没有找到ABC配置[" + config.getMappingName() + "]");
		}
	}

	@Override
	protected EntityBindContext buildRootContext(Entity entity) {
		return new ABCNodeEntityBindContext(rootNode, entity);
	}


}

