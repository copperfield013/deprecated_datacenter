package cn.sowell.datacenter.model.specialposition.pojo;

import com.abc.extface.dto.SpecialPosition;

public class SpecialPositionEntity {
	
	private SpecialPosition specialPosition;
	
	private String belongPositionName;

	public SpecialPosition getSpecialPosition() {
		return specialPosition;
	}

	public void setSpecialPosition(SpecialPosition specialPosition) {
		this.specialPosition = specialPosition;
	}

	public String getBelongPositionName() {
		return belongPositionName;
	}

	public void setBelongPositionName(String belongPositionName) {
		this.belongPositionName = belongPositionName;
	}
	
	

}
