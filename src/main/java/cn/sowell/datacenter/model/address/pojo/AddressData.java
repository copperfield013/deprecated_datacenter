package cn.sowell.datacenter.model.address.pojo;

import java.util.List;

import com.abc.dto.ErrorInfomation;

import cn.sowell.datacenter.model.peopledata.EntityElement;

public class AddressData {
	@EntityElement("编码")
	private String code;
	
	@EntityElement("地址级别")
	private String level;
	
	@EntityElement("所属行政区域")
	private String areaCode;
	
	@EntityElement("地址简称")
	private String shortName;
	
	
	@EntityElement("updateTime")
	private String updateTime;
	
	private List<ErrorInfomation> errors;
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public List<ErrorInfomation> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorInfomation> errors) {
		this.errors = errors;
	}
}
