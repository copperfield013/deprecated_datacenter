package cn.sowell.datacenter.api.controller.menu;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.model.admin.service.AdminUserService;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.datacenter.model.config.service.NonAuthorityException;
import cn.sowell.datacenter.model.config.service.SideMenuService;

@Controller
@RequestMapping("/api/menu")
public class ApiMenuController {

	@Resource
	SideMenuService menuService;
	
	@Resource
	AuthorityService authService;
	
	@Resource
	AdminUserService userService;
	
	@ResponseBody
	@RequestMapping("/getMenu")
	public ResponseJSON getMenu() {
		JSONObjectResponse res = new JSONObjectResponse();
		UserDetails user = userService.loadUserByUsername("admin");
		List<SideMenuLevel1Menu> menus = menuService.getSideMenuLevelMenus((UserIdentifier) user);
		menus = menus.stream().filter(menu->{
			try {
				authService.vaidateUserL1MenuAccessable(user, menu.getId());
			} catch (NonAuthorityException e) {
				return false;
			}
			return true;
		}).collect(Collectors.toList());
		res.put("menus", JSON.toJSON(menus));
		return res;
	}
}
