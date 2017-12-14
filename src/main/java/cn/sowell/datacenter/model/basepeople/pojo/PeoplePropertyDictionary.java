package cn.sowell.datacenter.model.basepeople.pojo;

import javax.persistence.Column;

public class PeoplePropertyDictionary {
	@Column(name="p_id")
	private Long propertyId;
	
	@Column(name="p_name")
	private String propertyName;
	
	@Column(name="p_cname")
	private String cname;
	
	@Column(name="p_type")
	private Integer type;
	
	@Column(name="p_check_rule")
	private Integer checkRule;
	
	@Column(name="a_id")
	private Integer associateId;
	
	@Column(name="a_name")
	private String associateName;
	
	@Column(name="a_cname")
	private String associateCname;

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCheckRule() {
		return checkRule;
	}

	public void setCheckRule(Integer checkRule) {
		this.checkRule = checkRule;
	}

	public Integer getAssociateId() {
		return associateId;
	}

	public void setAssociateId(Integer associateId) {
		this.associateId = associateId;
	}

	public String getAssociateName() {
		return associateName;
	}

	public void setAssociateName(String associateName) {
		this.associateName = associateName;
	}

	public String getAssociateCname() {
		return associateCname;
	}

	public void setAssociateCname(String associateCname) {
		this.associateCname = associateCname;
	}
	
}
