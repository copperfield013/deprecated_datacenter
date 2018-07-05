package cn.sowell.datacenter.model.config.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.config.dao.SideMenuDao;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.SideMenuService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Service
public class SideMenuServiceImpl implements SideMenuService{

	@Resource
	SideMenuDao sDao;
	
	@Resource
	NormalOperateDao nDao;
	
	@Resource
	TemplateService tService;
	
	@Resource
	ModulesService mService;
	
	Map<Long, SideMenuLevel1Menu> l1MenuMap;
	
	Map<Long, SideMenuLevel2Menu> l2MenuMap;
	
	synchronized void reloadMenuMap() {
		l1MenuMap = null;
		l2MenuMap = null;
	}
	
	synchronized Map<Long, SideMenuLevel1Menu> getL1MenuMap(){
		if(l1MenuMap == null) {
			List<SideMenuLevel1Menu> level1s = sDao.getSideMenus();
			
			Map<Long, List<SideMenuLevel2Menu>> groupsMap 
						= sDao.querySideMenuLevel2Map();
			level1s.forEach(level1->{
				List<SideMenuLevel2Menu> level2s = groupsMap.get(level1.getId());
				level1.setLevel2s(level2s);
				if(level2s != null) {
					Iterator<SideMenuLevel2Menu> itr = level2s.iterator();
					while(itr.hasNext()) {
						SideMenuLevel2Menu l2 = itr.next();
						TemplateGroup tmplGroup = tService.getTemplateGroup(l2.getTemplateGroupId());
						if(tmplGroup != null) {
							ModuleMeta module = mService.getModule(tmplGroup.getModule());
							if(module != null) {
								l2.setTemplateGroupTitle(tmplGroup.getTitle());
								l2.setTemplateGroupKey(tmplGroup.getKey());
								l2.setTemplateModuleTitle(module.getTitle());
								l2.setTemplateModule(module.getName());
								l2.setLevel1Menu(level1);
								continue ;
							}
						}
						itr.remove();
					}
				}
			});
			
			l1MenuMap = CollectionUtils.toMap(level1s, SideMenuLevel1Menu::getId);
			
		}
		return l1MenuMap;
	}
	
	
	Map<Long, SideMenuLevel2Menu> getL2MenuMap(){
		synchronized (this) {
			if(l2MenuMap == null) {
				Map<Long, SideMenuLevel2Menu> map = new HashMap<>();
				getL1MenuMap().values().stream().forEach(l1->{
					List<SideMenuLevel2Menu> l2Menus = l1.getLevel2s();
					if(l2Menus != null) {
						l2Menus.forEach(l2->map.put(l2.getId(), l2)); 
					}
				});
				l2MenuMap = map;
			}
			return l2MenuMap;
		}
	}
	
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<SideMenuLevel1Menu> getSideMenuLevelMenus(UserIdentifier user) {
		return new ArrayList<>(getL1MenuMap().values());
	}
	
	@Override
	public void updateSideMenuModules(UserIdentifier user, List<SideMenuLevel1Menu> modules) {
		List<SideMenuLevel1Menu> originModules = getSideMenuLevelMenus(user);
		CollectionUpdateStrategy<SideMenuLevel1Menu> updateModules = 
				new CollectionUpdateStrategy<>(SideMenuLevel1Menu.class, nDao,
						module->module.getId());
		CollectionUpdateStrategy<SideMenuLevel2Menu> updateGroups = 
				new CollectionUpdateStrategy<>(SideMenuLevel2Menu.class, nDao,
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
		reloadMenuMap();
	}
	
	@Override
	public SideMenuLevel2Menu getLevel2Menu(Long menuId) {
		return getL2MenuMap().get(menuId);
	}

}
