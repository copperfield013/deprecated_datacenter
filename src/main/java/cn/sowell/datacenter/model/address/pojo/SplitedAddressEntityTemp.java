package cn.sowell.datacenter.model.address.pojo;

import java.util.List;

import com.abc.extface.dto.SplitedAddressEntity;

public class SplitedAddressEntityTemp {
	
	private SplitedAddressEntity splitedAddressEntity;
	
	private List<SubAddressElement> elements;

	public SplitedAddressEntity getSplitedAddressEntity() {
		return splitedAddressEntity;
	}

	public void setSplitedAddressEntity(SplitedAddressEntity splitedAddressEntity) {
		this.splitedAddressEntity = splitedAddressEntity;
	}

	public List<SubAddressElement> getElements() {
		return elements;
	}

	public void setElements(List<SubAddressElement> elements) {
		this.elements = elements;
	}

}