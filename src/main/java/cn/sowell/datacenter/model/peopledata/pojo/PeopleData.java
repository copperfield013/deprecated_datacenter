package cn.sowell.datacenter.model.peopledata.pojo;

import java.lang.reflect.Field;
import java.util.Date;

import cn.sowell.datacenter.model.peopledata.ABCAttribute;


public class PeopleData {
	
	@ABCAttribute(ignored=true)
	private Long id;
	
	@ABCAttribute("peoplecode")
	private String peopleCode;
	
	private String name;
	
	private String idcode;
	
	private String gender;
	
	private Date birthday;
	
	private String address;
	
	@ABCAttribute("contact1")
	private String contact;
	
	private String nativePlace;
	
	private String householdPlace;
	
	private String nation;
	
	private String politicalStatus;
	
	private String maritalStatus;
	
	private String religion;
	
	private String healthCondition;
	
	private String peopleType;
	
	@ABCAttribute(value="家庭医生", entityName="familyDoctor")
	private PeopleDataRelation familyDoctor;
	
	private String lowIncomeInsuredCode;
	
	//低保人员类别
	private String lowIncomeInsuredType;
	
	private String handicappedCode;
	
	private String handicappedType;
	
	private String handicappedLevel;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPeopleCode() {
		return peopleCode;
	}

	public void setPeopleCode(String peopleCode) {
		this.peopleCode = peopleCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcode() {
		return idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}


	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getHouseholdPlace() {
		return householdPlace;
	}

	public void setHouseholdPlace(String householdPlace) {
		this.householdPlace = householdPlace;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getPoliticalStatus() {
		return politicalStatus;
	}

	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getHealthCondition() {
		return healthCondition;
	}

	public void setHealthCondition(String healthCondition) {
		this.healthCondition = healthCondition;
	}

	public String getPeopleType() {
		return peopleType;
	}

	public void setPeopleType(String peopleType) {
		this.peopleType = peopleType;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public PeopleDataRelation getFamilyDoctor() {
		return familyDoctor;
	}

	public void setFamilyDoctor(PeopleDataRelation familyDoctor) {
		this.familyDoctor = familyDoctor;
	}

	public String getLowIncomeInsuredCode() {
		return lowIncomeInsuredCode;
	}

	public void setLowIncomeInsuredCode(String lowIncomeInsuredCode) {
		this.lowIncomeInsuredCode = lowIncomeInsuredCode;
	}

	public String getHandicappedCode() {
		return handicappedCode;
	}

	public void setHandicappedCode(String handicappedCode) {
		this.handicappedCode = handicappedCode;
	}

	public String getHandicappedType() {
		return handicappedType;
	}

	public void setHandicappedType(String handicappedType) {
		this.handicappedType = handicappedType;
	}

	public String getHandicappedLevel() {
		return handicappedLevel;
	}

	public void setHandicappedLevel(String handicappedLevel) {
		this.handicappedLevel = handicappedLevel;
	}

	public String getLowIncomeInsuredType() {
		return lowIncomeInsuredType;
	}

	public void setLowIncomeInsuredType(String lowIncomeInsuredType) {
		this.lowIncomeInsuredType = lowIncomeInsuredType;
	}

}
