package cn.sowell.datacenter.model.modules.pojo;

public class ImportTemplateCriteria {
	private String module;
	private Long userId;
	private String composite;
	private boolean loadFields = false;
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getComposite() {
		return composite;
	}
	public void setComposite(String composite) {
		this.composite = composite;
	}
	public boolean isLoadFields() {
		return loadFields;
	}
	public void setLoadFields(boolean loadFields) {
		this.loadFields = loadFields;
	}
}
