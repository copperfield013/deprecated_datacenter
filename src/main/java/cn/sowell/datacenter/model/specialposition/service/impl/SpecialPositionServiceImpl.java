package cn.sowell.datacenter.model.specialposition.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.abc.extface.dto.SpecialPosition;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.specialposition.dao.SpecialPositionDao;
import cn.sowell.datacenter.model.specialposition.pojo.criteria.SpecialPositionCriteria;
import cn.sowell.datacenter.model.specialposition.service.SpecialPositionService;

@Service
public class SpecialPositionServiceImpl implements SpecialPositionService {


	@Resource
	SpecialPositionDao specialPositionDao;
	
	@Override
	public List<SpecialPosition> getSpecialPositionList(SpecialPositionCriteria specialPositionCriteria, PageInfo pageInfo) {
		return specialPositionDao.getSpecialPositionList(specialPositionCriteria, pageInfo);
	}

	@Override
	public SpecialPosition getSpecialPosition(Long id) {
		return specialPositionDao.query(id);
	}

	@Override
	public void add(SpecialPosition specialPosition) {
		specialPositionDao.save(specialPosition);
	}

	@Override
	public void update(SpecialPosition specialPosition) {
		specialPositionDao.update(specialPosition);
	}

	@Override
	public void delete(Long id) {
		SpecialPosition specialPosition = new SpecialPosition();
		specialPosition.setId(id);
		specialPositionDao.delete(specialPosition);
	}

}
