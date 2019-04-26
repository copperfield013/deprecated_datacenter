package cn.sowell.datacenter.api2.controller.meta;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.api2.controller.Api2Constants;
import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.model.api2.service.MetaJsonService;
import cn.sowell.datacenter.model.api2.service.TemplateJsonParseService;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateTreeNode;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateTreeTemplate;
import cn.sowell.dataserver.model.tmpl.service.DetailTemplateService;
import cn.sowell.dataserver.model.tmpl.service.TemplateGroupService;
import cn.sowell.dataserver.model.tmpl.service.TreeTemplateService;

@Controller
@RequestMapping(Api2Constants.URI_TMPL)
public class Api2TemplateController {
	@Resource
	AuthorityService authService;
	
	@Resource
	TemplateGroupService tmplGroupService;
	
	@Resource
	DetailTemplateService dtmplService;
	
	@Resource
	TreeTemplateService treeService;
	
	@Resource
	ModulesService mService;
	
	@Resource
	MetaJsonService metaService;
	
	@Resource
	TemplateJsonParseService tJsonService;
	
	@ResponseBody
	@RequestMapping({
		"/dtmpl_config/{contextType:normal}/{menuId}",
		"/dtmpl_config/{contextType:rabc}/{menuId}/{fieldGroupId}",
		"/dtmpl_config/{contextType:node}/{menuId}/{nodeId}"
	})
	public ResponseJSON detailTemplateConfig(
			@PathVariable String contextType,
			@PathVariable Long menuId,
			@PathVariable(required=false) Long fieldGroupId,
			@PathVariable(required=false) Long nodeId,
			ApiUser user) {
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		TemplateGroup tmplGroup = null;
		if("normal".equals(contextType)) {
			tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		}else if("rabc".equals(contextType)) {
			TemplateDetailFieldGroup fieldGroup = dtmplService.getFieldGroup(fieldGroupId);
			tmplGroup = tmplGroupService.getTemplate(fieldGroup.getRabcTemplateGroupId());
		}else if("node".equals(contextType)) {
			TemplateTreeNode node = treeService.getNodeTemplate(menu.getTemplateModule(), nodeId);
			tmplGroup = tmplGroupService.getTemplate(node.getTemplateGroupId());
		}
		JSONObjectResponse jRes = new JSONObjectResponse();
		jRes.put("config", tJsonService.toDetailTemplateConfig(tmplGroup));
		jRes.put("menu", metaService.toMenuJson(menu));
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/select_config/{menuId}/{fieldGroupId}")
	public ResponseJSON selectConfig(@PathVariable Long menuId, 
			@PathVariable Long fieldGroupId, ApiUser user) {
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateDetailFieldGroup fieldGroup = dtmplService.getFieldGroup(fieldGroupId);
		jRes.put("config", tJsonService.toSelectConfig(fieldGroup));
		jRes.put("menu", metaService.toMenuJson(menu));
		return jRes;
	}
	
	
	@ResponseBody
	@RequestMapping("/ttmpl/{ttmplId}")
	public ResponseJSON getTreeTemplate(@PathVariable Long ttmplId, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateTreeTemplate ttmpl = treeService.getTemplate(ttmplId);
		jRes.put("ttmpl", ttmpl);
		return jRes;
	}
}
