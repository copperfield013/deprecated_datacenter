package cn.sowell.datacenter.api2.controller.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.JsonUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.modules.AdminModulesController;
import cn.sowell.datacenter.api2.controller.Api2Constants;
import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.common.EntityQueryPoolUtils;
import cn.sowell.datacenter.common.RequestParameterMapComposite;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.api2.service.MetaJsonService;
import cn.sowell.datacenter.model.api2.service.TemplateJsonParseService;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.datacenter.model.modules.bean.EntityDetail;
import cn.sowell.datacenter.model.modules.service.EntityConvertService;
import cn.sowell.dataserver.model.abc.service.EntitiesQueryParameter;
import cn.sowell.dataserver.model.abc.service.EntityQueryParameter;
import cn.sowell.dataserver.model.abc.service.ModuleEntityService;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.view.EntityItem;
import cn.sowell.dataserver.model.modules.service.view.EntityQuery;
import cn.sowell.dataserver.model.modules.service.view.EntityQueryPool;
import cn.sowell.dataserver.model.modules.service.view.PagedEntityList;
import cn.sowell.dataserver.model.modules.service.view.TreeNodeContext;
import cn.sowell.dataserver.model.tmpl.pojo.ArrayEntityProxy;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroupAction;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateTreeNode;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateTreeTemplate;
import cn.sowell.dataserver.model.tmpl.service.ActionTemplateService;
import cn.sowell.dataserver.model.tmpl.service.ArrayItemFilterService;
import cn.sowell.dataserver.model.tmpl.service.ListCriteriaFactory;
import cn.sowell.dataserver.model.tmpl.service.ListTemplateService;
import cn.sowell.dataserver.model.tmpl.service.TemplateGroupService;
import cn.sowell.dataserver.model.tmpl.service.TreeTemplateService;
import cn.sowell.dataserver.model.tmpl.service.impl.TreeTemplateServiceImpl.TreeRelationComposite;

@Controller
@RequestMapping(Api2Constants.URI_ENTITY + "/curd")
public class Api2EntityCurdController {
	
	@Resource
	AuthorityService authService;
	
	@Resource
	TemplateGroupService tmplGroupService;
	
	@Resource
	ListTemplateService ltmplService;
	
	@Resource
	ModulesService mService;
	
	@Resource
	ListCriteriaFactory lcriteriFacrory;
	
	@Resource
	ApplicationContext applicationContext;
	
	@Resource
	TemplateJsonParseService tJsonService;
	
	@Resource
	MetaJsonService mJsonService;
	
	@Resource
	ModuleEntityService entityService;
	
	@Resource
	TreeTemplateService treeService;
	
	@Resource
	ArrayItemFilterService arrayItemFilterService;
	
	@Resource
	EntityConvertService entityConvertService;
	
	@Resource
	ActionTemplateService atmplService;
	
	static Logger logger = Logger.getLogger(Api2EntityCurdController.class);
	
	
	@ResponseBody
	@RequestMapping("/start_query/{menuId}")
	public ResponseJSON startQuery(@PathVariable Long menuId, 
			PageInfo pageInfo,
			HttpServletRequest request, ApiUser user, HttpSession session) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		
		
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		TemplateListTemplate ltmpl = ltmplService.getTemplate(tmplGroup.getListTemplateId());
		//获得查询池
		EntityQueryPool qPool = EntityQueryPoolUtils.getEntityQueryPool(session, user);
		//注册一个查询
		EntityQuery query = qPool.regist();
		//根据上下文获得节点模板
		//设置参数
		query
			.setModuleName(menu.getTemplateModule())
			.setPageSize(pageInfo.getPageSize())
			.setTemplateGroup(tmplGroup)
			;
		Map<Long, String> requrestCriteriaMap = lcriteriFacrory.exractTemplateCriteriaMap(request);
		//根据传入的条件和约束开始初始化查询对象，但还不获取实体数据
		query.prepare(requrestCriteriaMap, applicationContext);
		//传递参数到页面
		writeListPageAttributes(jRes, query, menu, ltmpl);
		
		return jRes;
	}
	
	
	private void writeListPageAttributes(JSONObjectResponse jRes, 
			EntityQuery query, 
			SideMenuLevel2Menu menu, 
			TemplateListTemplate ltmpl) {
		jRes.put("queryKey", query.getKey());
		jRes.put("menu", mJsonService.toMenuJson(menu));
		
		jRes.put("ltmpl", tJsonService.toListTemplateJson(ltmpl));
		jRes.put("criteriaValueMap",  JsonUtils.convertToStringKeyMap(query.getCriteriaValueMap()));
		jRes.put("tmplGroup", tJsonService.toTemplateGroupJson(query.getTemplateGroup()));
		jRes.put("moduleWritable", mService.getModuleEntityWritable(menu.getTemplateModule()));
	}


	@ResponseBody
	@RequestMapping("/tree/{menuId}")
	public ResponseJSON tree(@PathVariable Long menuId, 
			HttpSession session, 
			PageInfo pageInfo, 
			HttpServletRequest request,
			ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		TemplateListTemplate ltmpl = ltmplService.getTemplate(tmplGroup.getListTemplateId());
		//获得查询池
		EntityQueryPool qPool = EntityQueryPoolUtils.getEntityQueryPool(session, user);
		//注册一个查询
		EntityQuery query = qPool.regist();
		TemplateTreeTemplate ttmpl = treeService.getTemplate(tmplGroup.getTreeTemplateId());
		//构造根节点的上下文
		TreeNodeContext nodeContext = new TreeNodeContext(ttmpl);
		//根据上下文获得节点模板
		TemplateTreeNode nodeTemplate = treeService.analyzeNodeTemplate(nodeContext);;
		//设置参数
		query
			.setModuleName(menu.getTemplateModule())
			.setPageSize(pageInfo.getPageSize())
			.setTemplateGroup(tmplGroup)
			.setNodeTemplate(nodeTemplate)
			;
		Map<Long, String> requrestCriteriaMap = lcriteriFacrory.exractTemplateCriteriaMap(request);
		//根据传入的条件和约束开始初始化查询对象，但还不获取实体数据
		query.prepare(requrestCriteriaMap, applicationContext);
		String nodesCSS = treeService.generateNodesCSS(ttmpl);
		jRes.put("nodeCSS", nodesCSS);
		jRes.put("nodeTmpl", query.getNodeTemplate());
		writeListPageAttributes(jRes, query, menu, ltmpl);
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/start_query_rel/{menuId}/{parentEntityCode}/{nodeRelationId}")
	public ResponseJSON treeNode(@PathVariable Long menuId,
			@PathVariable String parentEntityCode, 
			@PathVariable Long nodeRelationId, 
			@RequestParam(required=false, defaultValue="10") Integer pageSize, 
			HttpSession session, 
			ApiUser user) {
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		JSONObjectResponse jRes = new JSONObjectResponse();
		
		String moduleName = menu.getTemplateModule();
		
		TreeRelationComposite relationComposite = treeService.getNodeRelationTemplate(moduleName, nodeRelationId);
		if(relationComposite != null) {
			//TemplateTreeRelation nodeRelationTempalte = relationComposite.getReltionTempalte();
			//构造查询节点的上下文
			TreeNodeContext nodeContext = new TreeNodeContext(relationComposite);
			//设置当前节点路径
			//nodeContext.setPath(path);
			//根据上下文获得节点模板
			TemplateTreeNode itemNodeTemplate = treeService.analyzeNodeTemplate(nodeContext);
			//获得查询池
			EntityQueryPool qPool = EntityQueryPoolUtils.getEntityQueryPool(session, user);
			//注册一个查询
			EntityQuery query = qPool.regist();
			//在模板中匹配查询结果的Node模板
			//设置参数
			query
				.setModuleName(menu.getTemplateModule())
				.setParentEntityCode(parentEntityCode)
				.setRelationTemplate(relationComposite.getRelationTempalte())
				.setPageSize(pageSize)
				.setNodeTemplate(itemNodeTemplate)
				;
			//执行查询
			Map<Long, String> requrestCriteriaMap = new HashMap<>();
			query.prepare(requrestCriteriaMap, applicationContext);
			
			//根据树形模板将entity转换成node
			jRes.put("queryKey", query.getKey());
			jRes.put("nodeTmpl", query.getNodeTemplate());
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/ask_for/{queryKey}")
	public ResponseJSON askFor(@PathVariable String queryKey,
			PageInfo pageInfo, HttpSession session, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		EntityQueryPool pool = EntityQueryPoolUtils.getEntityQueryPool(session, user);
		EntityQuery query = pool.getQuery(queryKey);
		query.setPageSize(pageInfo.getPageSize());
		PagedEntityList el = query.pageList(pageInfo.getPageNo());
		
		List<EntityItem> entities = entityService.convertEntityItems(el);
		jRes.put("entities", entities);
		jRes.put("isEndList", el.getIsEndList());
		jRes.put("queryKey", query.getKey());
		JSONObject jPageInfo = new JSONObject();
		
		jPageInfo.put("pageSize", query.getPageSize());
		jPageInfo.put("pageNo", query.getPageNo());
		jPageInfo.put("virtualEndPageNo", query.getVirtualEndPageNo());
		jRes.put("pageInfo", jPageInfo);
		
		
		return jRes;
	}
	
	
	@ResponseBody
	@RequestMapping("/get_entities_count/{queryKey}")
	public ResponseJSON getEntitiesCount(@PathVariable String queryKey, ApiUser user, HttpSession session) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		EntityQueryPool pool = EntityQueryPoolUtils.getEntityQueryPool(session, user);
		EntityQuery query = pool.getQuery(queryKey);
		jRes.put("count", query.getCount());
		jRes.setStatus("suc");
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/remove/{menuId}")
	public ResponseJSON remove(@PathVariable Long menuId, String codes, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		Set<String> entityCodes = TextUtils.split(codes, ",");
		EntitiesQueryParameter param = new EntitiesQueryParameter(menu.getTemplateModule(), user);
		param.setEntityCodes(entityCodes);
		try {
			entityService.remove(param);
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("删除实体失败", e);
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/save/{menuId}")
	public ResponseJSON save(
			@PathVariable Long menuId,
			@RequestParam(value=Api2Constants.KEY_FUSE_MODE, required=false) Boolean fuseMode,
			@RequestParam(value=Api2Constants.KEY_ACTION_ID, required=false) Long actionId,
    		RequestParameterMapComposite composite, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		String moduleName = menu.getTemplateModule();
		Map<String, Object> entityMap = composite.getMap();
		if(actionId != null) {
			ArrayEntityProxy.setLocalUser(user);
			TemplateGroupAction groupAction = tmplGroupService.getTempateGroupAction(actionId);
			AdminModulesController.validateGroupAction(groupAction, menu, "");
			entityMap = atmplService.coverActionFields(groupAction, entityMap);
		}
    	 try {
    		 entityMap.remove(Api2Constants.KEY_FUSE_MODE);
    		 entityMap.remove(Api2Constants.KEY_ACTION_ID);
    		 String code = null;
    		 EntityQueryParameter param = new EntityQueryParameter(moduleName, user);
    		 Long tmplGroupId = menu.getTemplateGroupId();
    		 TemplateGroup tmplGroup = tmplGroupService.getTemplate(tmplGroupId);
    		 param.setArrayItemCriterias(arrayItemFilterService.getArrayItemFilterCriterias(tmplGroup.getDetailTemplateId(), user));
    		 if(Boolean.TRUE.equals(fuseMode)) {
    			 code = entityService.fuseEntity(param, entityMap);
    		 }else {
    			 code = entityService.mergeEntity(param, entityMap);
    		 }
    		 if(code != null) {
    			 jRes.put("code", code);
    			 jRes.setStatus("suc");
    		 }
         } catch (Exception e) {
        	 logger.error("保存实体时出现异常", e);
        	 jRes.setStatus("error");
         }
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/do_action/{menuId}/{actionId}")
	public ResponseJSON doAction(@PathVariable Long menuId, @PathVariable Long actionId, String codes, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		authService.validateUserL2MenuAccessable(user, menuId);
		Set<String> entityCodes = TextUtils.split(codes, ",");
		if(entityCodes.isEmpty()) {
			jRes.setStatus("error");
			jRes.put("error", "没有传入codes参数");
		}else {
			ArrayEntityProxy.setLocalUser(user);
			TemplateGroupAction groupAction = tmplGroupService.getTempateGroupAction(actionId);
			TemplateActionTemplate atmpl = atmplService.getTemplate(groupAction.getAtmplId());
			if(atmpl != null) {
				if(entityCodes.size() > 1) {
					if(TemplateGroupAction.ACTION_MULTIPLE_SINGLE.equals(groupAction.getMultiple())
						|| TemplateGroupAction.ACTION_FACE_DETAIL.equals(groupAction.getFace())) {
						//操作要单选，那么不能处理多个code
						jRes.setStatus("error");
						jRes.put("message", "该操作只能处理一个编码");
						return  jRes;
					}
				}
				try {
					int sucs = atmplService.doAction(atmpl, entityCodes, 
							TemplateGroupAction.ACTION_MULTIPLE_TRANSACTION.equals(groupAction.getMultiple()), 
							user);
					jRes.setStatus("suc");
					jRes.put("sucsCount", sucs);
				} catch (Exception e) {
					logger.error("执行失败", e);
					jRes.setStatus("error");
				}
			}else {
				jRes.setStatus("not found action");
			}
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/detail/{menuId}/{code}")
	public ResponseJSON detail(@PathVariable Long menuId, 
			@PathVariable String code, 
			Long historyId,
			ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		//检测用户的权限
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		
		//获得实体对象
		EntityQueryParameter queryParam = new EntityQueryParameter(tmplGroup.getModule(), code, user);
		queryParam.setArrayItemCriterias(arrayItemFilterService.getArrayItemFilterCriterias(tmplGroup.getDetailTemplateId(), user));
		ModuleEntityPropertyParser entity = entityService.getEntityParser(queryParam);
		
		EntityHistoryItem lastHistory = entityService.getLastHistoryItem(queryParam);
		if(historyId != null && lastHistory != null && !historyId.equals(lastHistory.getId())) {
			entity = entityService.getHistoryEntityParser(queryParam, historyId, null);
        }
        if(entity == null) {
        	entity = entityService.getEntityParser(queryParam);
        }
		
		if(entity == null) {
			jRes.setStatus("error");
			jRes.put("message", "没有找到实体");
		}else {
			//用模板组合解析，并返回可以解析为json的对象
			EntityDetail detail = entityConvertService.convertEntityDetail(entity, tmplGroup);
			
			jRes.put("entity", detail);
			jRes.put("errors", entityConvertService.toErrorItems(entity.getErrors()));
			jRes.put("historyId", historyId);
			jRes.setStatus("suc");
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/history/{menuId}/{code}/{pageNo}")
	public ResponseJSON entityHistory(@PathVariable Long menuId, 
			@PathVariable String code, 
			@PathVariable Integer pageNo, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		//检测用户的权限
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		EntityQueryParameter queryParam = new EntityQueryParameter(tmplGroup.getModule(), code, user);
		List<EntityHistoryItem> historyItems = entityService.queryHistory(queryParam, pageNo, 100);
		JSONArray aHistoryItems = entityConvertService.toHistoryItems(historyItems, null);
		jRes.put("history", aHistoryItems);
		return jRes;
	}
	
	
}
