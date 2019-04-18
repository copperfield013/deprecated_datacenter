package cn.sowell.datacenter.jv.controller.entity;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.jv.JvConstants;
import cn.sowell.datacenter.model.config.service.AuthorityService;

@Controller
@RequestMapping(JvConstants.URI_ENTITY + "/curd")
public class JvEntityCurdController {
	@Resource
	AuthorityService authService;
	
	@RequestMapping("/list/{menuId}")
	public String list(@PathVariable Long menuId, ApiUser user, Model model) {
		authService.validateUserL2MenuAccessable(user, menuId);
		model.addAttribute("menuId", menuId);
		return JvConstants.JSP_ENTITY + "/entity_list.jsp";
	}
	
	@RequestMapping("/tree/{menuId}")
	public String index(@PathVariable Long menuId, ApiUser user, Model model) {
		authService.validateUserL2MenuAccessable(user, menuId);
		model.addAttribute("menuId", menuId);
		return JvConstants.JSP_ENTITY + "/entity_tree.jsp";
	}
	
	@RequestMapping("/detail/{menuId}/{code}")
	public String detail(@PathVariable Long menuId, @PathVariable String code, ApiUser user, Model model) {
		authService.validateUserL2MenuAccessable(user, menuId);
		model.addAttribute("menuId", menuId);
		model.addAttribute("code", code);
		model.addAttribute("mode", "detail");
		return JvConstants.JSP_ENTITY + "/entity_detail.jsp";
	}
	
	
	@RequestMapping("/add/{menuId}")
	public String add(@PathVariable Long menuId, ApiUser user, Model model) {
		authService.validateUserL2MenuAccessable(user, menuId);
		model.addAttribute("menuId", menuId);
		model.addAttribute("mode", "add");
		return JvConstants.JSP_ENTITY + "/entity_detail.jsp";
	}
	
	@RequestMapping("/update/{menuId}/{code}")
	public String update(@PathVariable Long menuId, @PathVariable String code, ApiUser user, Model model) {
		authService.validateUserL2MenuAccessable(user, menuId);
		model.addAttribute("menuId", menuId);
		model.addAttribute("code", code);
		model.addAttribute("mode", "update");
		return JvConstants.JSP_ENTITY + "/entity_detail.jsp";
	}
	
	
	
	
}
