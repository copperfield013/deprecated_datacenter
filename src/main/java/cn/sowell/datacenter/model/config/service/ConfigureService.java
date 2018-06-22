package cn.sowell.datacenter.model.config.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.entityResolver.config.abst.Module;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;

public interface ConfigureService {

	List<SideMenuLevel1Menu> getSideMenuLevelMenus(UserIdentifier user);

	void updateSideMenuModules(UserIdentifier user, List<SideMenuLevel1Menu> modules);

	JSONObject getModuleConfigJson();

	List<Module> getEnabledModules();
	
}
