package cn.sowell.datacenter.model.tmpl.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_tmpl_group")
public class TemplateGroup {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_module")
	private String module;
	
	@Column(name="list_tmpl_id")
	private Long listTemplateId;
	
	@Transient
	@Column(name="list_tmpl_title")
	private String listTemplateTitle;
	
	@Column(name="detail_tmpl_id")
	private Long detailTemplateId;
	
	@Transient
	@Column(name="detail_tmpl_title")
	private String detailTemplateTitle;
	
	@Column(name="c_key")
	private String key;
	
	@Column(name="c_authority")
	private String authority;
	
	@Column(name="c_disabled")
	private Integer disabled;
	
	@Column(name="create_user_id")
	private Long createUserId;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
	private Date updateTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Long getListTemplateId() {
		return listTemplateId;
	}

	public void setListTemplateId(Long listTemplateId) {
		this.listTemplateId = listTemplateId;
	}

	public String getListTemplateTitle() {
		return listTemplateTitle;
	}

	public void setListTemplateTitle(String listTemplateTitle) {
		this.listTemplateTitle = listTemplateTitle;
	}

	public Long getDetailTemplateId() {
		return detailTemplateId;
	}

	public void setDetailTemplateId(Long detailTemplateId) {
		this.detailTemplateId = detailTemplateId;
	}

	public String getDetailTemplateTitle() {
		return detailTemplateTitle;
	}

	public void setDetailTemplateTitle(String detailTemplateTitle) {
		this.detailTemplateTitle = detailTemplateTitle;
	}


	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
