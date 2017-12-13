package cn.sowell.datacenter.model.specialposition.dao;

import java.util.List;

import com.abc.extface.dto.SpecialPosition;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.specialposition.pojo.criteria.SpecialPositionCriteria;

public interface SpecialPositionDao {
	
	List<SpecialPosition> getSpecialPositionList(SpecialPositionCriteria specialPositionCriteria, PageInfo pageInfo);
	
	void save(SpecialPosition specialPosition);
	
	void delete(SpecialPosition specialPosition);
	
	SpecialPosition query(Long specialPositionId);
	
	void update(SpecialPosition specialPosition);

}
