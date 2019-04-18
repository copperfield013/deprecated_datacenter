package cn.sowell.datacenter.model.config.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.abc.auth.pojo.AuthorityVO;
import com.abc.auth.service.ServiceFactory;
import com.google.common.collect.Lists;

import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel1Menu;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.pojo.criteria.AuthorityCriteria;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.datacenter.model.config.service.NonAuthorityException;
import cn.sowell.datacenter.model.config.service.SideMenuService;
import cn.sowell.dataserver.model.tmpl.service.TemplateGroupService;

@Service
public class AuthorityServiceImpl implements AuthorityService{
	
	@Resource
	SideMenuService menuService;
	
	@Resource
	TemplateGroupService tmplGroupService;
	
	@Override
	public SideMenuLevel2Menu validateL2MenuAccessable(Long level2MenuId) throws NonAuthorityException{
		return this.validateUserL2MenuAccessable((UserDetails) UserUtils.getCurrentUser(), level2MenuId);
	}
	
	@Override
	public SideMenuLevel1Menu validateL1MenuAccessable(Long level1MenuId) throws NonAuthorityException{
		return this.validateUserL1MenuAccessable((UserDetails) UserUtils.getCurrentUser(), level1MenuId);
	}

	@Override
	public SideMenuLevel2Menu validateUserL2MenuAccessable(UserDetails user, Long level2MenuId) throws NonAuthorityException{
		Assert.notNull(level2MenuId, "二级菜单的id不能为空");
		
		SideMenuLevel2Menu l2 = menuService.getLevel2Menu(level2MenuId);
		if(l2 != null) {
			Set<String> userAuthorities = CollectionUtils.toSet(user.getAuthorities(), GrantedAuthority::getAuthority);
			if(!l2.getAuthoritySet().isEmpty()) {
				//只有当用户至少包含菜单的其中一个权限时，才能验证成功
				if(!userAuthorities.stream()
						.filter(auth->l2.getAuthoritySet().contains(auth))
						.findFirst().isPresent()) {
					throw new NonAuthorityException(l2.getAuthoritySet(), userAuthorities);
				}
			}
			validateUserL1MenuAccessable(user, l2.getSideMenuLevel1Id());
			return l2;
		}else {
			throw new NonAuthorityException("根据id[" + level2MenuId + "]获得不到对应二级菜单");
		}
	}
	
	@Override
	public SideMenuLevel1Menu validateUserL1MenuAccessable(UserDetails user, Long level1MenuId) throws NonAuthorityException{
		Assert.notNull(level1MenuId, "一级菜单的id不能为空");
		
		SideMenuLevel1Menu l1 = menuService.getLevel1Menu(level1MenuId);
		if(l1 != null) {
			Set<String> userAuthorities = CollectionUtils.toSet(user.getAuthorities(), GrantedAuthority::getAuthority);
			
			//只有当用户至少包含菜单的其中一个权限时，才能验证成功
			if(!userAuthorities.stream()
					.filter(auth->l1.getAuthoritySet().contains(auth))
					.findFirst().isPresent()) {
				throw new NonAuthorityException(l1.getAuthoritySet(), userAuthorities);
			}
			return l1;
		}else {
			throw new NonAuthorityException("根据id[" + level1MenuId + "]获得不到对应一级菜单");
		}
	}
	
	
	public AuthorityVO getAuthority(String authCode) {
		ABCUser user = (ABCUser) UserUtils.getCurrentUser();
		return ServiceFactory.getRoleAuthorityService().getFunctionAuth(user.getUserInfo()).stream().filter(auth->authCode.equals(auth.getCode())).findFirst().orElse(null);
	}
	
	@Override
	public List<AuthorityVO> queryAuthorities(AuthorityCriteria criteria) {
		Assert.notNull(criteria.getUser(), "必须传入当前用户对象");
		return Lists.newArrayList(ServiceFactory.getRoleAuthorityService().getFunctionAuth(criteria.getUser()));
	}

}
