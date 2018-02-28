package cn.sowell.datacenter.model.tmpl.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;

public interface ListTemplateDao {

	/**
	 * 查询所有列表模板
	 * @param userId
	 * @param serializable 
	 * @param pageInfo
	 * @return
	 */
	List<TemplateListTmpl> queryLtmplList(String module, Serializable userId, PageInfo pageInfo);

	/**
	 * 根据模板id获得所有列数据
	 * @param ltmplId
	 * @return
	 */
	Set<TemplateListColumn> getColumnsByTmplId(Long ltmplId);

	/**
	 * 根据模板id获得所有查询条件数据
	 * @param ltmplId
	 * @return
	 */
	Set<TemplateListCriteria> getCriteriaByTmplId(Long ltmplId);

}
