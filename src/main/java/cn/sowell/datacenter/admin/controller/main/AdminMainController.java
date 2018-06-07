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
import cn.sowell.datacenter.model.config.pojo.SideMenuModule;
import cn.sowell.datacenter.model.config.service.ConfigureService;

@Controller
@RequestMapping("/admin")
public class  AdminMainController {
	
	@Resource
	ConfigureService configService;
	
	
	@RequestMapping("/login")
	public String login(@RequestParam(name="error",required=false) String error, Model model){
		model.addAttribute("error", error);
		model.addAttribute("errorMap", AdminConstants.ERROR_CODE_MAP);
		return "/admin/common/login.jsp";
	}
	
	@RequestMapping({"/", ""})
	public String index(Model model){
		UserIdentifier user = UserUtils.getCurrentUser();
		List<SideMenuModule> modules = configService.getSideMenuModules(user);
		model.addAttribute("modules", modules);
		return "/admin/index.jsp";
	}
	
}
