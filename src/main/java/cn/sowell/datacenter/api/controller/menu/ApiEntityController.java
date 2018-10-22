package cn.sowell.datacenter.api.controller.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.datacenter.entityResolver.CEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.ArrayItemPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.RelationEntityProxy;
import cn.sowell.datacenter.model.admin.service.AdminUserService;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.ViewDataService;
import cn.sowell.dataserver.model.modules.service.impl.EntityView.EntityColumn;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Controller
@RequestMapping("/api/entity")
public class ApiEntityController {
	
	@Resource
	AdminUserService userService;
	
	@Resource
	AuthorityService authService;
	
	@Resource
	TemplateService tService;
	
	@Resource
	ViewDataService vService;
	
	@Resource
	ModulesService mService;
	
	
	private UserDetails getUser() {
		return userService.loadUserByUsername("admin");
	}
	@ResponseBody
	@RequestMapping("/list/{menuId}")
	public ResponseJSON list(@PathVariable Long menuId, PageInfo pageInfo,
			HttpServletRequest request) {
		JSONObjectResponse res = new JSONObjectResponse();
		UserDetails user = getUser();
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		String moduleName = menu.getTemplateModule();
		//创建条件对象
		ListTemplateEntityViewCriteria criteria = new ListTemplateEntityViewCriteria();
		//设置条件
		criteria.setModule(moduleName);
		criteria.setTemplateGroupId(menu.getTemplateGroupId());
		Map<Long, String> criteriaMap = exractTemplateCriteriaMap(request);
		criteria.setListTemplateCriteria(criteriaMap);
		criteria.setPageInfo(pageInfo);
		criteria.setUser((UserIdentifier) user);
		//执行查询
		ListTemplateEntityView view = (ListTemplateEntityView) vService.query(criteria);
		
		res.put("entities", toEntities(view));
		res.put("pageInfo", view.getCriteria().getPageInfo());
		res.put("criterias", toCriterias(view, criteria));
		return res;
	}
	
	
	private JSONArray toCriterias(ListTemplateEntityView view, ListTemplateEntityViewCriteria lcriteria) {
		JSONArray aCriterias = new JSONArray();
		TemplateListTemplate ltmpl = view.getListTemplate();
		Set<TemplateListCriteria> criterias = ltmpl.getCriterias();
		if(criterias != null && !criterias.isEmpty()) {
			for (TemplateListCriteria criteria : criterias) {
				if(criteria.getQueryShow() != null) {
					JSONObject jCriteria = (JSONObject) JSONObject.toJSON(criteria);
					jCriteria.put("value", lcriteria.getListTemplateCriteria().get(criteria.getId()));
					aCriterias.add(jCriteria);
				}
			}
		}
		return aCriterias;
		
	}
	private JSONArray toEntities(ListTemplateEntityView view) {
		JSONArray arrayEntities = new JSONArray();
		int index = view.getCriteria().getPageInfo().getFirstIndex();
		List<EntityColumn> cols = view.getColumns();
		for(CEntityPropertyParser p :view.getParsers()) {
			ModuleEntityPropertyParser parser = (ModuleEntityPropertyParser) p;
			JSONObject jEntity = new JSONObject();
			jEntity.put("code", parser.getCode());
			jEntity.put("title", parser.getTitle());
			jEntity.put("index", index++);
			JSONArray arrayFields = new JSONArray();
			jEntity.put("fields", arrayFields);
			cols.forEach(col->{
				JSONObject jField = new JSONObject();
				jField.put("id", col.getColumnId());
				jField.put("title", col.getTitle());
				jField.put("value", parser.getFormatedProperty(col.getFieldName(), col.getFieldType(), col.getFieldFormat()));
				arrayFields.add(jField);
			});
			arrayEntities.add(jEntity);
		}
		return arrayEntities;
	}

	private Map<Long, String> exractTemplateCriteriaMap(HttpServletRequest request) {
		ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(request, "criteria", "_");
		Map<Long, String> criteriaMap = new HashMap<Long, String>();
		pvs.getPropertyValueList().forEach(pv->{
			 Long criteriaId = FormatUtils.toLong(pv.getName());
			 if(criteriaId != null){
				 criteriaMap.put(criteriaId, FormatUtils.toString(pv.getValue()));
			 }
		 });
		return criteriaMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/detail/{menuId}/{code}")
	public ResponseJSON detail(@PathVariable Long menuId, @PathVariable String code) {
		UserDetails user = getUser();
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		String moduleName = menu.getTemplateModule();
		
		ModuleMeta moduleMeta = mService.getModule(moduleName);
        TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
        TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplGroup.getDetailTemplateId());
        
        ModuleEntityPropertyParser entity = null;
        
    	entity = mService.getEntity(moduleName, code, null, (UserIdentifier) user);
    	
    	JSONObject jEntity = toEntityJson(entity, dtmpl);
    	JSONObjectResponse res = new JSONObjectResponse();
    	res.put("entity", jEntity);
    	return res;
	}
	private JSONObject toEntityJson(ModuleEntityPropertyParser entity, TemplateDetailTemplate dtmpl) {
		JSONObject jEntity = new JSONObject();
		jEntity.put("title", entity.getTitle());
		jEntity.put("code", entity.getCode());
		JSONArray aFieldGroups = new JSONArray();
		jEntity.put("fieldGroups", aFieldGroups);
		
		for ( TemplateDetailFieldGroup fieldGroup : dtmpl.getGroups()) {
			JSONObject jFieldGroup = new JSONObject();
			aFieldGroups.add(jFieldGroup);
			jFieldGroup.put("id", fieldGroup.getId());
			jFieldGroup.put("title", fieldGroup.getTitle());
			if(!Integer.valueOf(1).equals(fieldGroup.getIsArray())) {
				JSONArray aFields = new JSONArray();
				jFieldGroup.put("fields", aFields);
				for (TemplateDetailField field : fieldGroup.getFields()) {
					JSONObject jField = new JSONObject();
					aFields.add(jField);
					jField.put("id", field.getId());
					jField.put("title", field.getTitle());
					jField.put("type", field.getType());
					jField.put("available", field.getFieldAvailable());
					jField.put("value", entity.getFormatedProperty(field.getFieldName()));
				}
			}else {
				JSONArray aCompositeEntities = new JSONArray();
				jFieldGroup.put("array", aCompositeEntities);
				List<ArrayItemPropertyParser> compositeEntities = entity.getCompositeArray(fieldGroup.getComposite().getName());
				if(compositeEntities != null) {
					for (ArrayItemPropertyParser compositeEntity : compositeEntities) {
						JSONObject jCompositeEntity = new JSONObject();
						aCompositeEntities.add(jCompositeEntity);
						jCompositeEntity.put("code", compositeEntity.getCode());
						if(fieldGroup.getRelationSubdomain() != null) {
							jCompositeEntity.put("relation", compositeEntity.getFormatedProperty(fieldGroup.getComposite().getName() + "." + RelationEntityProxy.LABEL_KEY));
						}
						JSONArray aFields = new JSONArray();
						jCompositeEntity.put("fields", aFields);
						for (TemplateDetailField field : fieldGroup.getFields()) {
							JSONObject jField = new JSONObject();
							aFields.add(jField);
							jField.put("id", field.getId());
							jField.put("title", field.getTitle());
							jField.put("type", field.getType());
							jField.put("available", field.getFieldAvailable());
							jField.put("value", compositeEntity.getFormatedProperty(field.getFieldName()));
						}
					}
				}
			}
		}
		
		return jEntity;
	}
	
	
	
	
	
}
