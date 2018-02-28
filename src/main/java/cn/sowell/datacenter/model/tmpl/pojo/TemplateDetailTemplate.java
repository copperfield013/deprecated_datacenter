package cn.sowell.datacenter.model.tmpl.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_tmpl_detail_template")
public class TemplateDetailTemplate {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long tmplId;
	
	@Column(name="c_name")
	private String name;
	
	@Column(name="c_module")
	private String module;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
	private Date updateTime;
	
	@Column(name="create_user_id")
	private Long createUserId;
	
	@Column(name="c_authority")
	private String authority;
	
	@Transient
	private List<TemplateDetailFieldGroup> groups = new ArrayList<TemplateDetailFieldGroup>();
	public Long getTmplId() {
		return tmplId;
	}
	public void setTmplId(Long tmplId) {
		this.tmplId = tmplId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public List<TemplateDetailFieldGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<TemplateDetailFieldGroup> groups) {
		this.groups = groups;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
}
