package cn.sowell.datacenter.model.config.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.entityResolver.config.ModuleConfigureMediator;
import cn.sowell.datacenter.entityResolver.config.abst.Module;
import cn.sowell.datacenter.entityResolver.config.param.QueryModuleCriteria;
import cn.sowell.datacenter.model.config.dao.ConfigureDao;
import cn.sowell.datacenter.model.config.pojo.ConfigModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuModule;
import cn.sowell.datacenter.model.config.pojo.SideMenuModuleTempalteGroup;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateGroup;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Service
public class ConfigureServiceImpl implements ConfigureService{

	@Resource
	ConfigureDao cDao;
	
	@Resource
	NormalOperateDao nDao;
	
	@Resource
	ModuleConfigureMediator moduleConfigMediator;
	
	@Override
	public List<SideMenuModule> getSideMenuModules(UserIdentifier user) {
		List<SideMenuModule> modules = cDao.getSideMenuModules();
		Map<String, ConfigModule> configModuleMap = cDao.getConfigModule(CollectionUtils.toSet(modules, module->module.getModuleName()));
		Iterator<SideMenuModule> itr = modules.iterator();
		while(itr.hasNext()) {
			SideMenuModule module = itr.next();
			ConfigModule cModule = configModuleMap.get(module.getModuleName());
			if(cModule == null) {
				//如果没有找到对应的ConfigModule,那么该模块不可用，直接移除
				itr.remove();
			}else {
				module.setConfigModule(cModule);
				module.setTitle(cModule.getTitle());
			}
		}
		
		Map<Long, List<SideMenuModuleTempalteGroup>> groupsMap 
					= cDao.getSideMenuModuleTemplateGroupsMap(CollectionUtils.toSet(modules, module->module.getId()));
		modules.forEach(module->module.setGroups(groupsMap.get(module.getId())));
		return modules;
	}
	
	@Override
	public void updateSideMenuModules(UserIdentifier user, List<SideMenuModule> modules) {
		List<SideMenuModule> originModules = getSideMenuModules(user);
		CollectionUpdateStrategy<SideMenuModule> updateModules = 
				new CollectionUpdateStrategy<>(SideMenuModule.class, nDao,
						module->module.getId());
		CollectionUpdateStrategy<SideMenuModuleTempalteGroup> updateGroups = 
				new CollectionUpdateStrategy<>(SideMenuModuleTempalteGroup.class, nDao,
						group->group.getId());
		updateModules.setBeforeUpdate((origin, module)->{
					origin.setTitle(module.getTitle());
					origin.setOrder(module.getOrder());
				});
		updateModules.setAfterUpdate((origin, module)->{
			module.getGroups().forEach(group->group.setSideMenuModuleId(origin.getId()));
			updateGroups.doUpdate(origin.getGroups(), module.getGroups());
		});
		updateModules.setAfterCreate(module->{
			module.getGroups().forEach(group->group.setSideMenuModuleId(module.getId()));
			updateGroups.doUpdate(null, module.getGroups());
		});
		updateGroups.setBeforeUpdate((o, g)->{
			o.setOrder(g.getOrder());
			o.setTitle(g.getTitle());
		});
		updateModules.doUpdate(originModules, modules);
	}
	
	
	@Resource
	TemplateService tService;
	
	@Override
	public JSONObject getModuleConfigJson() {
		JSONObject jConfig = new JSONObject();
		JSONObject jModules = new JSONObject();
		QueryModuleCriteria criteria = new QueryModuleCriteria();
		criteria.setFilterDisabled(true);
		List<Module> modules = moduleConfigMediator.queryModules(criteria);
		Map<String, List<TemplateGroup>> moduleGroupsMap = tService.queryTemplateGroups(CollectionUtils.toSet(modules, module->module.getName()));
		modules.forEach(module->{
			JSONObject jModule = new JSONObject();
			jModule.put("name", module.getName());
			jModule.put("title", module.getTitle());
			JSONArray jGroups = new JSONArray();
			List<TemplateGroup> groups = moduleGroupsMap.get(module.getName());
			if(groups != null) {
				for (TemplateGroup group : groups) {
					JSONObject jGroup = new JSONObject();
					jGroup.put("title", group.getTitle());
					jGroup.put("id", group.getId());
					jGroups.add(jGroup);
				}
			}
			jModule.put("groups", jGroups);
			jModules.put(module.getName(), jModule);
		});
		jConfig.put("modules", jModules);
		return jConfig;
	}


}
