package cn.sowell.datacenter.model.address.pojo;

import javax.persistence.Column;

public class AddressCode {
	
	@Column(name="ABP0002")
	private String addressCode;  //标准地址唯一编码
	
	@Column(name="ABC0917")
	private int level; //级别
	
	@Column(name="ABC0918")
	private int areaCode; //所属行政区域

	public String getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}
	
}
