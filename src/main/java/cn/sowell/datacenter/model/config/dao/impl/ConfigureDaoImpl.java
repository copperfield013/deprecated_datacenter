package cn.sowell.datacenter.model.config.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.dao.deferedQuery.HibernateRefrectResultTransformer;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.config.dao.ConfigureDao;
import cn.sowell.datacenter.model.config.pojo.ConfigModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2;

@Repository
public class ConfigureDaoImpl implements ConfigureDao{

	@Resource
	SessionFactory sFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SideMenuLevel1Menu> getSideMenuModules() {
		String hql = "from SideMenuLevel1Menu m order by m.order asc";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, List<SideMenuLevel2>> getSideMenuLevel2Map(
			Set<Long> sideMenuModuleIds) {
		if(sideMenuModuleIds != null && !sideMenuModuleIds.isEmpty()) {
			String sql = "	SELECT" +
					"		l2.*," +
					"		g.c_title tmplgroup_title," +
					"		g.c_key tmplgroup_key," +
					"		g.c_module tmpl_module," +
					"		m.c_title tmpl_module_title " +
					"	FROM" +
					"		t_config_sidemenu_level2 l2" +
					"		LEFT JOIN t_tmpl_group g ON l2.tmplgroup_id = g.id" +
					"		LEFT JOIN t_config_module m ON g.c_module = m.c_name" +
					"	where l2.sidemenu_level1_id in (:l1Ids)" + 
					"	order by l2.c_order asc";
			
			SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
			query.setParameterList("l1Ids", sideMenuModuleIds, StandardBasicTypes.LONG);
			query.setResultTransformer(HibernateRefrectResultTransformer.getInstance(SideMenuLevel2.class));
			List<SideMenuLevel2> groups = query.list();
			return CollectionUtils.toListMap(groups, group->group.getSideMenuLevel1Id());
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
