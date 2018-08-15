package cn.sowell.datacenter.model.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abc.auth.pojo.UserInfo;
import com.abc.auth.service.ServiceFactory;

import cn.sowell.datacenter.entityResolver.UserCodeService;
import cn.sowell.datacenter.model.admin.dao.AdminUserDao;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.datacenter.model.admin.service.AdminUserService;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService, UserCodeService{

	@Resource
	AdminUserDao userDao;
	
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserInfo u = ServiceFactory.getUserInfoService().getUserInfoByUserName(username);
		if(u != null) {
			u.setPassword("e10adc3949ba59abbe56e057f20f883e");
			return new ABCUser(u);
		}else {
			return null;
		}
		
		//return userDao.getUser(username);
	}
	

	@Override
	public String getUserCode(Object userPrinciple) {
		ABCUser user = (ABCUser) userPrinciple;
		return user.getCode();
	}

}
