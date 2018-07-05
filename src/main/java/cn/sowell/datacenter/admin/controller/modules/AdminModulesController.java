package cn.sowell.datacenter.admin.controller.modules;

import java.util.Date;
import java.util.HashMap;
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

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.common.RequestParameterMapComposite;
import cn.sowell.datacenter.entityResolver.FieldDescCacheMap;
import cn.sowell.datacenter.entityResolver.FusionContextConfig;
import cn.sowell.datacenter.entityResolver.FusionContextConfigFactory;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.SideMenuService;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.dataserver.model.dict.service.DictionaryService;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.ViewDataService;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListCriteria;
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
	
	
	Logger logger = Logger.getLogger(AdminModulesController.class);

	@RequestMapping("/list/{menuId}")
	public String list(
			@PathVariable Long menuId,
			PageInfo pageInfo,
			HttpServletRequest request, Model model, ServletRequest session) {
		
		SideMenuLevel2Menu menu = menuService.getLevel2Menu(menuId);
		String moduleName = menu.getTemplateModule();
		String templateGroupKey = menu.getTemplateGroupKey();
		//创建条件对象
		ListTemplateEntityViewCriteria criteria = new ListTemplateEntityViewCriteria();
		//设置条件
		criteria.setModule(moduleName);
		criteria.setTemplateGroupKey(templateGroupKey);
		Map<Long, String> criteriaMap = exractTemplateCriteriaMap(request);
		criteria.setListTemplateCriteria(criteriaMap);
		criteria.setPageInfo(pageInfo);
		//执行查询
		ListTemplateEntityView view = (ListTemplateEntityView) vService.query(criteria);
		model.addAttribute("view", view);
		
		//导出状态获取
		String uuid = (String) session.getAttribute(AdminConstants.EXPORT_PEOPLE_STATUS_UUID);
		if(uuid != null){
			ExportStatus exportStatus = eService.getExportStatus(uuid);
			if(exportStatus != null && !exportStatus.isBreaked() && !exportStatus.isCompleted()){
				model.addAttribute("exportStatus", exportStatus);
			}
		}
		//隐藏条件拼接成文件用于提示
		Set<TemplateListCriteria> tCriterias = view.getListTemplate().getCriterias();
		if(tCriterias != null){
			StringBuffer hidenCriteriaDesc = new StringBuffer();
			for (TemplateListCriteria tCriteria : tCriterias) {
				if(tCriteria.getQueryShow() == null && TextUtils.hasText(tCriteria.getDefaultValue()) && tCriteria.getFieldAvailable()) {
					hidenCriteriaDesc.append(tCriteria.getTitle() + ":" + tCriteria.getDefaultValue() + "&#10;");
				}
			}
			model.addAttribute("hidenCriteriaDesc", hidenCriteriaDesc);
		}
		model.addAttribute("menu", menu);
		model.addAttribute("criteria", criteria);
		
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
		SideMenuLevel2Menu menu = menuService.getLevel2Menu(menuId);
		String moduleName = menu.getTemplateModule();
		
		Object moduleMeta = mService.getModule(moduleName);
		Date date = null;
        date = dateFormat.parse(datetime);
        if(timestamp != null){
        	date = new Date(timestamp);
        }
        TemplateDetailTemplate dtmpl = tService.getDetailTemplateByGroupId(menu.getTemplateGroupId());
        
        ModuleEntityPropertyParser entity = mService.getEntity(moduleName, code, date);
        model.addAttribute("menu", menu);
        model.addAttribute("date", date);
        model.addAttribute("entity", entity);
        model.addAttribute("datetime", datetime);
        model.addAttribute("dtmpl", dtmpl);
        model.addAttribute("timestamp", timestamp);
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_MODULES + "/modules_detail_tmpl.jsp";
		
	}
	
	@RequestMapping("/add/{menuId}")
	public String add(@PathVariable Long menuId, Model model) {
		SideMenuLevel2Menu menu = menuService.getLevel2Menu(menuId);
		String moduleName = menu.getTemplateModule();
		ModuleMeta mMeta = mService.getModule(moduleName);
		TemplateDetailTemplate dtmpl = tService.getDetailTemplateByGroupId(menu.getTemplateGroupId());
		FusionContextConfig config = fFactory.getModuleConfig(moduleName);
		model.addAttribute("menu", menu);
		model.addAttribute("dtmpl", dtmpl);
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
		SideMenuLevel2Menu menu = menuService.getLevel2Menu(menuId);
		String moduleName = menu.getTemplateModule();
		ModuleMeta mMeta = mService.getModule(moduleName);
		FusionContextConfig config = fFactory.getModuleConfig(moduleName);
		ModuleEntityPropertyParser entity = mService.getEntity(moduleName, code, null);
		TemplateDetailTemplate dtmpl = tService.getDetailTemplateByGroupId(menu.getTemplateGroupId());
		model.addAttribute("menu", menu);
		model.addAttribute("entity", entity);
		model.addAttribute("module", mMeta);
		model.addAttribute("dtmpl", dtmpl);
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
		SideMenuLevel2Menu menu = menuService.getLevel2Menu(menuId);
		String moduleName = menu.getTemplateModule();
    	 try {
    		 composite.getMap().remove(KEY_FUSE_MODE);
    		 if(Boolean.TRUE.equals(fuseMode)) {
    			 mService.fuseEntity(moduleName, composite.getMap());
    		 }else {
    			 mService.mergeEntity(moduleName, composite.getMap());
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
		SideMenuLevel2Menu menu = menuService.getLevel2Menu(menuId);
    	JSONObjectResponse response = new JSONObjectResponse();
    	try {
			List<EntityHistoryItem> historyItems = mService.queryHistory(menu.getTemplateModule(), code, pageNo, pageSize);
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
		try {
			mService.deleteEntity(code);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	
}
