package cn.sowell.datacenter.admin.controller.treeview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RelationEntity;
import com.abc.mapping.node.ABCNode;
import com.abc.mapping.node.AttributeNode;
import com.abc.service.RelationTreeServiceFactory;
import com.abc.vo.RelationVO;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.treeview.service.TreeViewService;


@Controller
@RequestMapping("/admin/treeview")
public class AdminTreeViewController {
	
	@Resource
	TreeViewService treeService;
	
	@RequestMapping("/treeView")
	public String treeView(@RequestParam String id, @RequestParam String mappingName, PageInfo pageInfo, Model model) {
		//ABCNode node = treeService.getABCNode(mappingName);
		//model.addAttribute("node", node);
		List<String> nameList = new ArrayList<String>();
		Map<String, String> mappingNameMap = new HashMap<>();
		Map<String, List> labelSetMap = new HashMap<>();
		Map<String, String> subAbcNodeNameMap = new HashMap<>();
		getNodeInfo(mappingName, nameList, mappingNameMap, labelSetMap, subAbcNodeNameMap);
		
		model.addAttribute("nameList", nameList);
		model.addAttribute("mappingNameMap", JSONObject.toJSON(mappingNameMap));
		model.addAttribute("labelSetMap", JSONObject.toJSON(labelSetMap));
		model.addAttribute("subAbcNodeNameMap", JSONObject.toJSON(subAbcNodeNameMap));
		
		model.addAttribute("id", id);
		model.addAttribute("mappingName", mappingName);
		return "/admin/treeview/tree_view.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/data")
	public String getData(@RequestParam(defaultValue="") String code, @RequestParam String configName) {
		String dataJsonStr = treeService.getData(configName, code);
		JSONObject dataJsonObj = JSONObject.parseObject(dataJsonStr);
		List<AttributeNode> abcAttrNodeList = getAbcNodeList(configName);
		List resultList = new ArrayList<>();
		resultList.add(dataJsonObj.get("唯一编码"));
		abcAttrNodeList.forEach(key -> {
			resultList.add(dataJsonObj.get(key.getAbcattrName()));
		});
		return JSONObject.toJSONString(resultList);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/getChildrenNode")
	public String getChildrenNode(@RequestParam(defaultValue="") String code, @RequestParam String mappingName) {
		Entity dataEntity = treeService.getEntity(mappingName, code);
		Set<String> relationNames = dataEntity.getRelationNames();
		Map<String, List> relationsMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		
		//获取当前节点下的节点信息
		List<String> nameList = new ArrayList<String>();
		Map<String, String> mappingNameMap = new HashMap<>();
		Map<String, List> labelSetMap = new HashMap<>();
		Map<String, String> subAbcNodeNameMap = new HashMap<>();
		getNodeInfo(mappingName, nameList, mappingNameMap, labelSetMap, subAbcNodeNameMap);
		resultMap.put("mappingNameMap", mappingNameMap);
		
		List<AttributeNode> abcAttrNodeList = getAbcNodeList(mappingName);
		for(String relationName : relationNames) {
			List<RelationEntity> relationList = dataEntity.getRelations(relationName);
			if(relationList != null && relationList.size() > 0) {
				for(RelationEntity relationEntity : relationList) {
					JSONObject nodeJsonObj = JSONObject.parseObject(relationEntity.getEntity().toJson());
					List result = new ArrayList<>();
					result.add(relationName);
					for(AttributeNode attributeNode : abcAttrNodeList) {
						result.add(nodeJsonObj.get(attributeNode.getAbcattrName()));
					}
					relationsMap.put(nodeJsonObj.getString("唯一编码"), result);
				}
			}
		}
		resultMap.put("relations", relationsMap);
		System.out.println("\n\n------------------------------------------");
		System.out.println(resultMap);
		return JSONObject.toJSONString(resultMap);
	}
	
	private void getNodeInfo(String mappingName,List<String> nameList, Map<String, String> mappingNameMap, Map<String, List> labelSetMap, Map<String, String> subAbcNodeNameMap) {
		Collection<RelationVO> relationVoList = RelationTreeServiceFactory.getRelationTreeService().getRelationVO(mappingName);
		relationVoList.forEach(relationVo -> {
			
			nameList.add(relationVo.getName());
			
			mappingNameMap.put(relationVo.getName(), relationVo.getMappingName());
			
			labelSetMap.put(relationVo.getName(), new ArrayList<>(relationVo.getLabelSet()));
			
			subAbcNodeNameMap.put(relationVo.getName(), relationVo.getSubAbcNodeName());
		});
	}
	
	@ResponseBody
	@RequestMapping("/getNodeAttr")
	public String getAbcNode(String mappingName) {
		List<AttributeNode> attributesNameList = getAbcNodeList(mappingName);
		JSONObject result = new JSONObject();
		result.put("AttributesName", attributesNameList);
		return result.toString();
	}
	
	/**
	 * get某个mappingName对应的基本属性列表
	 * @param mappingName
	 * @return
	 */
	private List<AttributeNode> getAbcNodeList(String mappingName) {
		ABCNode abcNode = treeService.getABCNode(mappingName);
		List<AttributeNode> attributesNameCollection = abcNode.getOrderAttributes();
		List<AttributeNode> attributesNameList = new ArrayList<>();
		for(AttributeNode attributeNode : attributesNameCollection) {
			if(attributeNode.getOrder() > 0) {
				attributesNameList.add(attributeNode);
			}
		}
		return attributesNameList;
	}
	
	@RequestMapping("/add")
	public String add(String type, String mappingName, String id, String rootId, Model model) {
		if(type.equals("relation")) {
			List<String> nameList = new ArrayList<String>();
			Map<String, String> mappingNameMap = new HashMap<>();
			Map<String, List> labelSetMap = new HashMap<>();
			Map<String, String> subAbcNodeNameMap = new HashMap<>();
			getNodeInfo(mappingName, nameList, mappingNameMap, labelSetMap, subAbcNodeNameMap);
			
			model.addAttribute("nameList", nameList);
			model.addAttribute("mappingNameMap", JSONObject.toJSON(mappingNameMap));
			model.addAttribute("labelSetMap", JSONObject.toJSON(labelSetMap));
			model.addAttribute("subAbcNodeNameMap", JSONObject.toJSON(subAbcNodeNameMap));
			
			model.addAttribute("parentNodeId", id);
			model.addAttribute("mappingName", mappingName);
			model.addAttribute("rootId", rootId);
			return "/admin/treeview/tree_relation_add.jsp";
		}
		return "";
	}
	
	@RequestMapping("/edit")
	public String edit(String mappingName, String id, String rootId, Model model) {
		String nodeStr = treeService.getData(mappingName, id);
		List<AttributeNode> abcAttrNodeList = getAbcNodeList(mappingName);
		JSONObject nodeJsonObj = JSONObject.parseObject(nodeStr);
		Map<String, Object> nodeMap = new HashMap<>();
		for(Map.Entry<String, Object> entry : nodeJsonObj.entrySet()) {
			for(AttributeNode attributeNode : abcAttrNodeList) {
				if(entry.getKey().equals(attributeNode.getAbcattrName())) {
					nodeMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		model.addAttribute("node", nodeMap);
		
		model.addAttribute("abcAttrNodeList", abcAttrNodeList);
		model.addAttribute("parentNodeId", id);
		model.addAttribute("mappingName", mappingName);
		model.addAttribute("rootId", rootId);
		return "/admin/treeview/tree_node_edit.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(String code) {
		boolean bool = treeService.deleteTree(code);
		if(bool) {
			return "true";
		}else {
			return "false";
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/saveNode")
	public String saveNode(String mappingName, String paramJson) {
		//String code = treeService.saveRole();
		String code = treeService.saveTree(mappingName, paramJson);
		return "true";
	}
	

}