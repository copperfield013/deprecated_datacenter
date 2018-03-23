package cn.sowell.datacenter.model.modules.pojo;

public class ModuleMeta {
	private String key;
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean hasFunction(String functionExport) {
		return false;
	}
	
}
