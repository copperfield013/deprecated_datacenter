package cn.sowell.datacenter.model.peopledata.pojo;

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
@Table(name="t_people_view_template_fieldgroup")
public class PeopleTemplateGroup {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="tmpl_id")
	private Long tmplId;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_order")
	private Integer order;
	
	@Column(name="c_unmodifiable")
	private Integer unmodifiable;
	
	@Column(name="update_time")
	private Date updateTime;
	
	@Transient
	private List<PeopleTemplateField> fields = new ArrayList<PeopleTemplateField>();
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
	public List<PeopleTemplateField> getFields() {
		return fields;
	}
	public void setFields(List<PeopleTemplateField> fields) {
		this.fields = fields;
	}
}
