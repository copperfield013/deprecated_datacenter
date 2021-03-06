package cn.sowell.datacenter.model.config.bean;

import cn.sowell.datacenter.common.ApiUser;

public class ValidateDetailParamter {
	private String validateSign;
	private String code;
	private ApiUser user;
	private Long nodeId;
	private Long dtmplId;
	private Long fieldGroupId;
	private String contextType;
	public ValidateDetailParamter(String validateSign, ApiUser user) {
		this.validateSign = validateSign;
		this.user = user;
	}
	public String getValidateSign() {
		return validateSign;
	}
	public String getCode() {
		return code;
	}
	public ValidateDetailParamter setCode(String code) {
		this.code = code;
		return this;
	}
	public ApiUser getUser() {
		return user;
	}
	public Long getNodeId() {
		return nodeId;
	}
	public ValidateDetailParamter setNodeId(Long nodeId) {
		this.nodeId = nodeId;
		return this;
	}
	public Long getDetailTemplateId() {
		return dtmplId;
	}
	public ValidateDetailParamter setDetailTemplateId(Long dtmplId) {
		this.dtmplId = dtmplId;
		return this;
	}
	public ValidateDetailParamter setContextType(String contextType) {
		this.contextType = contextType;
		return this;
	}
	public String getContextType() {
		return contextType;
	}
	public Long getFieldGroupId() {
		return fieldGroupId;
	}
	public ValidateDetailParamter setFieldGroupId(Long fieldGroupId) {
		this.fieldGroupId = fieldGroupId;
		return this;
	}
}
