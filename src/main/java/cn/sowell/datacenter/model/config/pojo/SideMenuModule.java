package cn.sowell.datacenter.model.config.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_config_sidemenu_module")
public class SideMenuModule {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_order")
	private Integer order;
	
	@Column(name="module_name")
	private String moduleName;
	
	@Transient
	private ConfigModule configModule;
	
	@Transient
	private List<SideMenuModuleTempalteGroup> groups;

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


	public List<SideMenuModuleTempalteGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<SideMenuModuleTempalteGroup> groups) {
		this.groups = groups;
	}

	public ConfigModule getConfigModule() {
		return configModule;
	}

	public void setConfigModule(ConfigModule configModule) {
		this.configModule = configModule;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

}
