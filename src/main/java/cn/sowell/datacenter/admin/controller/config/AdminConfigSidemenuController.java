package cn.sowell.datacenter.admin.controller.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.entityResolver.config.abst.Module;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateGroup;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_CONFIG + "/sidemenu")
public class AdminConfigSidemenuController {
	
	@Resource
	ConfigureService configService;
	
	@Resource
	ModulesService mService;
	
	@Resource
	TemplateService tService;
	
	Logger logger = Logger.getLogger(AdminConfigSidemenuController.class);
	
	
	@RequestMapping({"", "/"})
	public String main(Model model) {
		UserIdentifier user = UserUtils.getCurrentUser();
		List<SideMenuLevel1Menu> menus = configService.getSideMenuLevelMenus(user);
		List<Module> modules = configService.getEnabledModules();
		Map<String, List<TemplateGroup>> tmplGroupsMap = tService.queryTemplateGroups(CollectionUtils.toSet(modules, module->module.getName()));
		JSONObject config = configService.getModuleConfigJson();
		model.addAttribute("config", config);
		model.addAttribute("modules", modules);
		model.addAttribute("menus", menus);
		model.addAttribute("tmplGroupsMap", tmplGroupsMap);
		return AdminConstants.JSP_CONFIG_SIDEMENU + "/sidemenu_main.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON save(@RequestBody JsonRequest jReq) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		JSONObject req = jReq.getJsonObject();
		try {
			List<SideMenuLevel1Menu> modules = toMenuModules(req);
			configService.updateSideMenuModules(UserUtils.getCurrentUser(), modules);
			jRes.setStatus("suc");
		} catch (Exception e) {
			jRes.setStatus("error");
			logger.error("更新功能菜单时发生错误", e);
		}
		return jRes;
	}

	private List<SideMenuLevel1Menu> toMenuModules(JSONObject req) {
		JSONArray jModules = req.getJSONArray("modules");
		List<SideMenuLevel1Menu> modules = new ArrayList<>();
		for (Object x : jModules) {
			JSONObject jModule = (JSONObject) x;
			SideMenuLevel1Menu module = new SideMenuLevel1Menu();
			module.setId(jModule.getLong("id"));
			module.setTitle(jModule.getString("title"));
			module.setOrder(jModule.getInteger("order"));
			module.setLevel2s(new ArrayList<>());
			JSONArray jGroups = jModule.getJSONArray("groups");
			for (Object y : jGroups) {
				JSONObject jGroup = (JSONObject) y;
				SideMenuLevel2 group = new SideMenuLevel2();
				group.setId(jGroup.getLong("id"));
				group.setTitle(jGroup.getString("title"));
				group.setOrder(jGroup.getInteger("order"));
				group.setTemplateGroupId(jGroup.getLong("tmplGroupId"));
				if(Long.valueOf(0).equals(group.getTemplateGroupId())) {
					group.setIsDefault(1);
					group.setTemplateGroupId(null);
				}
				module.getLevel2s().add(group);
			}
			modules.add(module);
		}
		return modules;
	}
}
