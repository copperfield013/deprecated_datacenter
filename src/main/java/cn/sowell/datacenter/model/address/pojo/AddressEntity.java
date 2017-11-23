package cn.sowell.datacenter.model.address.pojo;

import javax.persistence.Column;

public class AddressEntity {
		
	@Column(name="ABP0003")
	private String addressStr; //存储原中文地址名称
	
	@Column(name="ABC0921")
	private String positionCode; 

	@Column(name="ABC0922")
	private String locationName; // 地点/楼栋名
	
	@Column(name="ABC0923")
	private String backAddress; //后址
	
	@Column(name="ABC0919")
	private String splitAddress; //分词后地址
	
	@Column(name="ABC0924")
	private String splitAddressByHand; //手工分词后地址
	
	@Column(name="ABP0002")
	private String addressCode;  //标准地址唯一编码

	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getBackAddress() {
		return backAddress;
	}

	public void setBackAddress(String backAddress) {
		this.backAddress = backAddress;
	}

	public String getSplitAddress() {
		return splitAddress;
	}

	public void setSplitAddress(String splitAddress) {
		this.splitAddress = splitAddress;
	}

	public String getSplitAddressByHand() {
		return splitAddressByHand;
	}

	public void setSplitAddressByHand(String splitAddressByHand) {
		this.splitAddressByHand = splitAddressByHand;
	}

	public String getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}
	
	
}
