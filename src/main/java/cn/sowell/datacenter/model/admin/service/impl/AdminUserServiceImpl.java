package cn.sowell.datacenter.model.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abc.auth.pojo.UserInfo;
import com.abc.auth.service.ServiceFactory;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.datacenter.entityResolver.UserCodeService;
import cn.sowell.datacenter.model.admin.dao.AdminUserDao;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.datacenter.model.admin.service.AdminUserService;
import cn.sowell.dataserver.model.modules.bean.criteriaConveter.UserRelationExistCriteriaConverter.UserCodeSupplier;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService, UserCodeService, UserCodeSupplier{

	@Resource
	AdminUserDao userDao;
	
	private ThreadLocal<String> threadUserCode = new ThreadLocal<>();
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserInfo u = ServiceFactory.getUserInfoService().getUserInfoByUserName(username);
		if(u != null) {
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

	@Override
	public void setUserCode(String userCode) {
		threadUserCode.set(userCode);
	}
	
	@Override
	public String getUserCode() {
		UserIdentifier user = UserUtils.getCurrentUser();
		if(user != null) {
			return (String) user.getId();
		}else {
			return threadUserCode.get();
		}
	}

}
