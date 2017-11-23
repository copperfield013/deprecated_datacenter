package cn.sowell.datacenter.model.position.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.abc.extface.dto.Position;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.position.dao.PositionDao;
import cn.sowell.datacenter.model.position.pojo.criteria.PositionCriteria;
import cn.sowell.datacenter.model.position.service.PositionService;

@Service
public class PositionServiceImpl implements PositionService{
	
	@Resource
	PositionDao positionDao;

	@Override
	public List<Position> getPositionList(PositionCriteria positionCriteria, PageInfo pageInfo) {
		return positionDao.getPositionList(positionCriteria, pageInfo);
	}

	@Override
	public Position getPosition(Long id) {
		return positionDao.query(id);
	}

	@Override
	public void add(Position position) {
		positionDao.save(position);
	}

	@Override
	public void update(Position position) {
		positionDao.update(position);
	}

	@Override
	public void delete(Long id) {
		Position position = new Position();
		position.setCode(id);
		positionDao.delete(position);
	}

}
