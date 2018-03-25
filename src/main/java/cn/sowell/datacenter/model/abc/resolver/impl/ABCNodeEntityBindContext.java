package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.List;

import org.springframework.util.Assert;

import com.abc.mapping.entity.Entity;
import com.abc.mapping.node.ABCNode;

import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.datacenter.model.abc.resolver.EntityBindContext;
import cn.sowell.datacenter.model.abc.resolver.EntityElement;
import cn.sowell.datacenter.model.abc.resolver.EntityProxy;
import cn.sowell.datacenter.model.abc.resolver.PropertyNamePartitions;
import cn.sowell.datacenter.model.abc.resolver.exception.UnsupportedEntityElementException;
import common.Logger;

public class ABCNodeEntityBindContext extends AbstractEntityBindContext {
	private ABCNodeProxy node;
	
	Logger logger = Logger.getLogger(ABCNodeEntityBindContext.class);
	
	public ABCNodeEntityBindContext(ABCNode rootNode, Entity entity) {
		this(new ABCNodeProxy(rootNode), new ABCEntityProxy(entity), null);
	}

	public ABCNodeEntityBindContext(ABCNodeProxy thisNode, EntityProxy thisEntity,
			ABCNodeEntityBindContext parent) {
		super(thisEntity);
		this.node = thisNode;
	}


	@Override
	public EntityBindContext getElement(String propName) {
		Assert.isInstanceOf(EntitiesContainedEntityProxy.class, this.entity);
		//构造属性名对象
		PropertyNamePartitions namePartitions = new PropertyNamePartitions(propName);
		//获得子节点的信息对象
		ABCNodeProxy eleNode = this.node.getElement(namePartitions.getMainPartition());
		try {
			//根据属性名获得子节点对象
			EntityProxy eleEntity = getElement(namePartitions);
			if(eleEntity == null) {
				//无法获得子节点时，创建一个子节点
				eleEntity = eleNode.createElementEntity();
				//将子节点对象放到当前节点中
				((EntitiesContainedEntityProxy) this.entity).putEntity(namePartitions, eleEntity);
			}
			return new ABCNodeEntityBindContext(eleNode, eleEntity, this);
		} catch (Exception e) {
			throw new RuntimeException("获得子节点时发生错误", e);
		}
	}
	
	private EntityProxy getElement(PropertyNamePartitions namePartitions) {
		List<EntityProxy> entities = ((EntitiesContainedEntityProxy) this.entity).getEntityElements(namePartitions.getMainPartition());
		if(entities != null) {
			int index = FormatUtils.coalesce(namePartitions.getIndex(), 0);
			if(index < entities.size()) {
				return entities.get(index);
			}
		}
		return null;
	}

	@Override
	protected EntityElement getEntityElement(String propName) {
		ABCNodeProxy ele = this.node.getElement(propName);
		if(ele != null) {
			return ele.getEntityElement();
		}else {
			throw new UnsupportedEntityElementException("没有找到属性[" + propName + "]");
		}
	}
	
	
	
	
	
}
