package cn.sowell.datacenter.model.config.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.datacenter.model.config.pojo.ConfigModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2;

public interface ConfigureDao {

	List<SideMenuLevel1Menu> getSideMenuModules();

	Map<Long, List<SideMenuLevel2>> getSideMenuLevel2Map(Set<Long> sideMenuModuleIds);

	Map<String, ConfigModule> getConfigModule(Set<String> moduleNames);

}
