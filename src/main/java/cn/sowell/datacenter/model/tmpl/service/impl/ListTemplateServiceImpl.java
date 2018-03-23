package cn.sowell.datacenter.model.tmpl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.datacenter.model.tmpl.dao.ListTemplateDao;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;

@Service
public class ListTemplateServiceImpl implements ListTemplateService{
	@Resource
	ListTemplateDao lDao;

	@Override
	public List<TemplateListTempalte> queryLtmplList(String module, UserIdentifier user) {
		return lDao.queryLtmplList(module, user.getId(), null);
	}
	
	
}
