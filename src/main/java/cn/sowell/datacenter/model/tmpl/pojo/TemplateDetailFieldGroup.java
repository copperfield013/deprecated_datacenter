package cn.sowell.datacenter.model.tmpl.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;

@Entity
@Table(name="t_tmpl_detail_fieldgroup")
public class TemplateDetailFieldGroup {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="tmpl_id")
	private Long tmplId;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_is_array")
	private Integer isArray;
	
	@Column(name="composite_id")
	private Long compositeId;
	
	@ManyToOne
	@JoinColumn(name = "composite_id",insertable = false, updatable = false)
	private DictionaryComposite composite;
	
	@Column(name="c_order")
	private Integer order;
	
	@Column(name="c_unmodifiable")
	private Integer unmodifiable;
	
	@Column(name="update_time")
	private Date updateTime;
	
	
	@Transient
	private List<TemplateDetailField> fields = new ArrayList<TemplateDetailField>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTmplId() {
		return tmplId;
	}
	public void setTmplId(Long tmplId) {
		this.tmplId = tmplId;
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
	public Integer getUnmodifiable() {
		return unmodifiable;
	}
	public void setUnmodifiable(Integer unmodifiable) {
		this.unmodifiable = unmodifiable;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public List<TemplateDetailField> getFields() {
		return fields;
	}
	public void setFields(List<TemplateDetailField> fields) {
		this.fields = fields;
	}
	public Integer getIsArray() {
		return isArray;
	}
	public void setIsArray(Integer isArray) {
		this.isArray = isArray;
	}
	public Long getCompositeId() {
		return compositeId;
	}
	public void setCompositeId(Long compositeId) {
		this.compositeId = compositeId;
	}
	public DictionaryComposite getComposite() {
		return composite;
	}
	public void setComposite(DictionaryComposite composite) {
		this.composite = composite;
	}
}
