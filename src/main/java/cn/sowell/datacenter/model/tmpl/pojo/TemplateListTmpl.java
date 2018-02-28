package cn.sowell.datacenter.model.tmpl.pojo;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_tmpl_list_template")
public class TemplateListTmpl {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_def_pagesize")
	private Integer defaultPageSize;
	
	@Column(name="def_order_field_id")
	private Long defaultOrderFieldId;
	
	@Column(name="c_def_order_dir")
	private String defaultOrderDirection;
	
	@Column(name="create_user_id")
	private Long createUserId;
	
	@Column(name="c_unmodifiable")
	private Integer unmodifiable;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
	private Date updateTime;
	
	@Column(name="c_authority")
	private String authority;
	
	@Column(name="c_module")
	private String module;
	
	@Transient
	private Set<TemplateListColumn> columns = new LinkedHashSet<TemplateListColumn>();
	
	@Transient
	private Set<TemplateListCriteria> criterias = new LinkedHashSet<TemplateListCriteria>();
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
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long serializable) {
		this.createUserId = serializable;
	}
	public Integer getUnmodifiable() {
		return unmodifiable;
	}
	public void setUnmodifiable(Integer unmodifiable) {
		this.unmodifiable = unmodifiable;
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
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Set<TemplateListColumn> getColumns() {
		return columns;
	}
	public void setColumns(Set<TemplateListColumn> columns) {
		this.columns = columns;
	}
	public Set<TemplateListCriteria> getCriterias() {
		return criterias;
	}
	public void setCriterias(Set<TemplateListCriteria> criterias) {
		this.criterias = criterias;
	}
	public Integer getDefaultPageSize() {
		return defaultPageSize;
	}
	public void setDefaultPageSize(Integer defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}
	public Long getDefaultOrderFieldId() {
		return defaultOrderFieldId;
	}
	public void setDefaultOrderFieldId(Long defaultOrderFieldId) {
		this.defaultOrderFieldId = defaultOrderFieldId;
	}
	public String getDefaultOrderDirection() {
		return defaultOrderDirection;
	}
	public void setDefaultOrderDirection(String defaultOrderDirection) {
		this.defaultOrderDirection = defaultOrderDirection;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	
}
