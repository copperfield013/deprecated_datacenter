package cn.sowell.datacenter.admin.controller.main;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.service.SideMenuService;

@Controller
@RequestMapping("/admin")
public class  AdminMainController {
	
	@Resource
	SideMenuService menuService;
	
	
	@RequestMapping("/login")
	public String login(@RequestParam(name="error",required=false) String error, Model model){
		model.addAttribute("error", error);
		model.addAttribute("errorMap", AdminConstants.ERROR_CODE_MAP);
		return "/admin/common/login.jsp";
	}
	
	@RequestMapping({"/", ""})
	public String index(Model model){
		UserIdentifier user = UserUtils.getCurrentUser();
		List<SideMenuLevel1Menu> menus = menuService.getSideMenuLevelMenus(user);
		model.addAttribute("menus", menus);
		return "/admin/index.jsp";
	}
	
}
