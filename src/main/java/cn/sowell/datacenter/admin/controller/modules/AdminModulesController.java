package cn.sowell.datacenter.admin.controller.modules;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
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
import cn.sowell.copframe.utils.FormatUtils;
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
import cn.sowell.datacenter.model.config.service.SideMenuService;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.dataserver.model.dict.service.DictionaryService;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.ViewDataService;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroupPremise;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

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
	TemplateService tService;

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
	
	
	Logger logger = Logger.getLogger(AdminModulesController.class);

	@RequestMapping("/list/{menuId}")
	public String list(
			@PathVariable Long menuId,
			PageInfo pageInfo,
			HttpServletRequest request, Model model, ServletRequest session) {
		
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		//创建条件对象
		ListTemplateEntityViewCriteria criteria = new ListTemplateEntityViewCriteria();
		//设置条件
		criteria.setModule(moduleName);
		criteria.setTemplateGroupId(menu.getTemplateGroupId());
		Map<Long, String> criteriaMap = exractTemplateCriteriaMap(request);
		criteria.setListTemplateCriteria(criteriaMap);
		criteria.setPageInfo(pageInfo);
		criteria.setUser(UserUtils.getCurrentUser());
		//执行查询
		ListTemplateEntityView view = (ListTemplateEntityView) vService.query(criteria);
		model.addAttribute("view", view);
		
		//导出状态获取
		String uuid = (String) session.getAttribute(AdminConstants.EXPORT_PEOPLE_STATUS_UUID);
		if(uuid != null){
			WorkProgress progress = eService.getExportProgress(uuid);
			if(progress != null && !progress.isBreaked() && !progress.isCompleted()){
				model.addAttribute("exportStatus", progress);
			}
		}
		//隐藏条件拼接成文件用于提示
		Set<TemplateListCriteria> tCriterias = view.getListTemplate().getCriterias();
		StringBuffer hidenCriteriaDesc = new StringBuffer();
		if(tCriterias != null){
			for (TemplateListCriteria tCriteria : tCriterias) {
				if(tCriteria.getQueryShow() == null && TextUtils.hasText(tCriteria.getDefaultValue()) && tCriteria.getFieldAvailable()) {
					hidenCriteriaDesc.append(tCriteria.getTitle() + ":" + tCriteria.getDefaultValue() + "&#10;");
				}
			}
		}
		TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
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
	
	private Map<Long, String> exractTemplateCriteriaMap(HttpServletRequest request) {
		ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(request, "criteria", "_");
		Map<Long, String> criteriaMap = new HashMap<Long, String>();
		pvs.getPropertyValueList().forEach(pv->{
			 Long criteriaId = FormatUtils.toLong(pv.getName());
			 if(criteriaId != null){
				 criteriaMap.put(criteriaId, FormatUtils.toString(pv.getValue()));
			 }
		 });
		return criteriaMap;
	}

	@RequestMapping("/detail/{menuId}/{code}")
	public String detail(@PathVariable String code, 
			@PathVariable Long menuId,
			String datetime, 
    		Long timestamp,
    		Model model) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		
		Object moduleMeta = mService.getModule(moduleName);
		Date date = null;
        date = dateFormat.parse(datetime);
        if(timestamp != null){
        	date = new Date(timestamp);
        }
        model.addAttribute("date", date == null? new Date(): date);
        TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
        TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplGroup.getDetailTemplateId());
        
        ModuleEntityPropertyParser entity = mService.getEntity(moduleName, code, date, UserUtils.getCurrentUser());
        model.addAttribute("menu", menu);
        model.addAttribute("entity", entity);
        model.addAttribute("datetime", datetime);
        model.addAttribute("dtmpl", dtmpl);
        model.addAttribute("groupPremises", tmplGroup.getPremises());
        model.addAttribute("timestamp", timestamp);
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_MODULES + "/modules_detail_tmpl.jsp";
		
	}
	
	@RequestMapping("/add/{menuId}")
	public String add(@PathVariable Long menuId, Model model) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
		ModuleMeta mMeta = mService.getModule(moduleName);
		TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
		TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplGroup.getDetailTemplateId());
		FusionContextConfig config = fFactory.getModuleConfig(moduleName);
		model.addAttribute("menu", menu);
		model.addAttribute("dtmpl", dtmpl);
		model.addAttribute("groupPremises", tmplGroup.getPremises());
		model.addAttribute("groupPremisesMap", CollectionUtils.toMap(tmplGroup.getPremises(), premise->premise.getFieldName()));
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
		TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
		FusionContextConfig config = fFactory.getModuleConfig(moduleName);
		ModuleEntityPropertyParser entity = mService.getEntity(moduleName, code, null, UserUtils.getCurrentUser());
		TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplGroup.getDetailTemplateId());
		model.addAttribute("menu", menu);
		model.addAttribute("entity", entity);
		model.addAttribute("module", mMeta);
		model.addAttribute("dtmpl", dtmpl);
		model.addAttribute("groupPremises", tmplGroup.getPremises());
		model.addAttribute("config", config);
		model.addAttribute("fieldDescMap", new FieldDescCacheMap(config.getConfigResolver()));
		return AdminConstants.JSP_MODULES + "/modules_update_tmpl.jsp";
	}
	
	
	final static String KEY_FUSE_MODE = "%fuseMode%";
	
	@ResponseBody
    @RequestMapping({"/save/{menuId}/{module}"})
    public AjaxPageResponse save(
    		@PathVariable Long menuId,
    		@RequestParam(value=KEY_FUSE_MODE, required=false) Boolean fuseMode,
    		RequestParameterMapComposite composite){
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		String moduleName = menu.getTemplateModule();
    	 try {
    		 composite.getMap().remove(KEY_FUSE_MODE);
    		 UserIdentifier user = UserUtils.getCurrentUser();
    		 if(Boolean.TRUE.equals(fuseMode)) {
    			 mService.fuseEntity(moduleName, composite.getMap(), user);
    		 }else {
    			 mService.mergeEntity(moduleName, composite.getMap(), user);
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
	
	
	@RequestMapping("/open_selection/{menuId}/{stmplId}")
	public String openSelection(
			@PathVariable Long menuId, 
			@PathVariable Long stmplId,
			String exists,
			PageInfo pageInfo,
			HttpServletRequest request,
			Model model) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		TemplateSelectionTemplate stmpl = tService.getSelectionTemplate(stmplId);
		
		//创建条件对象
		Map<Long, String> criteriaMap = exractTemplateCriteriaMap(request);
		SelectionTemplateEntityViewCriteria criteria = new SelectionTemplateEntityViewCriteria(stmpl, criteriaMap);
		//设置条件
		criteria.setExistCodes(TextUtils.split(exists, ",", HashSet<String>::new, e->e));
		criteria.setPageInfo(pageInfo);
		criteria.setUser(UserUtils.getCurrentUser());
		//执行查询
		SelectionTemplateEntityView view = (SelectionTemplateEntityView) vService.query(criteria);
		model.addAttribute("view", view);
		
		//隐藏条件拼接成文件用于提示
		Set<TemplateSelectionCriteria> tCriterias = view.getSelectionTemplate().getCriterias();
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
	
	
	@ResponseBody
	@RequestMapping("/load_entities/{menuId}/{stmplId}")
	public ResponseJSON loadEntities(
			@PathVariable Long menuId,
			@PathVariable Long stmplId,
			@RequestParam String codes, 
			@RequestParam String fields) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		authService.vaidateL2MenuAccessable(menuId);
		TemplateSelectionTemplate stmpl = tService.getSelectionTemplate(stmplId);
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

	private JSONObject toEntitiesJson(Map<String, CEntityPropertyParser> parsers, Set<String> fieldNames) {
		JSONObject entities = new JSONObject();
		if(parsers != null && fieldNames != null) {
			parsers.forEach((code, parser)->{
				JSONObject entity = new JSONObject();
				entity.put(ABCNodeProxy.CODE_PROPERTY_NAME, parser.getCode());
				entities.put(parser.getCode(), entity);
				for (String fieldName : fieldNames) {
					entity.put(fieldName, parser.getFormatedProperty(fieldName));
				}
			});
		}
		return entities;
	}
	
}
