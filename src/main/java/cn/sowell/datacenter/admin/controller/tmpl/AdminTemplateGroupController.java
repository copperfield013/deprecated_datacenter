package cn.sowell.datacenter.admin.controller.tmpl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/group")
@PreAuthorize("hasAuthority(@confAuthenService.getAdminConfigAuthen())")
public class AdminTemplateGroupController {
	
	@Resource
	TemplateService tService;
	
	@Resource
	ModulesService  mService;
	
	@Resource
	ConfigureService configService;
	
	Logger logger = Logger.getLogger(AdminTemplateGroupController.class);
	
	@RequestMapping("/list/{module}")
	public String list(@PathVariable String module, Model model) {
		ModuleMeta moduleMeta = mService.getModule(module);
		if(module != null) {
			List<TemplateGroup> tmplGroups = tService.queryTemplateGroups(module);
			model.addAttribute("module", moduleMeta);
			model.addAttribute("tmplGroups", tmplGroups);
			model.addAttribute("modulesJson", configService.getSiblingModulesJson(module));
			return AdminConstants.JSP_TMPL_GROUP + "/tmpl_group_list.jsp";
		}
		return null;
	}
	
	@RequestMapping("/to_create/{module}")
	public String toCreate(@PathVariable String module, Model model) {
		ModuleMeta moduleMeta = mService.getModule(module);
		if(module != null) {
			model.addAttribute("module", moduleMeta);
			return AdminConstants.JSP_TMPL_GROUP + "/tmpl_group_update.jsp";
		}
		return null;
	}
	
	@RequestMapping("/update/{groupId}")
	public String toUpdate(@PathVariable Long groupId, Model model) {
		TemplateGroup group = tService.getTemplateGroup(groupId);
		if(group != null) {
			ModuleMeta module = mService.getModule(group.getModule());
			model.addAttribute("module", module);
			model.addAttribute("group", group);
			model.addAttribute("premisesJson", JSON.toJSON(group.getPremises()));
			return AdminConstants.JSP_TMPL_GROUP + "/tmpl_group_update.jsp";
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public AjaxPageResponse save(TemplateGroup group) {
		Assert.hasText(group.getModule());
		try {
			tService.saveGroup(group, UserUtils.getCurrentUser());
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("保存成功", group.getModule() + "_tmpl_group_list");
		}catch (Exception e) {
			logger.error("保存失败", e);
			if(e instanceof ConstraintViolationException) {
				if("module_key_unique".equalsIgnoreCase(((ConstraintViolationException) e).getConstraintName())) {
					return AjaxPageResponse.FAILD("Key值重复， 保存失败");
				}
			}
			return AjaxPageResponse.FAILD("保存失败");
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/remove/{groupId}")
	public AjaxPageResponse remove(@PathVariable Long groupId) {
		try {
			tService.removeTemplateGroup(groupId);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/copy/{tmplGroupId}/{targetModuleName}")
	public ResponseJSON copy(@PathVariable Long tmplGroupId, @PathVariable String targetModuleName) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		try {
			Long newTmplId = tService.copyTemplateGroup(tmplGroupId, targetModuleName, UserUtils.getCurrentUser());
			if(newTmplId != null) {
				jRes.setStatus("suc");
				jRes.put("newTmplId", newTmplId);
			}
		} catch (Exception e) {
			logger.error("复制模板组合时发生错误", e);
		}
		return jRes;
	}
	
	
	
}
