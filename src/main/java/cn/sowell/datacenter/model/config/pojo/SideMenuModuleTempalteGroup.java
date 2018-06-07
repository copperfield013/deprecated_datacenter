package cn.sowell.datacenter.model.config.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.sowell.datacenter.model.tmpl.pojo.TemplateGroup;

@Entity
@Table(name="t_config_sidemenu_module_tmplgroup")
public class SideMenuModuleTempalteGroup {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="sidemenu_module_id")
	private Long sideMenuModuleId;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_order")
	private Integer order;
	
	@Column(name="c_is_default")
	private Integer isDefault;
	
	@Column(name="tmplgroup_id")
	private Long templateGroupId;
	
	
	@ManyToOne()
	@JoinColumn(name = "tmplgroup_id",insertable = false, updatable = false)
	private TemplateGroup templateGroup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSideMenuModuleId() {
		return sideMenuModuleId;
	}

	public void setSideMenuModuleId(Long sideMenuModuleId) {
		this.sideMenuModuleId = sideMenuModuleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Long getTemplateGroupId() {
		return templateGroupId;
	}

	public void setTemplateGroupId(Long templateGroupId) {
		this.templateGroupId = templateGroupId;
	}

	public TemplateGroup getTemplateGroup() {
		return templateGroup;
	}

	public void setTemplateGroup(TemplateGroup templateGroup) {
		this.templateGroup = templateGroup;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

}
