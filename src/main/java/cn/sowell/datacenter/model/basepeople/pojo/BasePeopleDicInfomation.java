package cn.sowell.datacenter.model.basepeople.pojo;

import java.util.List;

public class BasePeopleDicInfomation {
	private Long   id;
    private String cnName;
    private String enName;
    private List<TBasePeopleDictionaryEntity> dicList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public List<TBasePeopleDictionaryEntity> getDicList() {
		return dicList;
	}
	public void setDicList(List<TBasePeopleDictionaryEntity> dicList) {
		this.dicList = dicList;
	}
	
    
}
