package cn.sowell.datacenter.model.dict.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import cn.sowell.datacenter.entityResolver.Field;

@Entity
@Table(name="t_dictionary_field")
public class DictionaryField implements Field{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JSONField(name="id")
	private Long id;
	
	@Column(name="composite_id")
	@JSONField(name="c_id")
	private Long compositeId;
	
	@Transient
	@JSONField(serialize=false, deserialize=false)
	private DictionaryComposite composite;
	
	@Column(name="c_full_key")
	@JSONField(name="name")
	private String fullKey;
	
	
	@Column(name="c_title")
	@JSONField(name="cname")
	private String title;
	
	@Column(name="c_type")
	@JSONField(name="type")
	private String type;
	
	@Column(name="c_input_type")
	private String inputType;
	
	@Column(name="c_abc_type")
	private String abcType;
	
	@Column(name="optgroup_id")
	private Long optionGroupId;
	
	@Column(name="checkrule_id")
	private Long checkruleId;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
	private Date updateTime;
	
	@Column(name="c_authority")
	private String authority;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCompositeId() {
		return compositeId;
	}
	public void setCompositeId(Long compositeId) {
		this.compositeId = compositeId;
	}
	public String getFullKey() {
		return fullKey;
	}
	public void setFullKey(String fullKey) {
		this.fullKey = fullKey;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public Long getOptionGroupId() {
		return optionGroupId;
	}
	public void setOptionGroupId(Long optionGroupId) {
		this.optionGroupId = optionGroupId;
	}
	public Long getCheckruleId() {
		return checkruleId;
	}
	public void setCheckruleId(Long checkruleId) {
		this.checkruleId = checkruleId;
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
	public String getAbcType() {
		return abcType;
	}
	public void setAbcType(String abcType) {
		this.abcType = abcType;
	}
	public DictionaryComposite getComposite() {
		return composite;
	}
	public void setComposite(DictionaryComposite composite) {
		this.composite = composite;
	}
}
