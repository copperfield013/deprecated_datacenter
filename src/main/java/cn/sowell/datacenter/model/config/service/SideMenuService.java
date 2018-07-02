package cn.sowell.datacenter.model.config.service;

import java.util.List;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;

public interface SideMenuService {
	SideMenuLevel2Menu getLevel2Menu(Long menuId);

	List<SideMenuLevel1Menu> getSideMenuLevelMenus(UserIdentifier user);

	void updateSideMenuModules(UserIdentifier user, List<SideMenuLevel1Menu> modules);
}
