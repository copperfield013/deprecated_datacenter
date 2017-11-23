package cn.sowell.datacenter.model.position.dao;

import java.util.List;

import com.abc.extface.dto.Position;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.position.pojo.criteria.PositionCriteria;

public interface PositionDao {
	
	List<Position> getPositionList(PositionCriteria positionCriteria, PageInfo pageInfo);
	
	void save(Position position);
	
	void delete(Position position);
	
	Position query(Long positionId);
	
	void update(Position position);

}
