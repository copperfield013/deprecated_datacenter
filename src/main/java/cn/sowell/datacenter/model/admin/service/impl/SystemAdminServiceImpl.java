package cn.sowell.datacenter.model.admin.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.admin.dao.SystemAdminDao;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.common.dao.NormalOperateDao;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;

@Service
public class SystemAdminServiceImpl implements SystemAdminService{

	@Resource
	SystemAdminDao aDao;
	
	@Resource
	NormalOperateDao nDao;
	
	@Override
	public void setTmplAsDefault(Long tmplId, UserIdentifier user) {
		aDao.setTmplAsDefault(tmplId, (long) user.getId());
	}
	
	@Override
	public SystemAdmin getSystemAdminByUserId(Serializable userId) {
		return aDao.getSystemAdminByUserId((long) userId);
	}
}
