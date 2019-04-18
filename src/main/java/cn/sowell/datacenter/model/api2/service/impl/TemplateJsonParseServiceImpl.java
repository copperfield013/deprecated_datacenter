package cn.sowell.datacenter.model.api2.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.datacenter.model.api2.service.TemplateJsonParseService;
import cn.sowell.dataserver.model.modules.service.view.EntityView;
import cn.sowell.dataserver.model.modules.service.view.EntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.AbstractListCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.AbstractListTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;

@Service
public class TemplateJsonParseServiceImpl implements TemplateJsonParseService{

	static Pattern operatePattern = Pattern.compile("^operate[(-d)*(-u)*(-r)*]$"); 
	public JSONObject toListTemplateJson(TemplateListTemplate listTemplate) {
		JSONObject jDtmpl = new JSONObject();
		jDtmpl.put("id", listTemplate.getId());
		jDtmpl.put("title", listTemplate.getTitle());
		jDtmpl.put("module", listTemplate.getModule());
		jDtmpl.put("criterias", toCriterias(listTemplate.getCriterias()));
		jDtmpl.put("columns", toColumns(listTemplate.getColumns()));
		Set<String> operates = null;
		List<TemplateListColumn> columns = listTemplate.getColumns();
		for (TemplateListColumn column : columns) {
			if(column.getSpecialField() != null && operates == null && column.getSpecialField().startsWith("operate")) {
				operates = new LinkedHashSet<>();
				String specialField = column.getSpecialField();
				if(specialField.contains("-d")) {
					operates.add("detail");
				}
				if(specialField.contains("-u")) {
					operates.add("update");
				}
				break;
			}
		}
		if(operates != null) {
			jDtmpl.put("operates", operates);
		}
		return jDtmpl;
	}
	
	private JSONArray toCriterias(List<? extends AbstractListCriteria> criterias) {
		JSONArray aCriterias = new JSONArray();
		if(criterias != null) {
			for (AbstractListCriteria criteria : criterias) {
				aCriterias.add(criteria);
			}
		}
		return aCriterias;
	}

	private JSONArray toColumns(List<TemplateListColumn> columns) {
		JSONArray aColumns = new JSONArray();
		if(columns != null) {
			columns.forEach(column->{
				aColumns.add(column);
			});
		}
		return aColumns;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JSONArray toCriterias(EntityView view, 
			EntityViewCriteria lcriteria) {
		JSONArray aCriterias = new JSONArray();
		AbstractListTemplate ltmpl = view.getListTemplate();
		List<? extends AbstractListCriteria> criterias = ltmpl.getCriterias();
		if(criterias != null && !criterias.isEmpty()) {
			for (AbstractListCriteria criteria : criterias) {
				if(criteria.getQueryShow() != null) {
					JSONObject jCriteria = (JSONObject) JSONObject.toJSON(criteria);
					jCriteria.put("value", lcriteria.getTemplateCriteriaMap().get(criteria.getId()));
					aCriterias.add(jCriteria);
				}
			}
		}
		return aCriterias;
		
	}

	@Override
	public JSONObject toTemplateGroupJson(TemplateGroup tmplGroup) {
		return (JSONObject) JSONObject.toJSON(tmplGroup);
	}
	
}
