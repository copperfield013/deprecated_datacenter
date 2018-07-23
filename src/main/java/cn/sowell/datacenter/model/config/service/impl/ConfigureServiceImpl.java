package cn.sowell.datacenter.model.config.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.entityResolver.config.ModuleConfigureMediator;
import cn.sowell.datacenter.entityResolver.config.abst.Module;
import cn.sowell.datacenter.entityResolver.config.param.QueryModuleCriteria;
import cn.sowell.datacenter.model.config.dao.ConfigureDao;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Service
public class ConfigureServiceImpl implements ConfigureService{

	@Resource
	ConfigureDao cDao;
	
	@Resource
	NormalOperateDao nDao;
	
	@Resource
	ModuleConfigureMediator moduleConfigMediator;
	
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
