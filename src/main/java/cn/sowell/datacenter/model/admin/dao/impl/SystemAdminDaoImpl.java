package cn.sowell.datacenter.model.admin.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.datacenter.model.admin.dao.SystemAdminDao;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;

@Repository
public class SystemAdminDaoImpl implements SystemAdminDao{

	@Resource
	SessionFactory sFactory;
	
	@Override
	public void setTmplAsDefault(long tmplId, long userId) {
		String sql = "update t_sys_admin set def_dtmpl_id = :tmplId where user_id = :userId";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		int result = query.setLong("tmplId", tmplId)
			.setLong("userId", userId)
			.executeUpdate();
		if(result != 1){
			throw new RuntimeException("修改失败，修改了" + result + "条记录");
		}
	}
	
	@Override
	public void setListTmplAsDefault(Long ltmplId, long userId) {
		String sql = "update t_sys_admin set def_ltmpl_id = :ltmplId where user_id = :userId";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		int result = query.setLong("ltmplId", ltmplId)
			.setLong("userId", userId)
			.executeUpdate();
		if(result != 1){
			throw new RuntimeException("修改失败，修改了" + result + "条记录");
		}
	}

	@Override
	public SystemAdmin getSystemAdminByUserId(long userId) {
		String hql = "from SystemAdmin a where a.userId = :userId";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setLong("userId", userId);
		query.setMaxResults(1);
		return (SystemAdmin) query.uniqueResult();
	}
	
	@Override
	public Long getDefaultTemplateId(long adminId, String module, String type) {
		String sql = 
				"	SELECT dt.tmpl_id" +
				"	FROM t_system_admin_deftmpl dt" +
				"	WHER dt.admin_id = :adminId" +
				"	AND dt.c_module = :module" +
				"	AND dt.c_type = :type";
		SQLQuery query = sFactory.getCurrentSession().createSQLQuery(sql);
		query.setLong("adminId", adminId);
		query.setString("module", module);
		query.setString("type", type);
		return (Long) query.uniqueResult();
	}
	
}
