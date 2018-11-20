package cn.sowell.datacenter.api.controller.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.api.controller.APiDataNotFoundException;
import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.common.RequestParameterMapComposite;
import cn.sowell.datacenter.entityResolver.CEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.FieldParserDescription;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.ABCNodeProxy;
import cn.sowell.datacenter.entityResolver.impl.ArrayItemPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.RelationEntityProxy;
import cn.sowell.datacenter.model.admin.service.AdminUserService;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.dataserver.model.dict.pojo.DictionaryComposite;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.ViewDataService;
import cn.sowell.dataserver.model.modules.service.impl.EntityView;
import cn.sowell.dataserver.model.modules.service.impl.EntityView.EntityColumn;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.ListTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;
import javassist.NotFoundException;

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
	
	static Logger logger = Logger.getLogger(ApiEntityController.class);
	
	
	@ResponseBody
	@RequestMapping("/list/{menuId}")
	public ResponseJSON list(@PathVariable Long menuId, PageInfo pageInfo,
			HttpServletRequest request, ApiUser user) {
		JSONObjectResponse res = new JSONObjectResponse();
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		String moduleName = menu.getTemplateModule();
		ModuleMeta module = mService.getModule(moduleName);
		//创建条件对象
		ListTemplateEntityViewCriteria criteria = new ListTemplateEntityViewCriteria();
		//设置条件
		criteria.setModule(moduleName);
		criteria.setTemplateGroupId(menu.getTemplateGroupId());
		Map<Long, String> criteriaMap = exractTemplateCriteriaMap(request);
		criteria.setListTemplateCriteria(criteriaMap);
		criteria.setPageInfo(pageInfo);
		criteria.setUser(user);
		//执行查询
		ListTemplateEntityView view = (ListTemplateEntityView) vService.query(criteria);
		
		res.put("module", toModule(module));
		res.put("ltmpl", toListTemplate(view.getListTemplate()));
		res.put("entities", toEntities(view));
		res.put("pageInfo", view.getCriteria().getPageInfo());
		res.put("criterias", toCriterias(view, criteria));
		return res;
	}
	
	
	private JSONObject toModule(ModuleMeta module) {
		JSONObject jModule = new JSONObject();
		jModule.put("name", module.getName());
		jModule.put("title", module.getTitle());
		return jModule;
	}
	private JSONObject toListTemplate(TemplateListTemplate listTemplate) {
		JSONObject jDtmpl = new JSONObject();
		jDtmpl.put("id", listTemplate.getId());
		jDtmpl.put("title", listTemplate.getTitle());
		jDtmpl.put("module", listTemplate.getModule());
		return jDtmpl;
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
	private JSONArray toEntities(EntityView view) {
		JSONArray arrayEntities = new JSONArray();
		int index = view.getCriteria().getPageInfo().getFirstIndex();
		List<EntityColumn> cols = view.getColumns();
		for(CEntityPropertyParser parser :view.getParsers()) {
			JSONObject jEntity = new JSONObject();
			jEntity.put("code", parser.getCode());
			if(parser instanceof ModuleEntityPropertyParser) {
				jEntity.put("title", ((ModuleEntityPropertyParser) parser).getTitle());
			}
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
	@RequestMapping("/dtmpl/{menuId}")
	public ResponseJSON dtmpl(@PathVariable Long menuId, ApiUser user) {
		JSONObjectResponse res = new JSONObjectResponse();
		
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		String moduleName = menu.getTemplateModule();
		
		ModuleMeta moduleMeta = mService.getModule(moduleName);
        TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
        TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplGroup.getDetailTemplateId());
    	
    	JSONObject jEntity = toEntityJson(null, dtmpl);
    	
    	res.put("module", toModule(moduleMeta));
    	res.put("entity", jEntity);
    	return res;
	}
	
	@ResponseBody
	@RequestMapping("/detail/{menuId}/{code}")
	public ResponseJSON detail(@PathVariable Long menuId, @PathVariable String code, @RequestParam(required=false) Long historyId, ApiUser user) {
		JSONObjectResponse res = new JSONObjectResponse();
		
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		String moduleName = menu.getTemplateModule();
		
		ModuleMeta moduleMeta = mService.getModule(moduleName);
        TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
        TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplGroup.getDetailTemplateId());
        
        ModuleEntityPropertyParser entity = null;
        
        if(historyId == null) {
        	entity = mService.getEntity(moduleName, code, null, user);
        }else {
        	entity = mService.getHistoryEntityParser(moduleName, code, historyId, user);
        }
    	
        if(entity != null) {
        	JSONObject jEntity = toEntityJson(entity, dtmpl);
        	
        	List<EntityHistoryItem> historyItems = mService.queryHistory(menu.getTemplateModule(), code, 1, 100, user);
        	
        	res.put("module", toModule(moduleMeta));
        	res.put("entity", jEntity);
        	JSONArray aHistoryItems = toHistoryItems(historyItems, historyId);
        	res.put("history", aHistoryItems);
        }else {
        	throw new APiDataNotFoundException();
        }
    	return res;
	}
	
	
	private JSONArray toHistoryItems(List<EntityHistoryItem> historyItems, Long currentId) {
		JSONArray aHistoryItems = new JSONArray();
		if(historyItems != null) {
			boolean hasCurrentId = currentId != null;
			for (EntityHistoryItem historyItem : historyItems) {
				JSONObject jHistoryItem = new JSONObject();
				aHistoryItems.add(jHistoryItem);
				jHistoryItem.put("id", historyItem.getId());
				jHistoryItem.put("userName", historyItem.getUserName());
				jHistoryItem.put("time", historyItem.getTime());
				jHistoryItem.put("monthKey", historyItem.getMonthKey());
				if(hasCurrentId && historyItem.getId().equals(currentId)) {
					jHistoryItem.put("current", true);
				}
			}
			if(!hasCurrentId && !aHistoryItems.isEmpty()) {
				((JSONObject)aHistoryItems.get(0)).put("current", true);
			}
		}
		return aHistoryItems;
	}
	private JSONObject toEntityJson(ModuleEntityPropertyParser entity, TemplateDetailTemplate dtmpl) {
		JSONObject jEntity = new JSONObject();
		if(entity != null) {
			jEntity.put("title", entity.getTitle());
			jEntity.put("code", entity.getCode());
		}
		JSONArray aFieldGroups = new JSONArray();
		jEntity.put("fieldGroups", aFieldGroups);
		
		for ( TemplateDetailFieldGroup fieldGroup : dtmpl.getGroups()) {
			JSONObject jFieldGroup = new JSONObject();
			aFieldGroups.add(jFieldGroup);
			jFieldGroup.put("id", fieldGroup.getId());
			jFieldGroup.put("title", fieldGroup.getTitle());
			if(fieldGroup.getComposite() != null) {
				JSONObject jComposite = new JSONObject();
				jFieldGroup.put("composite", jComposite);
				DictionaryComposite composite = fieldGroup.getComposite();
				jComposite.put("id", composite.getId());
				jComposite.put("name", composite.getName());
				jComposite.put("addType", composite.getAddType());
				jComposite.put("relationSubdomain", composite.getRelationSubdomain());
				jComposite.put("access", composite.getAccess());
				jComposite.put("isArray", composite.getIsArray());
				jComposite.put("relationLabelAccess", composite.getRelationLabelAccess());
				jComposite.put("relationKey", composite.getRelationKey());
			}
			if(!Integer.valueOf(1).equals(fieldGroup.getIsArray())) {
				JSONArray aFields = new JSONArray();
				jFieldGroup.put("fields", aFields);
				for (TemplateDetailField field : fieldGroup.getFields()) {
					JSONObject jField = new JSONObject();
					aFields.add(jField);
					bindCommonData(field, jField);
					jField.put("fieldName", field.getFieldName());
					if(entity != null && field.getFieldAvailable()) {
						jField.put("value", entity.getFormatedProperty(field.getFieldName()));
					}
				}
			}else {
				JSONArray aDescs = new JSONArray();
				jFieldGroup.put("descs", aDescs);
				jFieldGroup.put("stmplId", fieldGroup.getSelectionTemplateId());
				String compositeName = null;
				if(fieldGroup.getComposite() != null) {
					compositeName = fieldGroup.getComposite().getName();
					for (TemplateDetailField field : fieldGroup.getFields()) {
						JSONObject jDesc = new JSONObject();
						aDescs.add(jDesc);
						jDesc.put("format", FieldParserDescription.getArrayFieldNameFormat(field.getFieldName(), compositeName));
						bindCommonData(field, jDesc);
					}
				}
				
				
				JSONArray aCompositeEntities = new JSONArray();
				jFieldGroup.put("array", aCompositeEntities);
				if(entity != null) {
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
								bindCommonData(field, jField);
								if(field.getFieldAvailable()) {
									jField.put("value", compositeEntity.getFormatedProperty(field.getFieldName()));
								}
							}
						}
					}
				}
			}
		}
		
		return jEntity;
	}
	
	void bindCommonData(TemplateDetailField field, JSONObject jField) {
		jField.put("id", field.getId());
		jField.put("fieldName", field.getFieldName());
		jField.put("title", field.getTitle());
		jField.put("type", field.getType());
		jField.put("available", field.getFieldAvailable());
		jField.put("optionKey", field.getOptionGroupKey());
		jField.put("fieldId", field.getFieldId());
	}
	
	final static String KEY_FUSE_MODE = "%fuseMode%";
	
	@ResponseBody
	@RequestMapping("/update/{menuId}")
	public ResponseJSON update(
			@PathVariable Long menuId,
			@RequestParam(value=KEY_FUSE_MODE, required=false) Boolean fuseMode,
    		RequestParameterMapComposite composite, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		String moduleName = menu.getTemplateModule();
    	 try {
    		 composite.getMap().remove(KEY_FUSE_MODE);
    		 String code = null;
    		 if(Boolean.TRUE.equals(fuseMode)) {
    			 code = mService.fuseEntity(moduleName, composite.getMap(), user);
    		 }else {
    			 code = mService.mergeEntity(moduleName, composite.getMap(), user);
    		 }
    		 if(code != null) {
    			 jRes.put("code", code);
    			 jRes.setStatus("suc");
    		 }
         } catch (Exception e) {
        	 logger.error("保存实体时出现异常", e);
        	 jRes.setStatus("error");
         }
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/remove/{menuId}/{code}")
	public ResponseJSON remove(@PathVariable Long menuId, @PathVariable String code, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		try {
			mService.deleteEntity(menu.getTemplateModule(), code, user);
			jRes.setStatus("suc");
		} catch (Exception e) {
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/selections/{menuId}/{stmplId}")
	public ResponseJSON selections(
			@PathVariable Long menuId, 
			@PathVariable Long stmplId,
			String excepts,
			PageInfo pageInfo, 
			HttpServletRequest request, ApiUser user) {
		authService.vaidateUserL2MenuAccessable(user, menuId);
		TemplateSelectionTemplate stmpl = tService.getSelectionTemplate(stmplId);
		

		//创建条件对象
		Map<Long, String> criteriaMap = exractTemplateCriteriaMap(request);
		SelectionTemplateEntityViewCriteria criteria = new SelectionTemplateEntityViewCriteria(stmpl, criteriaMap);
		//设置条件
		criteria.setExistCodes(TextUtils.split(excepts, ",", HashSet<String>::new, e->e));
		criteria.setPageInfo(pageInfo);
		criteria.setUser(user);
		//执行查询
		SelectionTemplateEntityView view = (SelectionTemplateEntityView) vService.query(criteria);
		
		JSONObjectResponse jRes = new JSONObjectResponse();
		jRes.put("entities", toEntities(view));
		jRes.put("pageInfo", view.getCriteria().getPageInfo());
		//jRes.put("criterias", toCriterias(view, criteria));
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/load_entities/{menuId}/{stmplId}")
	public ResponseJSON loadSelectionEntities(
			@PathVariable Long menuId,
			@PathVariable Long stmplId,
			@RequestParam String codes, 
			@RequestParam String fields,
			ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		authService.vaidateUserL2MenuAccessable(user, menuId);
		TemplateSelectionTemplate stmpl = tService.getSelectionTemplate(stmplId);
		Map<String, CEntityPropertyParser> parsers = mService.getEntityParsers(
				stmpl.getModule(), 
				stmpl.getRelationName(), 
				TextUtils.split(codes, ",", HashSet<String>::new, c->c), user)
				;
		JSONObject entities = toEntitiesJson(parsers, TextUtils.split(fields, ",", HashSet<String>::new, f->f));
		jRes.put("entities", entities);
		jRes.setStatus("suc");
		return jRes;
	}

	private JSONObject toEntitiesJson(Map<String, CEntityPropertyParser> parsers, Set<String> fieldNames) {
		JSONObject entities = new JSONObject();
		if(parsers != null && fieldNames != null) {
			parsers.forEach((code, parser)->{
				JSONObject entity = new JSONObject();
				entity.put(ABCNodeProxy.CODE_PROPERTY_NAME, parser.getCode());
				entities.put(parser.getCode(), entity);
				for (String fieldName : fieldNames) {
					entity.put(fieldName, parser.getFormatedProperty(fieldName));
				}
			});
		}
		return entities;
	}
	
	
}
