package cn.sowell.datacenter.model.config.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.entityResolver.config.ModuleConfigureMediator;
import cn.sowell.datacenter.entityResolver.config.abst.Module;
import cn.sowell.datacenter.entityResolver.config.param.QueryModuleCriteria;
import cn.sowell.datacenter.model.config.dao.ConfigureDao;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2;
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
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<SideMenuLevel1Menu> getSideMenuLevelMenus(UserIdentifier user) {
		List<SideMenuLevel1Menu> modules = cDao.getSideMenuModules();
		
		Map<Long, List<SideMenuLevel2>> groupsMap 
					= cDao.getSideMenuLevel2Map(CollectionUtils.toSet(modules, module->module.getId()));
		modules.forEach(module->module.setLevel2s(groupsMap.get(module.getId())));
		return modules;
	}
	
	@Override
	public void updateSideMenuModules(UserIdentifier user, List<SideMenuLevel1Menu> modules) {
		List<SideMenuLevel1Menu> originModules = getSideMenuLevelMenus(user);
		CollectionUpdateStrategy<SideMenuLevel1Menu> updateModules = 
				new CollectionUpdateStrategy<>(SideMenuLevel1Menu.class, nDao,
						module->module.getId());
		CollectionUpdateStrategy<SideMenuLevel2> updateGroups = 
				new CollectionUpdateStrategy<>(SideMenuLevel2.class, nDao,
						group->group.getId());
		updateModules.setBeforeUpdate((origin, module)->{
					origin.setTitle(module.getTitle());
					origin.setOrder(module.getOrder());
				});
		updateModules.setAfterUpdate((origin, module)->{
			module.getLevel2s().forEach(group->group.setSideMenuLevel1Id(origin.getId()));
			updateGroups.doUpdate(origin.getLevel2s(), module.getLevel2s());
		});
		updateModules.setAfterCreate(module->{
			module.getLevel2s().forEach(group->group.setSideMenuLevel1Id(module.getId()));
			updateGroups.doUpdate(null, module.getLevel2s());
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
	public List<Module> getEnabledModules(){
		QueryModuleCriteria criteria = new QueryModuleCriteria();
		criteria.setFilterDisabled(true);
		List<Module> modules = moduleConfigMediator.queryModules(criteria);
		return modules;
	}
	
	
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
