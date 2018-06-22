package cn.sowell.datacenter.model.config.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_config_sidemenu_level2")
public class SideMenuLevel2 {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="sidemenu_level1_id")
	private Long sideMenuLevel1Id;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_order")
	private Integer order;
	
	@Column(name="c_is_default")
	private Integer isDefault;
	
	@Column(name="tmplgroup_id")
	private Long templateGroupId;
	
	@Transient
	@Column(name="tmplgroup_title")
	private String templateGroupTitle;
	
	@Transient
	@Column(name="tmplgroup_key")
	private String templateGroupKey;
	
	
	@Transient
	@Column(name="tmpl_module")
	private String templateModule;
	
	
	@Transient
	@Column(name="tmpl_module_title")
	private String templateModuleTitle;

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

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getTemplateGroupTitle() {
		return templateGroupTitle;
	}

	public void setTemplateGroupTitle(String templateGroupTitle) {
		this.templateGroupTitle = templateGroupTitle;
	}

	public String getTemplateModule() {
		return templateModule;
	}

	public void setTemplateModule(String templateModule) {
		this.templateModule = templateModule;
	}

	public String getTemplateGroupKey() {
		return templateGroupKey;
	}

	public void setTemplateGroupKey(String templateGroupKey) {
		this.templateGroupKey = templateGroupKey;
	}

	public String getTemplateModuleTitle() {
		return templateModuleTitle;
	}

	public void setTemplateModuleTitle(String templateModuleTitle) {
		this.templateModuleTitle = templateModuleTitle;
	}

	public Long getSideMenuLevel1Id() {
		return sideMenuLevel1Id;
	}

	public void setSideMenuLevel1Id(Long sideMenuLevel1Id) {
		this.sideMenuLevel1Id = sideMenuLevel1Id;
	}

}
