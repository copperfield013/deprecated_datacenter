package cn.sowell.datacenter.model.config.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.config.pojo.SideMenuModule;

public interface ConfigureService {

	List<SideMenuModule> getSideMenuModules(UserIdentifier user);

	void updateSideMenuModules(UserIdentifier user, List<SideMenuModule> modules);

	JSONObject getModuleConfigJson();
	
}
