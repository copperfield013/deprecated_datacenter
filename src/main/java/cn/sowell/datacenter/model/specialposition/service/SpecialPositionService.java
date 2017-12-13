package cn.sowell.datacenter.model.specialposition.service;

import java.util.List;

import com.abc.extface.dto.SpecialPosition;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.specialposition.pojo.criteria.SpecialPositionCriteria;

public interface SpecialPositionService {
	
	List<SpecialPosition> getSpecialPositionList(SpecialPositionCriteria specialPositionCriteria, PageInfo pageInfo);
	
	SpecialPosition getSpecialPosition(Long id);
	
	void add(SpecialPosition specialPosition);
	
	void update(SpecialPosition specialPosition);
	
	void delete(Long id);

}
