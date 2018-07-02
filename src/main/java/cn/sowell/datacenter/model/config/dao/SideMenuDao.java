package cn.sowell.datacenter.model.config.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;

public interface SideMenuDao {

	List<SideMenuLevel1Menu> getSideMenus();

	Map<Long, List<SideMenuLevel2Menu>> getSideMenuLevel2Map(Set<Long> sideMenuModuleIds);
	
	SideMenuLevel2Menu getLevel2Menu(Long menuId);

}
