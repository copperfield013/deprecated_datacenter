package cn.sowell.datacenter.model.config.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.config.dao.ConfigureDao;
import cn.sowell.datacenter.model.config.pojo.ConfigModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuModuleTempalteGroup;

@Repository
public class ConfigureDaoImpl implements ConfigureDao{

	@Resource
	SessionFactory sFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SideMenuModule> getSideMenuModules() {
		String hql = "from SideMenuModule m order by m.order asc";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, List<SideMenuModuleTempalteGroup>> getSideMenuModuleTemplateGroupsMap(
			Set<Long> sideMenuModuleIds) {
		if(sideMenuModuleIds != null && !sideMenuModuleIds.isEmpty()) {
			String hql = "from SideMenuModuleTempalteGroup g where g.sideMenuModuleId in (:moduleIds) order by g.order asc";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("moduleIds", sideMenuModuleIds);
			List<SideMenuModuleTempalteGroup> groups = query.list();
			return CollectionUtils.toListMap(groups, group->group.getSideMenuModuleId());
		}else {
			return new HashMap<>();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ConfigModule> getConfigModule(Set<String> moduleNames) {
		if(moduleNames != null && !moduleNames.isEmpty()) {
			String hql = "from ConfigModule m where m.name in (:moduleNames) and m.disabled is null";
			Query query = sFactory.getCurrentSession().createQuery(hql);
			query.setParameterList("moduleNames", moduleNames);
			List<ConfigModule> modules = query.list();
			return CollectionUtils.toMap(modules, module->module.getName());
		}else {
			return new HashMap<>();
		}
	}

}
