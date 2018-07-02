package cn.sowell.datacenter.model.config.service.impl;

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

@Service
public class SideMenuServiceImpl implements SideMenuService{

	@Resource
	SideMenuDao sDao;
	
	@Resource
	NormalOperateDao nDao;
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<SideMenuLevel1Menu> getSideMenuLevelMenus(UserIdentifier user) {
		List<SideMenuLevel1Menu> modules = sDao.getSideMenus();
		
		Map<Long, List<SideMenuLevel2Menu>> groupsMap 
					= sDao.getSideMenuLevel2Map(CollectionUtils.toSet(modules, module->module.getId()));
		modules.forEach(module->module.setLevel2s(groupsMap.get(module.getId())));
		return modules;
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
	}
	
	@Override
	public SideMenuLevel2Menu getLevel2Menu(Long menuId) {
		return sDao.getLevel2Menu(menuId);
	}

}
