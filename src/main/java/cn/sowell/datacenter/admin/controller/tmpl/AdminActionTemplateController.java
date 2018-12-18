package cn.sowell.datacenter.admin.controller.tmpl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.admin.controller.modules.AdminModulesController;
import cn.sowell.datacenter.entityResolver.CEntityPropertyParser;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.modules.service.ViewDataService;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityView;
import cn.sowell.dataserver.model.modules.service.impl.SelectionTemplateEntityViewCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.ArrayEntityProxy;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionArrayEntity;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionArrayEntityField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateSelectionTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/atmpl")
public class AdminActionTemplateController {
	
	@Resource
	ModulesService mService;
	
	@Resource
	TemplateService tService;
	
	@Resource
	ConfigureService configService;
	
	@Resource
	ViewDataService vService;
	
	static Logger logger = Logger.getLogger(AdminActionTemplateController.class);
	
	
	@RequestMapping("/list/{moduleName}")
	public String list(Model model, @PathVariable String moduleName) {
		ModuleMeta moduleMeta = mService.getModule(moduleName);
		ArrayEntityProxy.setLocalUser(UserUtils.getCurrentUser());
		List<TemplateActionTemplate> tmplList = tService.queryActionTemplates(moduleName);
		Map<Long, Set<TemplateGroup>> relatedGroupsMap = tService.getActionTemplateRelatedGroupsMap(CollectionUtils.toSet(tmplList, atmpl->atmpl.getId()));
		model.addAttribute("modulesJson", configService.getSiblingModulesJson(moduleName));
		model.addAttribute("tmplList", tmplList);
		model.addAttribute("module", moduleMeta);
		model.addAttribute("relatedGroupsMap", relatedGroupsMap);
		return AdminConstants.JSP_TMPL_ACTION + "/atmpl_list.jsp";
	}
	
	@RequestMapping("/to_create/{module}")
	public String toCreate(@PathVariable String module, Model model){
		ModuleMeta moduleMeta = mService.getModule(module);
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_TMPL_ACTION + "/atmpl_update.jsp";
	}
	
	@RequestMapping("/update/{tmplId}")
	public String update(@PathVariable Long tmplId, Model model){
		TemplateActionTemplate tmpl = tService.getActionTemplate(tmplId);
		ArrayEntityProxy.setLocalUser(UserUtils.getCurrentUser());
		JSONObject tmplJson = (JSONObject) JSON.toJSON(tmpl);
		ModuleMeta moduleMeta = mService.getModule(tmpl.getModule());
		model.addAttribute("module", moduleMeta);
		model.addAttribute("tmpl", tmpl);
		model.addAttribute("tmplJson", tmplJson);
		return AdminConstants.JSP_TMPL_ACTION + "/atmpl_update.jsp";
	}
	
	
	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON save(@RequestBody JsonRequest jReq) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		ArrayEntityProxy.setLocalUser(UserUtils.getCurrentUser());
		TemplateActionTemplate data = parseToTmplData(jReq.getJsonObject());
		try {
			tService.mergeTemplate(data);
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("保存模板时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/remove/{tmplId}")
	public AjaxPageResponse remove(@PathVariable Long tmplId){
		try {
			tService.removeActionTemplate(tmplId);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	private TemplateActionTemplate parseToTmplData(JSONObject jo) {
		if(jo != null){
			TemplateActionTemplate data = new TemplateActionTemplate();
			data.setId(jo.getLong("tmplId"));
			data.setTitle(jo.getString("title"));
			data.setModule(jo.getString("module"));
			JSONArray jGroups = jo.getJSONArray("groups");
			if(jGroups != null && !jGroups.isEmpty()){
				int i = 0;
				for (Object ele : jGroups) {
					if(ele instanceof JSONObject){
						JSONObject jGroup = (JSONObject) ele;
						TemplateActionFieldGroup group = new TemplateActionFieldGroup();
						group.setId(jGroup.getLong("id"));
						group.setTitle(jGroup.getString("title"));
						group.setIsArray(jGroup.getBoolean("isArray")?1:null);
						group.setCompositeId(jGroup.getLong("compositeId"));
						group.setSelectionTemplateId(jGroup.getLong("selectionTemplateId"));
						group.setUnallowedCreate(Integer.valueOf(1).equals(jGroup.getInteger("unallowedCreate"))? 1: null);
						group.setOrder(i++);
						data.getGroups().add(group);
						JSONArray jFields = jGroup.getJSONArray("fields");
						if(jFields != null && !jFields.isEmpty()){
							int j = 0;
							for (Object ele1 : jFields) {
								if(ele1 instanceof JSONObject){
									JSONObject jField = (JSONObject) ele1;
									TemplateActionField field = new TemplateActionField();
									field.setId(jField.getLong("id"));
									field.setFieldId(jField.getLong("fieldId"));
									field.setTitle(jField.getString("title"));
									field.setViewValue(jField.getString("viewVal"));
									Boolean dbcol = jField.getBoolean("dbcol");
									field.setColNum((dbcol == null || !dbcol) ? 1: 2);
									field.setOrder(j++);
									field.setValidators(jField.getString("validators"));
									group.getFields().add(field);
								}
							}
							if(j > 0) {
								JSONArray aEntities = jGroup.getJSONArray("entities");
								if(aEntities != null && !aEntities.isEmpty()) {
									for (int k = 0; k < aEntities.size(); k++) {
										JSONObject jEntity = (JSONObject) aEntities.get(k);
										TemplateActionArrayEntity entity = new TemplateActionArrayEntity();
										entity.setId(jEntity.getLong("id"));
										entity.setIndex(k);
										entity.setRelationEntityCode(jEntity.getString("relationEntityCode"));
										entity.setRelationLabel(jEntity.getString("relationLabel"));
										entity.setTmplFieldGroupId(group.getId());
										JSONObject fieldsMap = jEntity.getJSONObject("fieldMap");
										group.getFields().forEach(field->{
											if(field.getFieldId() != null) {
												JSONObject jField = fieldsMap.getJSONObject("f_" + field.getFieldId());
												if(jField != null) {
													TemplateActionArrayEntityField eField = new TemplateActionArrayEntityField();
													eField.setId(jField.getLong("id"));
													eField.setTmplFieldId(field.getId());
													eField.setActionArrayEntityId(entity.getId());
													eField.setValue(jField.getString("value"));
													entity.getFields().add(eField);
													field.getArrayEntityFields().add(eField);
												}
											}
										});
										group.getEntities().add(entity);
									}
								}
							}
						}
						
					}
					
				}
			}
			return data; 
		}
		return null;
	}
	
	@RequestMapping("/open_selection/{stmplId}")
	public String openSelection(
			@PathVariable Long stmplId,
			String exists,
			PageInfo pageInfo,
			HttpServletRequest request,
			Model model) {
		TemplateSelectionTemplate stmpl = tService.getSelectionTemplate(stmplId);
		
		//创建条件对象
		Map<Long, String> criteriaMap = AdminModulesController.exractTemplateCriteriaMap(request);
		SelectionTemplateEntityViewCriteria criteria = new SelectionTemplateEntityViewCriteria(stmpl, criteriaMap);
		//设置条件
		criteria.setExistCodes(TextUtils.split(exists, ",", HashSet<String>::new, e->e));
		criteria.setPageInfo(pageInfo);
		criteria.setUser(UserUtils.getCurrentUser());
		//执行查询
		SelectionTemplateEntityView view = (SelectionTemplateEntityView) vService.query(criteria);
		model.addAttribute("view", view);
		
		//隐藏条件拼接成文件用于提示
		Set<TemplateSelectionCriteria> tCriterias = view.getSelectionTemplate().getCriterias();
		StringBuffer hidenCriteriaDesc = new StringBuffer();
		if(tCriterias != null){
			for (TemplateSelectionCriteria tCriteria : tCriterias) {
				if(tCriteria.getQueryShow() == null && TextUtils.hasText(tCriteria.getDefaultValue()) && tCriteria.getFieldAvailable()) {
					hidenCriteriaDesc.append(tCriteria.getTitle() + ":" + tCriteria.getDefaultValue() + "&#10;");
				}
			}
		}
		
		model.addAttribute("stmpl", stmpl);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_TMPL_ACTION + "/atmpl_entity_selection.jsp";
	}
	
	
	@ResponseBody
	@RequestMapping("/load_entities/{stmplId}")
	public ResponseJSON loadEntities(
			@PathVariable Long stmplId,
			@RequestParam String codes, 
			@RequestParam String fields) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateSelectionTemplate stmpl = tService.getSelectionTemplate(stmplId);
		Map<String, CEntityPropertyParser> parsers = mService.getEntityParsers(
				stmpl.getModule(), 
				stmpl.getRelationName(), 
				TextUtils.split(codes, ",", HashSet<String>::new, c->c), UserUtils.getCurrentUser())
				;
		JSONObject entities = AdminModulesController.toEntitiesJson(parsers, TextUtils.split(fields, ",", HashSet<String>::new, f->f));
		jRes.put("entities", entities);
		jRes.setStatus("suc");
		return jRes;
	}
	
	
	
}
