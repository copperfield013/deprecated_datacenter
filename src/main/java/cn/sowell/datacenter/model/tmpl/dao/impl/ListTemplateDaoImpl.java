package cn.sowell.datacenter.model.tmpl.dao.impl;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.tmpl.dao.ListTemplateDao;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;

@Repository
public class ListTemplateDaoImpl implements ListTemplateDao{

	@Resource
	SessionFactory sFactory;
	
	@Override
	public List<TemplateListTmpl> queryLtmplList(String module, Serializable userId,
			PageInfo pageInfo) {
		return QueryUtils.pagingQuery("from TemplateListTmpl t where t.module = :module order by t.updateTime desc", sFactory.getCurrentSession(), pageInfo, dQuery->{
			dQuery.setParam("module", module);
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<TemplateListColumn> getColumnsByTmplId(Long ltmplId) {
		String hql = "from TemplateListColumn c where c.templateId = :ltmplId order by c.order asc";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setLong("ltmplId", ltmplId);
		return new LinkedHashSet<TemplateListColumn>(query.list());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<TemplateListCriteria> getCriteriaByTmplId(Long ltmplId) {
		String hql = "from TemplateListCriteria c where c.templateId = :ltmplId order by c.order asc";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setLong("ltmplId", ltmplId);
		return new LinkedHashSet<TemplateListCriteria>(query.list());
	}
	
	

}
