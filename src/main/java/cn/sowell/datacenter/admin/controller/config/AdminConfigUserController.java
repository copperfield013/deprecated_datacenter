package cn.sowell.datacenter.admin.controller.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.NoticeType;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.common.RequestParameterMapComposite;
import cn.sowell.datacenter.entityResolver.FieldDescCacheMap;
import cn.sowell.datacenter.entityResolver.FusionContextConfig;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.datacenter.model.config.service.ConfigUserService;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.ViewDataService;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_CONFIG + "/user")
public class AdminConfigUserController {
	
	@Resource
	ConfigUserService userService;
	
	@Resource
	TemplateService tService;
	
	@Resource
	ViewDataService vService;
	
	@Resource
	ModulesService mService;
	
	static Logger logger = Logger.getLogger(AdminConfigUserController.class);
	
	
	@RequestMapping({"/detail/{tmplId}", "/detail", "/detail/"})
	public String detail(@PathVariable(required=false) Long tmplId, 
			Long historyId, Model model) {
		ABCUser user = UserUtils.getCurrentUser(ABCUser.class);
		if(user != null) {
			TemplateDetailTemplate dtmpl = userService.getUserDetailTemplate(tmplId);
			if(dtmpl != null){
				ModuleEntityPropertyParser entity = userService.getUserEntity(user, historyId);
				model.addAttribute("dtmpl", dtmpl);
				model.addAttribute("user", user);
				model.addAttribute("entity", entity);
			}
		}
		return AdminConstants.JSP_CONFIG_USER + "/user_detail.jsp";
	}
	
	@RequestMapping({"/update/{tmplId}", "/update", "/update/"})
	public String update(@PathVariable(required=false) Long tmplId, Model model) {
		ABCUser user = UserUtils.getCurrentUser(ABCUser.class);
		if(user != null) {
			TemplateDetailTemplate dtmpl = userService.getUserDetailTemplate(tmplId);
			if(dtmpl != null){
				ModuleEntityPropertyParser entity = userService.getUserEntity(user, null);
				FusionContextConfig config = userService.getUserModuleConfig();
				model.addAttribute("module", mService.getModule(config.getModule()));
				model.addAttribute("config", config);
				model.addAttribute("dtmpl", dtmpl);
				model.addAttribute("user", user);
				model.addAttribute("entity", entity);
				model.addAttribute("fieldDescMap", new FieldDescCacheMap(config.getConfigResolver()));
				return AdminConstants.JSP_CONFIG_USER + "/user_update.jsp";
			}
		}
		return null;
	}
	
	@ResponseBody
    @RequestMapping({"/save"})
    public AjaxPageResponse save(
    		RequestParameterMapComposite composite){
    	 try {
    		 userService.mergeUserEntity(composite.getMap(), UserUtils.getCurrentUser(ABCUser.class));
    		 AjaxPageResponse res = new AjaxPageResponse();
    		 res.setNoticeType(NoticeType.SUC);
    		 res.setNotice("保存成功");
    		 res.setLocalPageRedirectURL("admin/config/user/detail");
    		 return res;
         } catch (Exception e) {
             logger.error("保存时发生错误", e);
             return AjaxPageResponse.FAILD("保存失败");
         }
    }
	
	@RequestMapping("/open_selection/{stmplId}")
	public String openSelection(
			@PathVariable Long stmplId, 
			String exists, 
			PageInfo pageInfo,
			HttpServletRequest request, 
			Model model) {
		TemplateSelectionTemplate stmpl = tService.getSelectionTemplate(stmplId);
		userService.validateUserAuthentication(stmpl.getModule());
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
		
		model.addAttribute("stmpl", stmpl);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_MODULES + "/user_relation_selection.jsp";
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
	
}
