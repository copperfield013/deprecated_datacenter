package cn.sowell.datacenter.admin.controller.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.config.pojo.SideMenuModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuModuleTempalteGroup;
import cn.sowell.datacenter.model.config.service.ConfigureService;

@Controller
@RequestMapping(AdminConstants.URI_CONFIG + "/sidemenu")
public class AdminConfigSidemenuController {
	
	@Resource
	ConfigureService configService;
	
	Logger logger = Logger.getLogger(AdminConfigSidemenuController.class);
	
	
	@RequestMapping({"", "/"})
	public String main(Model model) {
		UserIdentifier user = UserUtils.getCurrentUser();
		List<SideMenuModule> modules = configService.getSideMenuModules(user);
		JSONObject config = configService.getModuleConfigJson();
		model.addAttribute("config", config);
		model.addAttribute("modules", modules);
		return AdminConstants.JSP_CONFIG_SIDEMENU + "/sidemenu_main.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON save(@RequestBody JsonRequest jReq) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		JSONObject req = jReq.getJsonObject();
		try {
			List<SideMenuModule> modules = toMenuModules(req);
			Set<String> moduleNames = new HashSet<>();
			for (SideMenuModule module : modules) {
				if(moduleNames.contains(module.getModuleName())) {
					jRes.setStatus("duplicateModule");
					return jRes;
				}else {
					moduleNames.add(module.getModuleName());
				}
			}
			configService.updateSideMenuModules(UserUtils.getCurrentUser(), modules);
			jRes.setStatus("suc");
		} catch (Exception e) {
			jRes.setStatus("error");
			logger.error("更新功能菜单时发生错误", e);
		}
		return jRes;
	}

	private List<SideMenuModule> toMenuModules(JSONObject req) {
		JSONArray jModules = req.getJSONArray("modules");
		List<SideMenuModule> modules = new ArrayList<>();
		for (Object x : jModules) {
			JSONObject jModule = (JSONObject) x;
			SideMenuModule module = new SideMenuModule();
			module.setId(jModule.getLong("id"));
			module.setTitle(jModule.getString("title"));
			module.setOrder(jModule.getInteger("order"));
			module.setModuleName(jModule.getString("moduleName"));
			module.setGroups(new ArrayList<>());
			JSONArray jGroups = jModule.getJSONArray("groups");
			for (Object y : jGroups) {
				JSONObject jGroup = (JSONObject) y;
				SideMenuModuleTempalteGroup group = new SideMenuModuleTempalteGroup();
				group.setId(jGroup.getLong("id"));
				group.setTitle(jGroup.getString("title"));
				group.setOrder(jGroup.getInteger("order"));
				group.setTemplateGroupId(jGroup.getLong("tmplGroupId"));
				if(Long.valueOf(0).equals(group.getTemplateGroupId())) {
					group.setIsDefault(1);
					group.setTemplateGroupId(null);
				}
				module.getGroups().add(group);
			}
			modules.add(module);
		}
		return modules;
	}
}
