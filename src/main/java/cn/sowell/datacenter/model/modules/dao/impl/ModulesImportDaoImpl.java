package cn.sowell.datacenter.model.modules.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.datacenter.model.modules.dao.ModulesImportDao;
import cn.sowell.datacenter.model.modules.pojo.ImportTemplateCriteria;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplate;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplateField;

@Repository
public class ModulesImportDaoImpl implements ModulesImportDao{

	@Resource
	SessionFactory sFactory;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ModuleImportTemplate> getImportTemplates(ImportTemplateCriteria criteria) {
		DeferedParamQuery dQuery = new DeferedParamQuery("from ModuleImportTemplate t where t.module = :module and t.composite = :composite and t.createUserId = :createUserId");
		dQuery
			.setParam("module", criteria.getModule())
			.setParam("composite", criteria.getComposite())
			.setParam("createUserId", criteria.getUserId());
		return dQuery.createQuery(sFactory.getCurrentSession(), false, null).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ModuleImportTemplateField> getTemplateFields(Long tmplId) {
		String hql = "from ModuleImportTemplateField f where f.templateId = :tmplId order by f.order asc";
		Query query = sFactory.getCurrentSession().createQuery(hql);
		query.setLong("tmplId", tmplId);
		return query.list();
	}

}