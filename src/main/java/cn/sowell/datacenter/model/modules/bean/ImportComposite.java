package cn.sowell.datacenter.model.modules.bean;

public class ImportComposite {
	private String name;
	private String title;
	private String moduleKey;
	private String configKey;
	public ImportComposite(String name, String title, String moduleKey, String configKey) {
		super();
		this.name = name;
		this.title = title;
		this.moduleKey = moduleKey;
		this.configKey = configKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getModuleKey() {
		return moduleKey;
	}
	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
}
