package cn.sowell.datacenter.admin.controller.tmpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.entityResolver.ModuleConfigStructure;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/tree")
public class AdminTreeTemplateController {
	
	@Resource
	ModulesService mService;
	
	//TreeTemplateService treeService;
	
	
	@RequestMapping("/list/{moduleName}")
	public String list(@PathVariable String moduleName, Model model) {
		ModuleMeta module = mService.getModule(moduleName);
		model.addAttribute("module", module);
		return AdminConstants.JSP_TMPL_TREE + "/ttmpl_list.jsp";
	}
	
	@RequestMapping("/add/{moduleName}")
	public String add(@PathVariable String moduleName, Model model) {
		ModuleMeta module = mService.getModule(moduleName);
		ModuleConfigStructure configStructure = mService.getModuleConfigStructure(moduleName);
		model.addAttribute("configStructure", configStructure);
		model.addAttribute("configStructureJson", configStructure.toJson());
		model.addAttribute("module", module);
		return AdminConstants.JSP_TMPL_TREE + "/ttmpl_update.jsp";
	}
	
	@RequestMapping("/update/{moduleName}/{ttmplId}")
	public String update(@PathVariable String moduleName, Long ttmplId, Model model) {
		ModuleMeta module = mService.getModule(moduleName);
		model.addAttribute("module", module);
		return AdminConstants.JSP_TMPL_TREE + "/ttmpl_update.jsp";
	}
	
	@RequestMapping("/save/{moduleName}")
	public ResponseJSON save() {
		JSONObjectResponse jRes = new JSONObjectResponse();
		return jRes;
	}
}
