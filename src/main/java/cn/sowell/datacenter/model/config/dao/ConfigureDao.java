package cn.sowell.datacenter.model.config.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sowell.datacenter.model.config.pojo.ConfigModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuModuleTempalteGroup;

public interface ConfigureDao {

	List<SideMenuModule> getSideMenuModules();

	Map<Long, List<SideMenuModuleTempalteGroup>> getSideMenuModuleTemplateGroupsMap(Set<Long> sideMenuModuleIds);

	Map<String, ConfigModule> getConfigModule(Set<String> moduleNames);

}
