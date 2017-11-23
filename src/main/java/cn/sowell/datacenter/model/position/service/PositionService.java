package cn.sowell.datacenter.model.position.service;

import java.util.List;

import com.abc.extface.dto.Position;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.position.pojo.criteria.PositionCriteria;

public interface PositionService {
	
	List<Position> getPositionList(PositionCriteria positionCriteria, PageInfo pageInfo);
	
	Position getPosition(Long id);
	
	void add(Position position);
	
	void update(Position position);
	
	void delete(Long id);
}
