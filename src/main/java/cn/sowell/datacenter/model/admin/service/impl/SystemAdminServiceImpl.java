package cn.sowell.datacenter.model.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.datacenter.model.admin.dao.SystemAdminDao;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
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
	public void setListTemplateAsDefault(Long ltmplId,
			UserIdentifier user) {
		aDao.setListTmplAsDefault(ltmplId, (long) user.getId());
	}
	
	
	@Override
	public Long getDefaultTemplateId(long adminId, String module, String type) {
		return aDao.getDefaultTemplateId(adminId, module, type);
	}
	
	@Override
	public SystemAdmin getSystemAdminByUserId(long userId) {
		return aDao.getSystemAdminByUserId(userId);
	}
	
}
