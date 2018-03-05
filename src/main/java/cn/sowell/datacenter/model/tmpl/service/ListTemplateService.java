package cn.sowell.datacenter.model.tmpl.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;

import com.abc.query.criteria.Criteria;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;

public interface ListTemplateService {

	/**
	 * 
	 * @param module 
	 * @param user
	 * @return
	 */
	List<TemplateListTempalte> queryLtmplList(String module, UserIdentifier user);

	/**
	 * 
	 * @param object
	 * @param pageInfo
	 * @return
	 */
	List<PeopleData> queryPeopleList(Set<NormalCriteria> criterias, PageInfo pageInfo);


	Map<Long, NormalCriteria> getCriteriasFromRequest(
			MutablePropertyValues pvs,
			Map<Long, TemplateListCriteria> criteriaMap);

	ListTemplateParameter exractTemplateParameter(Long tmplId,
			String module,
			HttpServletRequest request);

	List<Criteria> toCriterias(Set<NormalCriteria> nCriterias);


	
}
