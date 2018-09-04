package cn.sowell.datacenter.model.relationtreeview.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.abc.vo.EntityRelation;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.datacenter.model.relationtreeview.service.RelationTreeViewService;


@Service
public class RelationTreeViewServiceImpl implements RelationTreeViewService {
	
	@Override
	public String saveTree(String paramJson) {
		// TODO Auto-generated method stub
		JSONObject paramJsonObj = JSONObject.parseObject(paramJson);
		String mappingName = paramJsonObj.getString("parentMappingName");	
		paramJsonObj.remove("parentMappingName");
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
		String[] newMappingNameList = newMappingName.split("\\.");
		paramJsonObj.remove("mappingName");
		String relation = paramJsonObj.getString("relation");
		paramJsonObj.remove("relation");
		if(relation != null && !relation.equals("")) {	//保存关联关系实体
			Entity newEntity = new Entity(newMappingName);
			paramJsonObj.forEach((key, value) -> {
				newEntity.putValue(key, value);
			});
			entity.putRelationEntity(newMappingNameList[newMappingNameList.length - 2], relation, newEntity);
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

	@Override
	public String addRelation(String parentMappingName,
			String parentId,
			String chileMappingName, 
			String chileId,
			String relationName,
			String labelsetmap,
			String labelsetvalue) {
		
		Integration integration = PanelFactory.getIntegration();
		
		BizFusionContext context = getBizFusionContext(parentMappingName);
		EntityRelation entityRelation = getEntityRelation(parentId,chileId,relationName, context, labelsetmap, labelsetvalue);
		String code = integration.integrateRelation(entityRelation, context);
		return code;
	}
	
	
	private EntityRelation getEntityRelation(String parentId,String chileId, String relationName,BizFusionContext context,String labelsetmap, String labelsetvalue) {
		
		EntityRelation entityRelation = new EntityRelation(parentId, context);
		
		//需要新增的关系
		String[] val = labelsetvalue.split(",");
		String[] setvalue = labelsetmap.split(",");
		
		for (int i = 0; i < val.length; i++) {
			String string = val[i];
			entityRelation.addRelation(relationName, val[i], chileId);
		}
		
		for (int j = 0; j < setvalue.length; j++) {
			boolean has = false;
			for (int i = 0; i < val.length; i++) {
				if (setvalue[j].equals(val[i])) {
					has = true;
					break;
				}
			}
			
			if (!has) {
				entityRelation.removeRelation(relationName,setvalue[j], chileId);
			}
			
		}
		return entityRelation;
	}
	
	
	private BizFusionContext getBizFusionContext(String mappingName){
		BizFusionContext context = new BizFusionContext();
		context.setMappingName(mappingName);
		context.setSource(FusionContext.SOURCE_COMMON);
		context.setUserCode(String.valueOf(UserUtils.getCurrentUser().getId()));
		return context;
	}

/*	@Override
	public String removeRelation(String parentMappingName, String parentId, String chileMappingName, String chileId,
			String relationName, String lableRelatioin) {
		Integration integration = PanelFactory.getIntegration();
		
		BizFusionContext context = getBizFusionContext(parentMappingName);
		EntityRelation entityRelation = new EntityRelation(parentId, context);
		entityRelation.removeRelation(relationName, lableRelatioin, chileId);
		String code = integration.integrateRelation(entityRelation, context);
		return code;
	}
*/
}
