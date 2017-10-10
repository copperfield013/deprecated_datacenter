package cn.sowell.datacenter.model.peopledata.pojo;

import java.util.Date;
import java.util.List;

import com.abc.dto.ErrorInfomation;

import cn.sowell.datacenter.model.peopledata.EntityElement;
import cn.sowell.datacenter.model.peopledata.EntityRecord;


public class PeopleData implements EntityData{
	
	@EntityElement(readIgnored=true)
	private Long id;
	
	@EntityElement("peoplecode")
	private String peopleCode;
	
	private String name;
	
	private String idcode;
	
	private String gender;
	
	private Date birthday;
	
	private String address;
	
	@EntityElement("contact1")
	private String contact;
	
	private String nativePlace;
	
	private String householdPlace;
	
	private String nation;
	
	private String maritalStatus;
	
	private String religion;
	
	private String healthCondition;
	
	private String peopleType;
	
	/*******低保信息******/
	private String lowIncomeInsuredCode;
	private String lowIncomeInsuredType;
	private String lowIncomeInsuredReason;
	private String lowIncomeInsuredId;
	private String lowIncomeInsuredAmount;
	private Date lowIncomeInsuredStart;
	private Date lowIncomeInsuredEnd;
	
	
	/*****残疾人信息******/
	private String handicappedCode;
	private String handicappedType;
	private String handicappedLevel;
	private String handicappedReason;
	
	
	
	/*****失业信息******/
	private Date unemployeeDate;
	private String unemployeeCode;
	private String unemployeeStatus;
	private String employeeId;
	private String hardToEmployeeType;
	private String employeeType;
	private String employeeCapacity;
	private String employeeSituation;
	private String employeeDestination;
	private String employeeWay;
	
	
	
	/*****党员信息******/
	private String politicalStatus;
	private Date partyDate;
	private String partyPost;
	private String partyOrganization;
	private String partySuperior;
	private String partyOrgContact;
	private String CYOrganization;
	
	
	
	/*****工作信息******/
	@EntityElement(value="workExperience")
	@EntityRecord(elementClass=WorkExperience.class, entityName="workExperience", domainName="工作经历")
	private List<WorkExperience> workExperiences;
	
	@EntityElement("家庭信息")
	@EntityRecord(entityName="familyInfomation", domainName="家庭信息")
	private FamilyInfo familyInfo;
	
	
	
	/*****计生信息******/
	private String childrenCount;
	private String contraceptionMeasure;
	private Integer pregnancyWeeks;
	private String familyPlanningCode;
	private String familyPlanningType;
	
	private List<ErrorInfomation> errors;
	
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

	public String getHandicappedReason() {
		return handicappedReason;
	}

	public void setHandicappedReason(String handicappedReason) {
		this.handicappedReason = handicappedReason;
	}

	public Date getUnemployeeDate() {
		return unemployeeDate;
	}

	public void setUnemployeeDate(Date unemployeeDate) {
		this.unemployeeDate = unemployeeDate;
	}

	public String getUnemployeeCode() {
		return unemployeeCode;
	}

	public void setUnemployeeCode(String unemployeeCode) {
		this.unemployeeCode = unemployeeCode;
	}

	public String getUnemployeeStatus() {
		return unemployeeStatus;
	}

	public void setUnemployeeStatus(String unemployeeStatus) {
		this.unemployeeStatus = unemployeeStatus;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getHardToEmployeeType() {
		return hardToEmployeeType;
	}

	public void setHardToEmployeeType(String hardToEmployeeType) {
		this.hardToEmployeeType = hardToEmployeeType;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getEmployeeCapacity() {
		return employeeCapacity;
	}

	public void setEmployeeCapacity(String employeeCapacity) {
		this.employeeCapacity = employeeCapacity;
	}

	public String getEmployeeSituation() {
		return employeeSituation;
	}

	public void setEmployeeSituation(String employeeSituation) {
		this.employeeSituation = employeeSituation;
	}

	public String getEmployeeDestination() {
		return employeeDestination;
	}

	public void setEmployeeDestination(String employeeDestination) {
		this.employeeDestination = employeeDestination;
	}

	public String getEmployeeWay() {
		return employeeWay;
	}

	public void setEmployeeWay(String employeeWay) {
		this.employeeWay = employeeWay;
	}

	public Date getPartyDate() {
		return partyDate;
	}

	public void setPartyDate(Date partyDate) {
		this.partyDate = partyDate;
	}

	public String getPartyOrganization() {
		return partyOrganization;
	}

	public void setPartyOrganization(String partyOrganization) {
		this.partyOrganization = partyOrganization;
	}

	public String getPartySuperior() {
		return partySuperior;
	}

	public void setPartySuperior(String partySuperior) {
		this.partySuperior = partySuperior;
	}

	public String getPartyOrgContact() {
		return partyOrgContact;
	}

	public void setPartyOrgContact(String partyOrgContact) {
		this.partyOrgContact = partyOrgContact;
	}

	public String getCYOrganization() {
		return CYOrganization;
	}

	public void setCYOrganization(String cYOrganization) {
		CYOrganization = cYOrganization;
	}



	public String getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(String childrenCount) {
		this.childrenCount = childrenCount;
	}

	public String getContraceptionMeasure() {
		return contraceptionMeasure;
	}

	public void setContraceptionMeasure(String contraceptionMeasure) {
		this.contraceptionMeasure = contraceptionMeasure;
	}

	public String getFamilyPlanningCode() {
		return familyPlanningCode;
	}

	public void setFamilyPlanningCode(String familyPlanningCode) {
		this.familyPlanningCode = familyPlanningCode;
	}

	public String getFamilyPlanningType() {
		return familyPlanningType;
	}

	public void setFamilyPlanningType(String familyPlanningType) {
		this.familyPlanningType = familyPlanningType;
	}

	public String getLowIncomeInsuredReason() {
		return lowIncomeInsuredReason;
	}

	public void setLowIncomeInsuredReason(String lowIncomeInsuredReason) {
		this.lowIncomeInsuredReason = lowIncomeInsuredReason;
	}

	public String getLowIncomeInsuredId() {
		return lowIncomeInsuredId;
	}

	public void setLowIncomeInsuredId(String lowIncomeInsuredId) {
		this.lowIncomeInsuredId = lowIncomeInsuredId;
	}

	public Date getLowIncomeInsuredStart() {
		return lowIncomeInsuredStart;
	}

	public void setLowIncomeInsuredStart(Date lowIncomeInsuredStart) {
		this.lowIncomeInsuredStart = lowIncomeInsuredStart;
	}

	public Date getLowIncomeInsuredEnd() {
		return lowIncomeInsuredEnd;
	}

	public void setLowIncomeInsuredEnd(Date lowIncomeInsuredEnd) {
		this.lowIncomeInsuredEnd = lowIncomeInsuredEnd;
	}


	public Integer getPregnancyWeeks() {
		return pregnancyWeeks;
	}

	public void setPregnancyWeeks(Integer pregnancyWeeks) {
		this.pregnancyWeeks = pregnancyWeeks;
	}

	public String getLowIncomeInsuredAmount() {
		return lowIncomeInsuredAmount;
	}

	public void setLowIncomeInsuredAmount(String lowIncomeInsuredAmount) {
		this.lowIncomeInsuredAmount = lowIncomeInsuredAmount;
	}

	public String getPartyPost() {
		return partyPost;
	}

	public void setPartyPost(String partyPost) {
		this.partyPost = partyPost;
	}

	public List<WorkExperience> getWorkExperiences() {
		return workExperiences;
	}

	public void setWorkExperiences(List<WorkExperience> workExperiences) {
		this.workExperiences = workExperiences;
	}

	public FamilyInfo getFamilyInfo() {
		return familyInfo;
	}

	public void setFamilyInfo(FamilyInfo familyInfo) {
		this.familyInfo = familyInfo;
	}

	public List<ErrorInfomation> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorInfomation> errors) {
		this.errors = errors;
	}

}
