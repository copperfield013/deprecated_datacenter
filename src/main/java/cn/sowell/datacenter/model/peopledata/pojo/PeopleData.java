package cn.sowell.datacenter.model.peopledata.pojo;

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
	
	private String maritalStatus;
	
	private String religion;
	
	private String healthCondition;
	
	private String peopleType;
	
	@ABCAttribute(value="家庭医生", entityName="familyDoctor")
	private PeopleDataRelation familyDoctor;
	
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
	private String companyName;
	private String workUnit;
	private String workAddress;
	private String unitContact;
	private String workDepartment;
	private String unitNature;
	private Float salary;
	private String workContent;
	private String workDuty;
	private String workSubject;
	private String workedOccupation;
	
	
	
	/*****家庭信息******/
	private String familyAddress;
	private Integer familyCount;
	private String familyContact;
	private String familyFinancialSituation;
	private String familyType;
	private Float familyYearIncome;
	private String familySituation;
	private Float familyAvgMonthIncome;
	private Integer familyUnemployeeCount;
	
	
	/*****计生信息******/
	private String childrenCount;
	private String contraceptionMeasure;
	private Integer pregnancyWeeks;
	private String familyPlanningCode;
	private String familyPlanningType;
	
	
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public String getUnitContact() {
		return unitContact;
	}

	public void setUnitContact(String unitContact) {
		this.unitContact = unitContact;
	}

	public String getWorkDepartment() {
		return workDepartment;
	}

	public void setWorkDepartment(String workDepartment) {
		this.workDepartment = workDepartment;
	}

	public String getUnitNature() {
		return unitNature;
	}

	public void setUnitNature(String unitNature) {
		this.unitNature = unitNature;
	}

	public Float getSalary() {
		return salary;
	}

	public void setSalary(Float salary) {
		this.salary = salary;
	}

	public String getWorkContent() {
		return workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	public String getWorkDuty() {
		return workDuty;
	}

	public void setWorkDuty(String workDuty) {
		this.workDuty = workDuty;
	}

	public String getWorkSubject() {
		return workSubject;
	}

	public void setWorkSubject(String workSubject) {
		this.workSubject = workSubject;
	}

	public String getWorkedOccupation() {
		return workedOccupation;
	}

	public void setWorkedOccupation(String workedOccupation) {
		this.workedOccupation = workedOccupation;
	}

	public String getFamilyAddress() {
		return familyAddress;
	}

	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}

	public Integer getFamilyCount() {
		return familyCount;
	}

	public void setFamilyCount(Integer familyCount) {
		this.familyCount = familyCount;
	}

	public String getFamilyContact() {
		return familyContact;
	}

	public void setFamilyContact(String familyContact) {
		this.familyContact = familyContact;
	}

	public String getFamilyFinancialSituation() {
		return familyFinancialSituation;
	}

	public void setFamilyFinancialSituation(String familyFinancialSituation) {
		this.familyFinancialSituation = familyFinancialSituation;
	}

	public String getFamilyType() {
		return familyType;
	}

	public void setFamilyType(String familyType) {
		this.familyType = familyType;
	}

	public Float getFamilyYearIncome() {
		return familyYearIncome;
	}

	public void setFamilyYearIncome(Float familyYearIncome) {
		this.familyYearIncome = familyYearIncome;
	}

	public String getFamilySituation() {
		return familySituation;
	}

	public void setFamilySituation(String familySituation) {
		this.familySituation = familySituation;
	}

	public Float getFamilyAvgMonthIncome() {
		return familyAvgMonthIncome;
	}

	public void setFamilyAvgMonthIncome(Float familyAvgMonthIncome) {
		this.familyAvgMonthIncome = familyAvgMonthIncome;
	}

	public Integer getFamilyUnemployeeCount() {
		return familyUnemployeeCount;
	}

	public void setFamilyUnemployeeCount(Integer familyUnemployeeCount) {
		this.familyUnemployeeCount = familyUnemployeeCount;
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

}
