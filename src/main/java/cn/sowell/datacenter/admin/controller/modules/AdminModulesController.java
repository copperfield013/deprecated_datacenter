package cn.sowell.datacenter.admin.controller.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.common.RequestParameterMapComposite;
import cn.sowell.datacenter.entityResolver.CEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.FieldDescCacheMap;
import cn.sowell.datacenter.entityResolver.FusionContextConfig;
import cn.sowell.datacenter.entityResolver.FusionContextConfigFactory;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.ABCNodeProxy;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.datacenter.model.config.service.NonAuthorityException;
import cn.sowell.datacenter.model.config.service.SideMenuService;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.dataserver.model.dict.pojo.DictionaryComposite;
import cn.sowell.dataserver.model.dict.service.DictionaryService;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.ViewDataService;
import cn.sowell.dataserver.model.modules.service.view.ListTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.view.ListTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.modules.service.view.SelectionTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.view.SelectionTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.ArrayEntityProxy;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroupAction;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroupPremise;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionTemplate;
import cn.sowell.dataserver.model.tmpl.service.ActionTemplateService;
import cn.sowell.dataserver.model.tmpl.service.DetailTemplateService;
import cn.sowell.dataserver.model.tmpl.service.ListCriteriaFactory;
import cn.sowell.dataserver.model.tmpl.service.ListTemplateService;
import cn.sowell.dataserver.model.tmpl.service.SelectionTemplateService;
import cn.sowell.dataserver.model.tmpl.service.TemplateGroupService;

@Controller
@RequestMapping(AdminConstants.URI_MODULES + "/curd")
public class AdminModulesController {
	
	@Resource
	ModulesService mService;
	
	@Resource
	ExportService eService;
	
	@Resource
	DictionaryService dictService;
	
	@Resource
	TemplateGroupService tmplGroupService;
	
	@Resource
	DetailTemplateService dtmplService;
	
	@Resource
	ListTemplateService ltmplService;
	
	@Resource
	SelectionTemplateService stmplService;
	
	@Resource
	ActionTemplateService atmplService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	FusionContextConfigFactory fFactory;
	
	@Resource
	ViewDataService vService;
	
	@Resource
	SideMenuService menuService;
	
	@Resource
	AuthorityService authService;
	
	@Resource
	ListCriteriaFactory lcriteriFacrory;
	
	
	Logger logger = Logger.getLogger(AdminModulesController.class);

	@RequestMapping("/list/{menuId}")
	public String list(
			@PathVariable Long menuId,
			PageInfo pageInfo,
			HttpServletRequest request, Model model, HttpSession session) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		//创建条件对象
		ListTemplateEntityViewCriteria criteria = new ListTemplateEntityViewCriteria();
		//设置条件
		criteria.setModule(moduleName);
		criteria.setTemplateGroupId(menu.getTemplateGroupId());
		Map<Long, String> criteriaMap = lcriteriFacrory.exractTemplateCriteriaMap(request);
		criteria.setListTemplateCriteria(criteriaMap);
		criteria.setPageInfo(pageInfo);
		criteria.setUser(UserUtils.getCurrentUser());
		//执行查询
		ListTemplateEntityView view = (ListTemplateEntityView) vService.query(criteria);
		model.addAttribute("view", view);
		
		//导出状态获取
		String uuid = (String) session.getAttribute(AdminConstants.EXPORT_ENTITY_STATUS_UUID);
		if(uuid != null){
			WorkProgress progress = eService.getExportProgress(uuid);
			if(progress != null && !progress.isBreaked()){
				model.addAttribute("exportStatus", progress);
			}
		}
		//隐藏条件拼接成文件用于提示
		List<TemplateListCriteria> tCriterias = view.getListTemplate().getCriterias();
		StringBuffer hidenCriteriaDesc = new StringBuffer();
		if(tCriterias != null){
			for (TemplateListCriteria tCriteria : tCriterias) {
				if(tCriteria.getQueryShow() == null && TextUtils.hasText(tCriteria.getDefaultValue()) && tCriteria.getFieldAvailable()) {
					hidenCriteriaDesc.append(tCriteria.getTitle() + ":" + tCriteria.getDefaultValue() + "&#10;");
				}
			}
		}
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		if(tmplGroup.getPremises() != null) {
			for (TemplateGroupPremise premise : tmplGroup.getPremises()) {
				hidenCriteriaDesc.append(premise.getFieldTitle() + ":" + premise.getFieldValue() + "&#10;");
			}
		}
		model.addAttribute("tmplGroup", tmplGroup);
		model.addAttribute("hidenCriteriaDesc", hidenCriteriaDesc);
		model.addAttribute("menu", menu);
		model.addAttribute("criteria", criteria);
		model.addAttribute("moduleWritable", mService.getModuleEntityWritable(moduleName));
		
		return AdminConstants.JSP_MODULES + "/modules_list_tmpl.jsp";
	}
	

	@RequestMapping("/detail/{menuId}/{code}")
	public String detail(@PathVariable String code, 
			@PathVariable Long menuId,
    		Long historyId,
    		Model model) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		
		ModuleMeta moduleMeta = mService.getModule(moduleName);
        TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
        TemplateDetailTemplate dtmpl = dtmplService.getTemplate(tmplGroup.getDetailTemplateId());
        
        ModuleEntityPropertyParser entity = null;
        UserIdentifier user = UserUtils.getCurrentUser();
        EntityHistoryItem lastHistory = mService.getLastHistoryItem(moduleName, code, user);
		if(historyId != null) {
			if(lastHistory != null && !historyId.equals(lastHistory.getId())) {
				entity = mService.getHistoryEntityParser(moduleName, code, historyId, user);
			}
        }
        if(entity == null) {
        	entity = mService.getEntity(moduleName, code, null, user);
        }
        if(lastHistory != null) {
        	model.addAttribute("hasHistory", true);
        }
        model.addAttribute("menu", menu);
        model.addAttribute("historyId", historyId);
        model.addAttribute("entity", entity);
        model.addAttribute("dtmpl", dtmpl);
        model.addAttribute("groupPremises", tmplGroup.getPremises());
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_MODULES + "/modules_detail_tmpl.jsp";
		
	}
	
	
	@RequestMapping("/add/{menuId}")
	public String add(@PathVariable Long menuId, Model model) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		ModuleMeta mMeta = mService.getModule(moduleName);
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		TemplateDetailTemplate dtmpl = dtmplService.getTemplate(tmplGroup.getDetailTemplateId());
		FusionContextConfig config = fFactory.getModuleConfig(moduleName);
		model.addAttribute("menu", menu);
		model.addAttribute("dtmpl", dtmpl);
		model.addAttribute("tmplGroup", tmplGroup);
		model.addAttribute("groupPremises", tmplGroup.getPremises());
		model.addAttribute("groupPremisesMap", CollectionUtils.toMap(tmplGroup.getPremises(), premise->premise.getFieldName()));
		List<TemplateGroupAction> groupActions = tmplGroup.getActions().stream().filter(action->TemplateGroupAction.ACTION_FACE_DETAIL.equals(action.getFace())).collect(Collectors.toList());
		List<TemplateGroupAction> outgoingGroupActions = new ArrayList<>(),
								normalGroupActions = new ArrayList<>();
		for (TemplateGroupAction action : groupActions) {
			if(TextUtils.hasText(action.getIconClass()) && Integer.valueOf(1).equals(action.getOutgoing())) {
				outgoingGroupActions.add(action);
			}else {
				normalGroupActions.add(action);
			}
		}
		model.addAttribute("outgoingGroupActions", outgoingGroupActions);
		model.addAttribute("normalGroupActions", normalGroupActions);
		model.addAttribute("module", mMeta);
		model.addAttribute("config", config);
		model.addAttribute("fieldDescMap", new FieldDescCacheMap(config.getConfigResolver()));
		return AdminConstants.JSP_MODULES + "/modules_update_tmpl.jsp";
	}
	
	
	@RequestMapping("/update/{menuId}/{code}")
	public String update(
			@PathVariable Long menuId,
			@PathVariable String code,
			Model model
			) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		ModuleMeta mMeta = mService.getModule(moduleName);
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		FusionContextConfig config = fFactory.getModuleConfig(moduleName);
		ModuleEntityPropertyParser entity = mService.getEntity(moduleName, code, null, UserUtils.getCurrentUser());
		TemplateDetailTemplate dtmpl = dtmplService.getTemplate(tmplGroup.getDetailTemplateId());
		model.addAttribute("menu", menu);
		model.addAttribute("entity", entity);
		model.addAttribute("module", mMeta);
		model.addAttribute("dtmpl", dtmpl);
		model.addAttribute("tmplGroup", tmplGroup);
		model.addAttribute("groupPremises", tmplGroup.getPremises());
		List<TemplateGroupAction> groupActions = tmplGroup.getActions().stream().filter(action->TemplateGroupAction.ACTION_FACE_DETAIL.equals(action.getFace())).collect(Collectors.toList());
		List<TemplateGroupAction> outgoingGroupActions = new ArrayList<>(),
								normalGroupActions = new ArrayList<>();
		for (TemplateGroupAction action : groupActions) {
			if(TextUtils.hasText(action.getIconClass()) && Integer.valueOf(1).equals(action.getOutgoing())) {
				outgoingGroupActions.add(action);
			}else {
				normalGroupActions.add(action);
			}
		}
		model.addAttribute("outgoingGroupActions", outgoingGroupActions);
		model.addAttribute("normalGroupActions", normalGroupActions);
		model.addAttribute("config", config);
		model.addAttribute("fieldDescMap", new FieldDescCacheMap(config.getConfigResolver()));
		return AdminConstants.JSP_MODULES + "/modules_update_tmpl.jsp";
	}
	
	
	
	
	@ResponseBody
    @RequestMapping({"/save/{menuId}/{module}"})
    public AjaxPageResponse save(
    		@PathVariable Long menuId,
    		@RequestParam(value=AdminConstants.KEY_FUSE_MODE, required=false) Boolean fuseMode,
    		@RequestParam(value=AdminConstants.KEY_ACTION_ID, required=false) Long actionId,
    		RequestParameterMapComposite composite){
		UserIdentifier user = UserUtils.getCurrentUser();
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		Map<String, Object> entityMap = composite.getMap();
		if(actionId != null) {
			ArrayEntityProxy.setLocalUser(user);
			TemplateGroupAction groupAction = tmplGroupService.getTempateGroupAction(actionId);
			validateGroupAction(groupAction, menu, "");
			entityMap = atmplService.coverActionFields(groupAction, entityMap);
		}
    	 try {
    		 entityMap.remove(AdminConstants.KEY_FUSE_MODE);
    		 entityMap.remove(AdminConstants.KEY_ACTION_ID);
    		 if(Boolean.TRUE.equals(fuseMode)) {
    			 mService.fuseEntity(moduleName, entityMap, user);
    		 }else {
    			 mService.mergeEntity(moduleName, entityMap, user);
    		 }
             return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("保存成功", "entity_list_" + menuId);
         } catch (Exception e) {
             logger.error("保存时发生错误", e);
             return AjaxPageResponse.FAILD("保存失败");
         }
    }
	
	
	
	@ResponseBody
    @RequestMapping("/paging_history/{menuId}/{code}")
    public JSONObjectResponse pagingHistory(
    		@PathVariable Long menuId,
    		@PathVariable String code, 
    		@RequestParam Integer pageNo, 
    		@RequestParam(defaultValue="100") Integer pageSize){
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
    	JSONObjectResponse response = new JSONObjectResponse();
    	try {
			List<EntityHistoryItem> historyItems = mService.queryHistory(menu.getTemplateModule(), code, pageNo, pageSize, UserUtils.getCurrentUser());
			response.put("history", JSON.toJSON(historyItems));
			response.setStatus("suc");
			if(historyItems.size() < pageSize){
				response.put("isLast", true);
			}
		} catch (Exception e) {
			logger.error("查询历史失败", e);
		}
    	
    	return response;
    }
	
	@ResponseBody
	@RequestMapping("/delete/{menuId}/{code}")
	public AjaxPageResponse delete(
			@PathVariable Long menuId,
			@PathVariable String code
			) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		try {
			mService.deleteEntity(menu.getTemplateModule(), code, UserUtils.getCurrentUser());
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/remove/{menuId}")
	public AjaxPageResponse remove(
			@PathVariable Long menuId,
			@RequestParam String codes) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		try {
			mService.removeEntities(menu.getTemplateModule(), collectCode(codes), UserUtils.getCurrentUser());
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	
	@RequestMapping("/open_selection/{menuId}/{stmplId}")
	public String openSelection(
			@PathVariable Long menuId, 
			@PathVariable Long stmplId,
			String exists,
			PageInfo pageInfo,
			HttpServletRequest request,
			Model model) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		TemplateSelectionTemplate stmpl = stmplService.getTemplate(stmplId);
		
		//创建条件对象
		Map<Long, String> criteriaMap = lcriteriFacrory.exractTemplateCriteriaMap(request);
		SelectionTemplateEntityViewCriteria criteria = new SelectionTemplateEntityViewCriteria(stmpl, criteriaMap);
		//设置条件
		criteria.setExistCodes(TextUtils.split(exists, ",", HashSet<String>::new, e->e));
		criteria.setPageInfo(pageInfo);
		criteria.setUser(UserUtils.getCurrentUser());
		//执行查询
		SelectionTemplateEntityView view = (SelectionTemplateEntityView) vService.query(criteria);
		model.addAttribute("view", view);
		
		//隐藏条件拼接成文件用于提示
		List<TemplateSelectionCriteria> tCriterias = view.getSelectionTemplate().getCriterias();
		StringBuffer hidenCriteriaDesc = new StringBuffer();
		if(tCriterias != null){
			for (TemplateSelectionCriteria tCriteria : tCriterias) {
				if(tCriteria.getQueryShow() == null && TextUtils.hasText(tCriteria.getDefaultValue()) && tCriteria.getFieldAvailable()) {
					hidenCriteriaDesc.append(tCriteria.getTitle() + ":" + tCriteria.getDefaultValue() + "&#10;");
				}
			}
		}
		
		model.addAttribute("menu", menu);
		model.addAttribute("stmpl", stmpl);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_MODULES + "/modules_selection.jsp";
	}
	
	@RequestMapping("/rabc_create/{mainMenuId}/{fieldGroupId}")
	public String rabcCreate(@PathVariable Long mainMenuId, 
			@PathVariable Long fieldGroupId, String entityCode, Model model) {
		SideMenuLevel2Menu mainMenu = authService.vaidateL2MenuAccessable(mainMenuId);
		model.addAttribute("mainMenu", mainMenu);
		TemplateGroup mainTmplGroup = tmplGroupService.getTemplate(mainMenu.getTemplateGroupId());
		if(mainTmplGroup != null) {
			TemplateDetailTemplate mainDtmpl = dtmplService.getTemplate(mainTmplGroup.getDetailTemplateId());
			if(mainDtmpl != null) {
				TemplateDetailFieldGroup fieldGroup = mainDtmpl.getGroups().stream().filter(fg->fieldGroupId.equals(fg.getId())).findFirst().get();
				
				Long relationTemplateGroupId = fieldGroup.getRabcTemplateGroupId();
				
				if(relationTemplateGroupId != null) {
					TemplateGroup tmplGroup = tmplGroupService.getTemplate(relationTemplateGroupId);
					
					String rabcModuleName = tmplGroup.getModule();
					
					ModuleMeta mMeta = mService.getModule(rabcModuleName);
					
					FusionContextConfig config = fFactory.getModuleConfig(rabcModuleName);
					if(TextUtils.hasText(entityCode)) {
						ModuleEntityPropertyParser entity = mService.getEntity(rabcModuleName, entityCode, null, UserUtils.getCurrentUser());
						model.addAttribute("entity", entity);
					}
					TemplateDetailTemplate dtmpl = dtmplService.getTemplate(tmplGroup.getDetailTemplateId());
					model.addAttribute("module", mMeta);
					model.addAttribute("dtmpl", dtmpl);
					model.addAttribute("tmplGroup", tmplGroup);
					model.addAttribute("groupPremises", tmplGroup.getPremises());
					List<TemplateGroupAction> groupActions = tmplGroup.getActions().stream().filter(action->TemplateGroupAction.ACTION_FACE_DETAIL.equals(action.getFace())).collect(Collectors.toList());
					List<TemplateGroupAction> outgoingGroupActions = new ArrayList<>(),
											normalGroupActions = new ArrayList<>();
					for (TemplateGroupAction action : groupActions) {
						if(TextUtils.hasText(action.getIconClass()) && Integer.valueOf(1).equals(action.getOutgoing())) {
							outgoingGroupActions.add(action);
						}else {
							normalGroupActions.add(action);
						}
					}
					model.addAttribute("rabcTemplateGroup", tmplGroup);
					model.addAttribute("outgoingGroupActions", outgoingGroupActions);
					model.addAttribute("normalGroupActions", normalGroupActions);
					model.addAttribute("config", config);
					model.addAttribute("fieldDescMap", new FieldDescCacheMap(config.getConfigResolver()));
					model.addAttribute("relationCompositeId", fieldGroup.getCompositeId());
					return AdminConstants.JSP_MODULES + "/modules_update_tmpl.jsp";
				}
				/*
				
				Long relationDetailTemplateId = fieldGroup.getRelationDetailTemplateId();
				TemplateDetailTemplate dtmpl = dtmplService.getTemplate(relationDetailTemplateId);
				String moduleName = dtmpl.getModule(); 
				ModuleMeta mMeta = mService.getModule(moduleName);
				FusionContextConfig config = fFactory.getModuleConfig(moduleName);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("dtmpl", dtmpl);
				model.addAttribute("module", mMeta);
				model.addAttribute("config", config);
				model.addAttribute("relationDetailTemplate", dtmpl);
				model.addAttribute("relationCompositeId", fieldGroup.getCompositeId());
				model.addAttribute("fieldDescMap", new FieldDescCacheMap(config.getConfigResolver()));
				return AdminConstants.JSP_MODULES + "/modules_update_tmpl.jsp";*/
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/rabc_save/{mainMenuId}/{rabcTemplateGroupId}")
	public ResponseJSON rabcSave(@PathVariable Long mainMenuId, 
			@PathVariable Long rabcTemplateGroupId,
			@RequestParam(value=AdminConstants.KEY_FUSE_MODE, required=false) Boolean fuseMode,
    		RequestParameterMapComposite composite){
		JSONObjectResponse jRes = new JSONObjectResponse();
		authService.vaidateL2MenuAccessable(mainMenuId);
		TemplateGroup rabcTmplGroup = tmplGroupService.getTemplate(rabcTemplateGroupId);
		String moduleName = rabcTmplGroup.getModule();
		Map<String, Object> entityMap = composite.getMap();
    	try {
    		 entityMap.remove(AdminConstants.KEY_FUSE_MODE);
    		 UserIdentifier user = UserUtils.getCurrentUser();
    		 String entityCode = null;
    		 if(Boolean.TRUE.equals(fuseMode)) {
    			 entityCode = mService.fuseEntity(moduleName, entityMap, user);
    		 }else {
    			 entityCode = mService.mergeEntity(moduleName, entityMap, user);
    		 }
    		 if(TextUtils.hasText(entityCode)) {
    			 jRes.put("entityCode", entityCode);
    			 jRes.setStatus("suc");
    		 }else {
    			 jRes.setStatus("unknow-entity-code");
    		 }
         } catch (Exception e) {
             logger.error("保存时发生错误", e);
             jRes.setStatus("error");
         }
		return jRes;
	}
	
	
	
	@ResponseBody
	@RequestMapping("/load_entities/{menuId}/{stmplId}")
	public ResponseJSON loadEntities(
			@PathVariable Long menuId,
			@PathVariable Long stmplId,
			@RequestParam String codes, 
			@RequestParam String fields) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		authService.vaidateL2MenuAccessable(menuId);
		TemplateSelectionTemplate stmpl = stmplService.getTemplate(stmplId);
		Map<String, CEntityPropertyParser> parsers = mService.getEntityParsers(
				stmpl.getModule(), 
				stmpl.getRelationName(), 
				TextUtils.split(codes, ",", HashSet<String>::new, c->c), UserUtils.getCurrentUser())
				;
		JSONObject entities = toEntitiesJson(parsers, TextUtils.split(fields, ",", HashSet<String>::new, f->f));
		jRes.put("entities", entities);
		jRes.setStatus("suc");
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/load_rabc_entities/{menuId}/{relationCompositeId}")
	public ResponseJSON loadRabcEntities(@PathVariable Long menuId, @PathVariable Long relationCompositeId, 
			String codes, String fields) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		DictionaryComposite composite = dictService.getComposite(menu.getTemplateModule(), relationCompositeId);
		Map<String, CEntityPropertyParser> parsers = mService.getEntityParsers(
				composite.getModule(), 
				composite.getName(), 
				TextUtils.split(codes, ",", HashSet<String>::new, c->c), UserUtils.getCurrentUser())
				;
		JSONObject entities = toEntitiesJson(parsers, TextUtils.split(fields, ",", HashSet<String>::new, f->f));
		jRes.put("entities", entities);
		jRes.setStatus("suc");
		return jRes;
		
	}

	public static JSONObject toEntitiesJson(Map<String, CEntityPropertyParser> parsers, Set<String> fieldNames) {
		JSONObject entities = new JSONObject();
		if(parsers != null && fieldNames != null) {
			parsers.forEach((code, parser)->{
				JSONObject entity = new JSONObject();
				entity.put(ABCNodeProxy.CODE_PROPERTY_NAME_NORMAL, parser.getCode());
				entities.put(parser.getCode(), entity);
				for (String fieldName : fieldNames) {
					entity.put(fieldName, parser.getFormatedProperty(fieldName));
				}
			});
		}
		return entities;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/do_action/{menuId}/{actionId}")
	public AjaxPageResponse doAction(@PathVariable Long menuId, @PathVariable Long actionId, @RequestParam(name="codes") String codeStr) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		ArrayEntityProxy.setLocalUser(UserUtils.getCurrentUser());
		TemplateGroupAction groupAction = tmplGroupService.getTempateGroupAction(actionId);
		Object vRes = validateGroupAction(groupAction, menu, codeStr);
		if(vRes instanceof AjaxPageResponse) {
			return (AjaxPageResponse) vRes;
		}
		Set<String> codes = (Set<String>) vRes;
		TemplateActionTemplate atmpl = atmplService.getTemplate(groupAction.getAtmplId());
		if(atmpl != null) {
			try {
				int sucs = atmplService.doAction(atmpl, codes, 
						TemplateGroupAction.ACTION_MULTIPLE_TRANSACTION.equals(groupAction.getMultiple()), 
						UserUtils.getCurrentUser());
				return AjaxPageResponse.REFRESH_LOCAL("执行结束, 共成功处理" + sucs + "个实体");
			} catch (Exception e) {
				logger.error("操作失败", e);
				return AjaxPageResponse.FAILD("执行失败");
			}
		}else {
			return AjaxPageResponse.FAILD("操作不存在");
		}
		
	}

	public static Object validateGroupAction(TemplateGroupAction groupAction, SideMenuLevel2Menu menu, String codes) {
		if(!groupAction.getGroupId().equals(menu.getTemplateGroupId())) {
			throw new NonAuthorityException("二级菜单[id=" + menu.getId() + "]对应的模板组合[id=" + menu.getTemplateGroupId() + "]与操作[id=" + groupAction.getId() + "]对应的模板组合[id=" + groupAction.getGroupId() + "]不一致");
		}
		if(!codes.isEmpty()) {
			Set<String> codeSet = collectCode(codes);
			if(!codeSet.isEmpty()) {
				if(codeSet.size() > 1) {
					if(TemplateGroupAction.ACTION_MULTIPLE_SINGLE.equals(groupAction.getMultiple())
						|| TemplateGroupAction.ACTION_FACE_DETAIL.equals(groupAction.getFace())) {
						//操作要单选，那么不能处理多个code
						return AjaxPageResponse.FAILD("该操作只能处理一个编码");
					}
				}
				return codeSet;
			}
		}
		return AjaxPageResponse.FAILD("没有传入编码参数");
		
	}

	private static Set<String> collectCode(String codes) {
		Set<String> codeSet = new LinkedHashSet<>();
		for (String code : codes.split(",")) {
			if(!code.isEmpty()) {
				codeSet.add(code);
			}
		};
		return codeSet;
	}
	
	
}
