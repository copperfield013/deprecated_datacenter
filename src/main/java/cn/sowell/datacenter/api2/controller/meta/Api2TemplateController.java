package cn.sowell.datacenter.api2.controller.meta;

import java.util.stream.Collectors;

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
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroupAction;
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
	
	@ResponseBody
	@RequestMapping("/dtmpl/{menuId}")
	public ResponseJSON detailTemplate(@PathVariable Long menuId, ApiUser user) {
		SideMenuLevel2Menu menu = authService.validateUserL2MenuAccessable(user, menuId);
		ModuleMeta module = mService.getModule(menu.getTemplateModule());
		TemplateGroup tmplGroup = tmplGroupService.getTemplate(menu.getTemplateGroupId());
		TemplateDetailTemplate dtmpl = dtmplService.getTemplate(tmplGroup.getDetailTemplateId());
		
		JSONObjectResponse jRes = new JSONObjectResponse();
		jRes.put("module", metaService.toModuleJson(module));
		jRes.put("menu", metaService.toMenuJson(menu));
		jRes.put("dtmpl", dtmpl);
		jRes.put("premises", tmplGroup.getPremises());
		jRes.put("buttonStatus", metaService.toButtonStatus(tmplGroup));
		if(tmplGroup.getActions() != null) {
			jRes.put("actions", tmplGroup.getActions().stream()
					.filter(action->TemplateGroupAction.ACTION_FACE_DETAIL.equals(action.getFace()))
					.collect(Collectors.toList()));
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/ttmpl/{ttmplId}")
	public ResponseJSON getTreeTemplate(@PathVariable Long ttmplId) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateTreeTemplate ttmpl = treeService.getTemplate(ttmplId);
		jRes.put("ttmpl", ttmpl);
		return jRes;
	}
}
