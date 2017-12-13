package cn.sowell.datacenter.model.specialposition.pojo.criteria;

import com.abc.extface.dto.SpecialPosition;

public class SpecialPositionCriteria extends SpecialPosition{
	
	private Long specialPositionId;

	public Long getSpecialPositionId() {
		return specialPositionId;
	}

	public void setSpecialPositionId(Long specialPositionId) {
		this.specialPositionId = specialPositionId;
		if(specialPositionId != null && !specialPositionId.equals("")) {
			super.setId(specialPositionId);
		}
	}

	
}
