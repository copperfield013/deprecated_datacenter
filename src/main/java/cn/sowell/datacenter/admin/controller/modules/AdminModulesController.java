package cn.sowell.datacenter.admin.controller.modules;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.abc.resolver.EntityPropertyParser;
import cn.sowell.datacenter.model.abc.resolver.FusionContextFactoryDC;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.dict.service.DictionaryService;
import cn.sowell.datacenter.model.modules.ModuleConstants;
import cn.sowell.datacenter.model.modules.pojo.EntityHistoryItem;
import cn.sowell.datacenter.model.modules.pojo.ModuleMeta;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.tmpl.bean.QueryEntityParameter;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateGroup;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

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
	FusionContextFactoryDC fFactory;
	
	Logger logger = Logger.getLogger(AdminModulesController.class);

	
	/**
	 * 通用各个模块的列表入口
	 * @return
	 */
	@RequestMapping("/list/{module}")
	public String list(
			@PathVariable String module,
			Long tmplId, 
			@RequestParam(name="tg", required=false) String templateGroupKey,
			PageInfo pageInfo,
			Model model,
			HttpServletRequest request, HttpSession session) {
		ModuleMeta moduleMeta = mService.getModule(module);
		Assert.notNull(moduleMeta);
		if(TextUtils.hasText(templateGroupKey)) {
			TemplateGroup tGroup = tService.getTemplateGroup(module, templateGroupKey);
			if(tGroup != null) {
				tmplId = tGroup.getListTemplateId();
				model.addAttribute("templateGroup", tGroup);
			}
		}
		ListTemplateParameter param = mService.exractTemplateParameter(tmplId, module, request);
		if(param.getListTemplate() != null){
			QueryEntityParameter queryParam = new QueryEntityParameter();
			queryParam.setCriterias(mService.toCriterias(param.getNormalCriteriaMap().values(), module));
			queryParam.setModule(module);
			queryParam.setPageInfo(pageInfo);
			List<EntityPropertyParser> parserList = mService.queryEntities(queryParam);
			model.addAttribute("parserList", parserList);
			
			if(moduleMeta.hasFunction(ModuleConstants.FUNCTION_EXPORT)) {
				String uuid = (String) session.getAttribute(AdminConstants.EXPORT_PEOPLE_STATUS_UUID);
				if(uuid != null){
					ExportStatus exportStatus = eService.getExportStatus(uuid);
					if(exportStatus != null && !exportStatus.isBreaked() && !exportStatus.isCompleted()){
						model.addAttribute("exportStatus", exportStatus);
					}
				}
			}
			if(param.getListTemplate().getCriterias() != null){
				model.addAttribute("criteriaOptionsMap", dictService.getOptionsMap(CollectionUtils.toSet(param.getListTemplate().getCriterias(), criteria->criteria.getFieldId())));
			}
		}
		model.addAttribute("vCriteriaMap", param.getNormalCriteriaMap());
		model.addAttribute("ltmpl", param.getListTemplate());
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("ltmplList", tService.queryLtmplList(module, param.getUser()));
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_MODULES + "/modules_list_tmpl.jsp";
	}
	
	@RequestMapping("/detail/{module}/{code}")
	public String detail(@PathVariable String code, 
			@PathVariable String module, 
			Long tmplId,
			@RequestParam(name="tg", required=false) String templateGroupKey,
			String datetime, 
    		Long timestamp,
    		Model model) {
		Object moduleMeta = mService.getModule(module);
		Date date = null;
        date = dateFormat.parse(datetime);
        if(timestamp != null){
        	date = new Date(timestamp);
        }
        UserIdentifier user = UserUtils.getCurrentUser();
        if(TextUtils.hasText(templateGroupKey)) {
			TemplateGroup tGroup = tService.getTemplateGroup(module, templateGroupKey);
			if(tGroup != null) {
				tmplId = tGroup.getDetailTemplateId();
				model.addAttribute("templateGroup", tGroup);
			}
		}
        TemplateDetailTemplate dtmpl = coalesceDetailTempalte(tmplId, user, module);
        List<TemplateDetailTemplate> dtmpls = tService.getAllDetailTemplateList(module, user, null, false);
        EntityPropertyParser entity = mService.getEntity(module, code, date);
        model.addAttribute("date", date);
        model.addAttribute("entity", entity);
        model.addAttribute("datetime", datetime);
        model.addAttribute("dtmpl", dtmpl);
        model.addAttribute("dtmpls", dtmpls);
        model.addAttribute("timestamp", timestamp);
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_MODULES + "/modules_detail_tmpl.jsp";
		
	}
	
	@RequestMapping("/add/{module}")
	public String add(@PathVariable String module, Long tmplId, 
			@RequestParam(name="tg", required=false) String templateGroupKey, Model model) {
		ModuleMeta mMeta = mService.getModule(module);
		if(TextUtils.hasText(templateGroupKey)) {
			TemplateGroup tGroup = tService.getTemplateGroup(module, templateGroupKey);
			if(tGroup != null) {
				tmplId = tGroup.getDetailTemplateId();
				model.addAttribute("templateGroup", tGroup);
			}
		}
		UserIdentifier user = UserUtils.getCurrentUser();
		TemplateDetailTemplate dtmpl = coalesceDetailTempalte(tmplId, user, module);
		model.addAttribute("dtmpl", dtmpl);
		model.addAttribute("dtmpls", tService.getAllDetailTemplateList(module, user, null, false));
		model.addAttribute("module", mMeta);
		model.addAttribute("config", fFactory.getDefaultConfig(module));
		return AdminConstants.JSP_MODULES + "/modules_update_tmpl.jsp";
	}
	
	
	@RequestMapping("/update/{module}/{code}")
	public String update(
			@PathVariable String module,
			@PathVariable String code,
			@RequestParam(name="tg", required=false) String templateGroupKey,
			Long tmplId,
			Model model
			) {
		ModuleMeta mMeta = mService.getModule(module);
		if(TextUtils.hasText(templateGroupKey)) {
			TemplateGroup tGroup = tService.getTemplateGroup(module, templateGroupKey);
			if(tGroup != null) {
				tmplId = tGroup.getDetailTemplateId();
				model.addAttribute("templateGroup", tGroup);
			}
		}
		UserIdentifier user = UserUtils.getCurrentUser();
		EntityPropertyParser entity = mService.getEntity(module, code, null);
		TemplateDetailTemplate dtmpl = coalesceDetailTempalte(tmplId, user, module);
		model.addAttribute("entity", entity);
		model.addAttribute("module", mMeta);
		model.addAttribute("dtmpl", dtmpl);
		model.addAttribute("dtmpls", tService.getAllDetailTemplateList(module, user, null, false));
		model.addAttribute("config", fFactory.getDefaultConfig(module));
		return AdminConstants.JSP_MODULES + "/modules_update_tmpl.jsp";
	}
	
	
	@ResponseBody
    @RequestMapping("/save/{module}")
    public AjaxPageResponse save(@PathVariable String module, String code, @RequestParam Map<String, Object> map){
    	 try {
    		 mService.mergeEntity(module, map);
             return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("保存成功", module + "_list_tmpl");
         } catch (Exception e) {
             logger.error("保存时发生错误", e);
             return AjaxPageResponse.FAILD("保存失败");
         }
    }
	
	
	@ResponseBody
    @RequestMapping("/paging_history/{module}/{code}")
    public JSONObjectResponse pagingHistory(
    		@PathVariable String module,
    		@PathVariable String code, 
    		@RequestParam Integer pageNo, 
    		@RequestParam(defaultValue="100") Integer pageSize){
    	JSONObjectResponse response = new JSONObjectResponse();
    	try {
			List<EntityHistoryItem> historyItems = mService.queryHistory(module, code, pageNo, pageSize);
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
	@RequestMapping("/delete/{module}/{code}")
	public AjaxPageResponse delete(
			@PathVariable String module,
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
	
	
	
	
	

	private TemplateDetailTemplate coalesceDetailTempalte(Long tmplId, UserIdentifier user, String module) {
    	if(tmplId == null){
			return tService.getDefaultDetailTemplate(user, module);
    	}else{
    		return tService.getDetailTemplate(tmplId);
    	}
	}
	
}
