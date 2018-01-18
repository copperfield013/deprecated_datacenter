package cn.sowell.datacenter.model.system.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.sowell.datacenter.model.admin.pojo.AdminUser;

@Entity
@Table(name="t_sys_admin")
public class SystemAdmin {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="def_dtmpl_id")
	private Long defaultDetailTemplateId;
	
	@Column(name="def_ltmpl_id")
	private Long defaultListTemplateId;
	
	@Column(name="sys_id")
	private Long systemId;
	
	@Transient
	private AdminUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public AdminUser getUser() {
		return user;
	}

	public void setUser(AdminUser user) {
		this.user = user;
	}

	public Long getDefaultListTemplateId() {
		return defaultListTemplateId;
	}

	public void setDefaultListTemplateId(Long defaultListTemplateId) {
		this.defaultListTemplateId = defaultListTemplateId;
	}

	public Long getDefaultDetailTemplateId() {
		return defaultDetailTemplateId;
	}

	public void setDefaultDetailTemplateId(Long defaultDetailTemplateId) {
		this.defaultDetailTemplateId = defaultDetailTemplateId;
	}
	
}
