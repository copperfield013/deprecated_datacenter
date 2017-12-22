package cn.sowell.datacenter.model.peopledata.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name="t_base_people_information")
public class PeopleCompositeDictionaryItem {
	
	@Id
	@Column(name="t_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JSONField(name="c_id")
	private Long id;
	
	@Column(name="t_info_enname")
	@JSONField(name="name")
	private String name;
	
	@Column(name="t_info_cnname")
	@JSONField(name="cname")
	private String cname;
	
	@Transient
	List<PeopleFieldDictionaryItem> fields;


	public List<PeopleFieldDictionaryItem> getFields() {
		return fields;
	}

	public void setFields(List<PeopleFieldDictionaryItem> fields) {
		this.fields = fields;
	}

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

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
	
}
