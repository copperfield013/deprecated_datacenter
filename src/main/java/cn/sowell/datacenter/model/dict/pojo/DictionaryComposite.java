package cn.sowell.datacenter.model.dict.pojo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import cn.sowell.datacenter.entityResolver.Composite;

@Entity
@Table(name="t_dictionary_composite")
public class DictionaryComposite implements Composite{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JSONField(name="c_id")
	private Long id;
	
	@Column(name="c_name")
	@JSONField(name="name")
	private String name;
	
	@Column(name="c_title")
	@JSONField(name="cname")
	private String title;
	
	@Column(name="c_module")
	@JSONField(serialize=false)
	private String module;
	
	@Column(name="c_is_array")
	@JSONField(name="isArray")
	private Integer isArray;
	
	@Column(name="create_time")
	@JSONField(serialize=false)
	private Date createTime;
	
	@Column(name="update_time")
	@JSONField(serialize=false)
	private Date updateTime;
	
	@Column(name="c_authority")
	@JSONField(serialize=false)
	private String authority;
	
	@Transient
	private List<DictionaryField> fields;

	@Transient
	private Set<String> relationSubdomain;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public List<DictionaryField> getFields() {
		return fields;
	}
	public void setFields(List<DictionaryField> fields) {
		this.fields = fields;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public Integer getIsArray() {
		return isArray;
	}
	public void setIsArray(Integer isArray) {
		this.isArray = isArray;
	}
	public void setRelationSubdomain(Set<String> relationSubdomain) {
		this.relationSubdomain = relationSubdomain;
	}
	public Set<String> getRelationSubdomain() {
		return relationSubdomain;
	}
	
}
