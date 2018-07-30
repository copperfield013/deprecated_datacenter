package cn.sowell.datacenter.model.treeview.service.impl;

import org.springframework.stereotype.Service;

import com.abc.application.BizFusionContext;
import com.abc.application.FusionContext;
import com.abc.application.RemovedFusionContext;
import com.abc.mapping.conf.MappingContainer;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.node.ABCNode;
import com.abc.panel.Discoverer;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.datacenter.model.treeview.service.TreeViewService;

@Service
public class TreeViewServiceImpl implements TreeViewService {
	
	@Override
	public String saveTree(String mappingName, String paramJson) {
		// TODO Auto-generated method stub
		JSONObject paramJsonObj = JSONObject.parseObject(paramJson);
		String[] mappingNameList = mappingName.split("\\.");
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mappingNameList[0]);
		context.setSource(FusionContext.SOURCE_COMMON);
		context.setUserCode("u5");
		
		Discoverer discoverer = PanelFactory.getDiscoverer(context);
		
		String parentNodeId = paramJsonObj.getString("parentNodeId");
		paramJsonObj.remove("parentNodeId");
		Entity entity = discoverer.discover(parentNodeId);//
		
		//Entity entity = new Entity(mappingName);
		
		String newMappingName = paramJsonObj.getString("mappingName");
		paramJsonObj.remove("mappingName");
		String relation = paramJsonObj.getString("relation");
		paramJsonObj.remove("relation");
		if(relation != null && !relation.equals("")) {	//保存关联关系实体
			Entity newEntity = new Entity(newMappingName);
			paramJsonObj.forEach((key, value) -> {
				newEntity.putValue(key, value);
			});
			entity.putRelationEntity(newMappingName, relation, newEntity);
		}else {		//保存普通属性
			paramJsonObj.forEach((key, value) -> {
				entity.putValue(key, value);
			});
		}
		Integration integration=PanelFactory.getIntegration();
		return integration.integrate(entity, context);
	}

	@Override
	public String getData(String mappingName, String code) {
		if(code != null && !code.equals("")) {
			BizFusionContext context = new BizFusionContext();
			context.setMappingName(mappingName);
			context.setSource(FusionContext.SOURCE_COMMON);
			context.setUserCode("u5");
			Discoverer discoverer = PanelFactory.getDiscoverer(context);
			Entity result = discoverer.discover(code);
			if(result != null) {
				System.out.println("\n----------------" + result.toJson());
				return result.toJson();
			}else {
				return "{}";
			}
		}else {
			return "{}";
		}
	}
	
	@Override
	public Entity getEntity(String mappingName, String code) {
		if(code != null && !code.equals("")) {
			BizFusionContext context = new BizFusionContext();
			context.setMappingName(mappingName);
			context.setSource(FusionContext.SOURCE_COMMON);
			context.setUserCode("u5");
			Discoverer discoverer = PanelFactory.getDiscoverer(context);
			Entity result = discoverer.discover(code);
			return result;
		}else {
			return null;
		}
	}

	@Override
	public boolean deleteTree(String code) {
		RemovedFusionContext entityInfo = new RemovedFusionContext(code, null, null);
		Integration integration=PanelFactory.getIntegration();
		boolean bool = integration.remove(entityInfo);
		return bool;
	}

	@Override
	public ABCNode getABCNode(String mappingName) {
		ABCNode node = MappingContainer.getABCNode(mappingName);
		return node;
	}

}
