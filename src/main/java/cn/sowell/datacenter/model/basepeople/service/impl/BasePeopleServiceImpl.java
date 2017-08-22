package cn.sowell.datacenter.model.basepeople.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.dao.BasePeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.People;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;

@Service
public class BasePeopleServiceImpl implements BasePeopleService{

	@Resource
	BasePeopleDao basePeopleDao;
	
	@Override
	public List<People> queryList(BasePeopleCriteria criteria, PageInfo pageInfo) {
		return basePeopleDao.queryList(criteria, pageInfo);
	}
	
	@Override
	public void delete(Long id){
		People p = new People();
		p.setId(id);
		basePeopleDao.delete(p);
	}

}
