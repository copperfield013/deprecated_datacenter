package cn.sowell.datacenter.model.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.sowell.datacenter.model.admin.dao.AdminUserDao;
import cn.sowell.datacenter.model.admin.service.AdminUserService;
import cn.sowell.dataserver.model.tmpl.service.AdminIdGetter;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService, AdminIdGetter{

	@Resource
	AdminUserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return userDao.getUser(username);
	}

	@Override
	public Long getSystemAdminIdByUserId(Long userId) {
		return userDao.getSystemAdminIdByUserId(userId);
	}

}
