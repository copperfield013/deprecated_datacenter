package cn.sowell.datacenter.model.tmpl.strategy;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

public class TemplateListUpdateStrategy implements TemplateUpdateStrategy<TemplateListTempalte> {

	@Resource
	TemplateService tService;
	
	@Resource
	NormalOperateDao nDao;
	
	@Override
	public void update(TemplateListTempalte template) {
		TemplateListTempalte origin = tService.getListTemplate(template.getId());
		if(origin != null){
			origin.setTitle(template.getTitle());
			origin.setUnmodifiable(template.getUnmodifiable());
			origin.setDefaultPageSize(template.getDefaultPageSize());
			origin.setDefaultOrderFieldId(template.getDefaultOrderFieldId());
			origin.setDefaultOrderDirection(template.getDefaultOrderDirection());
			
			Date now = new Date();
			
			NormalDaoSetUpdateStrategy.build(
					TemplateListColumn.class, nDao,
					column->column.getId(),
					(oColumn, column)->{
						oColumn.setTitle(column.getTitle());
						oColumn.setFieldKey(column.getFieldKey());
						oColumn.setFieldId(column.getFieldId());
						oColumn.setOrder(column.getOrder());
						oColumn.setOrderable(column.getOrderable());
						oColumn.setSpecialField(column.getSpecialField());
						oColumn.setUpdateTime(now);
						oColumn.setViewOption(column.getViewOption());
					},column->{
						column.setCreateTime(now);
						column.setUpdateTime(now);
						column.setTemplateId(origin.getId());
					})
				.doUpdate(origin.getColumns(), template.getColumns());
			
			NormalDaoSetUpdateStrategy.build(
				TemplateListCriteria.class, nDao, 
				criteria->criteria.getId(), 
				(originCriteria, criteria)->{
					originCriteria.setTitle(criteria.getTitle());
					originCriteria.setFieldId(criteria.getFieldId());
					originCriteria.setFieldKey(criteria.getFieldKey());
					originCriteria.setRelation(criteria.getRelation());
					originCriteria.setQueryShow(criteria.getQueryShow());
					originCriteria.setComparator(criteria.getComparator());
					originCriteria.setInputType(criteria.getInputType());
					originCriteria.setRelationLabel(criteria.getRelationLabel());
					originCriteria.setOrder(criteria.getOrder());
					originCriteria.setViewOption(criteria.getViewOption());
					originCriteria.setDefaultValue(criteria.getDefaultValue());
					originCriteria.setPlaceholder(criteria.getPlaceholder());
					originCriteria.setUpdateTime(now);
				}, criteria->{
					criteria.setCreateTime(now);
					criteria.setUpdateTime(now);
					criteria.setTemplateId(origin.getId());
				})
			.doUpdate(origin.getCriterias(), template.getCriterias());
		}else{
			throw new RuntimeException("列表模板[id=" + template.getId() + "]不存在");
		}
	}

	@Override
	public Long create(TemplateListTempalte template) {
		if(template.getId() == null){
			Date now = new Date();
			//创建
			template.setCreateTime(now );
			template.setUpdateTime(now);
			Long tmplId = nDao.save(template);
			Set<TemplateListColumn> columns = template.getColumns();
			for (TemplateListColumn column : columns) {
				column.setTemplateId(tmplId);
				column.setCreateTime(now);
				column.setUpdateTime(now);
				nDao.save(column);
			}
			Set<TemplateListCriteria> criterias = template.getCriterias();
			for (TemplateListCriteria criteria : criterias) {
				criteria.setTemplateId(tmplId);
				criteria.setCreateTime(now);
				criteria.setUpdateTime(now);
				nDao.save(criteria);
			}
			return tmplId;
		}
		return null;
	}

}
