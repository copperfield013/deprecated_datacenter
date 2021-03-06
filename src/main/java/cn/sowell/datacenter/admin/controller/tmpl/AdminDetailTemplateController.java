package cn.sowell.datacenter.admin.controller.tmpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.admin.controller.tmpl.CommonTemplateActionConsumer.ChooseRequestParam;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailArrayItemCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailArrayItemFilter;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroupTreeNode;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.service.ArrayItemFilterService;
import cn.sowell.dataserver.model.tmpl.service.DetailTemplateService;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/dtmpl")
@PreAuthorize("hasAuthority(@confAuthenService.getAdminConfigAuthen())")
public class AdminDetailTemplateController {

	@Resource
	DetailTemplateService dtmplService;
	
	Logger logger = Logger.getLogger(AdminDetailTemplateController.class);

	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	ModulesService mService;
	
	@Resource
	ConfigureService configService;
	
	@Resource
	ArrayItemFilterService arrayItemFilterService;
	
	@Resource
	CommonTemplateActionConsumer actionConsumer;
	
	@RequestMapping("/to_create/{module}")
	public String toCreate(@PathVariable String module, Model model){
		ModuleMeta moduleMeta = mService.getModule(module);
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_update.jsp";
	}
	
	@RequestMapping("/list/{moduleName}")
	public String list(Model model, @PathVariable String moduleName){
		ModuleMeta moduleMeta = mService.getModule(moduleName);
		List<TemplateDetailTemplate> tmplList = dtmplService.queryAll(moduleName);
		Map<Long, List<TemplateGroup>> relatedGroupsMap = dtmplService.getRelatedGroupsMap(CollectionUtils.toSet(tmplList, dtmpl->dtmpl.getId()));
		model.addAttribute("modulesJson", configService.getSiblingModulesJson(moduleName));
		model.addAttribute("tmplList", tmplList);
		model.addAttribute("module", moduleMeta);
		model.addAttribute("relatedGroupsMap", relatedGroupsMap);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_list.jsp";
	}
	

	@RequestMapping("/choose/{moduleName}")
	public String choose(@PathVariable String moduleName, String except, Model model) {
		return actionConsumer.choose(
				ChooseRequestParam.create(moduleName, dtmplService, model)
					.setExcept(except)
					.setURI(AdminConstants.URI_TMPL + "/dtmpl/choose/" +moduleName)
			);
	}
	
	@ResponseBody
	@RequestMapping("/switch_groups/{dtmplId}/{targetDtmplId}")
	public AjaxPageResponse switchGroups(@PathVariable Long dtmplId, @PathVariable Long targetDtmplId) {
		try {
			TemplateDetailTemplate dtmpl = dtmplService.getTemplate(dtmplId),
									targerTemplate = dtmplService.getTemplate(targetDtmplId);
			if(dtmpl != null) {
				if(targerTemplate != null) {
					dtmplService.switchAllRelatedGroups(dtmplId, targetDtmplId);
					return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("切换成功", dtmpl.getModule() + "_dtmpl_list");
				}else {
					throw new Exception("切换详情模板的列表模板[id=" + targetDtmplId + "]不存在");
				}
			}else {
				throw new Exception("原详情模板[id=" + dtmplId + "]不存在");
			}
		} catch (Exception e) {
			logger.error("切换时发生错误", e);
		}
		return AjaxPageResponse.FAILD("切换失败");
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON saveTmpl(@RequestBody JsonRequest jReq){
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateDetailTemplate data = parseToTmplData(jReq.getJsonObject());
		try {
			Long dtmplId = dtmplService.merge(data);
			jRes.put("dtmplId", dtmplId);
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("保存模板时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	@RequestMapping("/update/{tmplId}")
	public String update(@PathVariable Long tmplId, Model model){
		TemplateDetailTemplate tmpl = dtmplService.getTemplate(tmplId);
		JSONObject tmplJson = (JSONObject) JSON.toJSON(tmpl);
		ModuleMeta moduleMeta = mService.getModule(tmpl.getModule());
		model.addAttribute("module", moduleMeta);
		model.addAttribute("tmpl", tmpl);
		model.addAttribute("tmplJson", tmplJson);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_update.jsp";
	}
	
	@RequestMapping("/relation_dtmpl/{moduleName}/{relationCompositeId}")
	public String citeRelationDtmpl(
			@PathVariable String moduleName, 
			@PathVariable Long relationCompositeId,
			Long dtmplId,
			Model model) {
		ModuleMeta relationCompositeModule = mService.getCompositeRelatedModule(moduleName, relationCompositeId);
		if(relationCompositeModule != null) {
			model.addAttribute("module", relationCompositeModule);
			if(dtmplId != null) {
				TemplateDetailTemplate dtmpl = dtmplService.getTemplate(dtmplId);
				model.addAttribute("tmpl", dtmpl);
				model.addAttribute("tmplJson", JSON.toJSON(dtmpl));
			}
			ModuleMeta mainModule = mService.getModule(moduleName);
			model.addAttribute("mainModule", mainModule);
			model.addAttribute("relationCompositeId", relationCompositeId);
			return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_update.jsp";
		}
		return null; 
	}
	
	
	@ResponseBody
	@RequestMapping("/remove/{tmplId}")
	public AjaxPageResponse remove(@PathVariable Long tmplId){
		try {
			dtmplService.remove(tmplId);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	@RequestMapping("/group_list/{dtmplId}")
	public String groupList(@PathVariable Long dtmplId, Model model) {
		TemplateDetailTemplate dtmpl = dtmplService.getTemplate(dtmplId);
		List<TemplateGroup> tmplGroups = dtmplService.getRelatedGroups(dtmplId);
		model.addAttribute("tmplGroups", tmplGroups);
		model.addAttribute("tmplType", "detail");
		model.addAttribute("tmplId", dtmplId);
		model.addAttribute("tmpl", dtmpl);
		return AdminConstants.JSP_TMPL_GROUP + "/tmpl_group_list_from_tmpl.jsp";
	}
	
	
	private TemplateDetailTemplate parseToTmplData(JSONObject jo) {
		if(jo != null){
			TemplateDetailTemplate data = new TemplateDetailTemplate();
			if(!"new".equals(jo.getString("saveMethod"))) {
				data.setId(jo.getLong("tmplId"));
			}
			data.setRange(jo.getInteger("range"));
			data.setTitle(jo.getString("title"));
			data.setModule(jo.getString("module"));
			JSONArray jGroups = jo.getJSONArray("groups");
			if(jGroups != null && !jGroups.isEmpty()){
				int i = 0;
				for (Object ele : jGroups) {
					if(ele instanceof JSONObject){
						JSONObject jGroup = (JSONObject) ele;
						TemplateDetailFieldGroup group = new TemplateDetailFieldGroup();
						group.setId(jGroup.getLong("id"));
						group.setTitle(jGroup.getString("title"));
						group.setIsArray(jGroup.getBoolean("isArray")?1:null);
						group.setCompositeId(jGroup.getLong("compositeId"));
						group.setDialogSelectType(jGroup.getString("dialogSelectType"));
						group.setRabcTemplateGroupId(jGroup.getLong("rabcTemplateGroupId"));
						group.setRabcUncreatable(jGroup.getInteger("rabcUncreatable"));
						group.setRabcUnupdatable(jGroup.getInteger("rabcUnupdatable"));
						group.setArrayItemFilterId(jGroup.getLong("arrayItemFilterId"));
						group.setSelectionTemplateId(jGroup.getLong("selectionTemplateId"));
						group.setUnallowedCreate(Integer.valueOf(1).equals(jGroup.getInteger("unallowedCreate"))? 1: null);
						group.setRabcTreeTemplateId(jGroup.getLong("rabcTreeTemplateId"));
						JSONArray jRabcTreeNodeIds = jGroup.getJSONArray("rabcTreeNodeIds");
						if(jRabcTreeNodeIds != null) {
							group.setRabcTreeNodes(new ArrayList<>());
							for (int j = 0; j < jRabcTreeNodeIds.size(); j++) {
								Long nodeId = jRabcTreeNodeIds.getLong(j);
								TemplateDetailFieldGroupTreeNode node = new TemplateDetailFieldGroupTreeNode();
								node.setNodeTemplateId(nodeId);
								node.setOrder(j);
								group.getRabcTreeNodes().add(node);
							}
						}
						group.setOrder(i++);
						data.getGroups().add(group);
						JSONArray jFields = jGroup.getJSONArray("fields");
						if(jFields != null && !jFields.isEmpty()){
							int j = 0;
							for (Object ele1 : jFields) {
								if(ele1 instanceof JSONObject){
									JSONObject jField = (JSONObject) ele1;
									TemplateDetailField field = new TemplateDetailField();
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
						}
						
					}
					
				}
			}
			return data; 
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/copy/{dtmplId}/{targetModuleName}")
	public ResponseJSON copy(@PathVariable Long dtmplId, @PathVariable String targetModuleName) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		try {
			Long newTmplId = dtmplService.copy(dtmplId, targetModuleName);
			if(newTmplId != null) {
				jRes.setStatus("suc");
				jRes.put("newTmplId", newTmplId);
			}
		} catch (Exception e) {
			logger.error("复制详情模板时发生错误", e);
		}
		return jRes;
	}
	
	
	@ResponseBody
	@RequestMapping("/load_dtmpls/{moduleName}")
	public ResponseJSON loadDetailTemplates(
			@PathVariable String moduleName, 
			Long dtmplId) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		try {
			List<TemplateDetailTemplate> tmplList = dtmplService.queryAll(moduleName);
			if(tmplList != null) {
				JSONArray jDtmpls = new JSONArray();
				tmplList.forEach((tmpl)->{
					JSONObject jDtmpl = new JSONObject();
					jDtmpl.put("id", tmpl.getId());
					jDtmpl.put("title", tmpl.getTitle());
					jDtmpls.add(jDtmpl);
				});
				jRes.put("dtmpls", jDtmpls);
				jRes.setStatus("suc");
			}
			
		} catch (Exception e) {
			logger.error("加载详情模板列表时发生错误[moduleName=" + moduleName + "]", e);
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	@RequestMapping("/arrayitem_filter/{moduleName}/{compositeId}")
	public String arrayItemFilter(@PathVariable String moduleName,
			@PathVariable Long compositeId, Long filterId, Model model) {
		ModuleMeta module = mService.getModule(moduleName);
		if(filterId != null) {
			TemplateDetailArrayItemFilter filter = arrayItemFilterService.getTemplate(filterId);
			if(filter != null) {
				model.addAttribute("filter", filter);
				JSONArray criteriaDataJSON = ListTemplateFormater.toCriteriaData(filter.getCriterias());
				model.addAttribute("criteriaDataJSON", criteriaDataJSON);
			}
		}
		model.addAttribute("module", module);
		model.addAttribute("compositeId", compositeId);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_arrayitem_filter_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/arrayitem_filter_save/{moduleName}/{compositeId}")
	public ResponseJSON arrayItemFilterSave(@PathVariable String moduleName, @PathVariable Long compositeId, @RequestBody JsonRequest jReq) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		Long filterId = jReq.getJsonObject().getLong("filterId");
		TemplateDetailArrayItemFilter filter = new TemplateDetailArrayItemFilter();
		filter.setModule(moduleName);
		filter.setCompositeId(compositeId);
		if(filterId != null) {
			filter.setId(filterId);
		}
		JSONArray jCriterias = jReq.getJsonObject().getJSONArray("criteriaData");
		List<TemplateDetailArrayItemCriteria> criterias = ListTemplateFormater.getCriterias(jCriterias, TemplateDetailArrayItemCriteria::new, null);
		if(criterias != null) {
			filter.setCriterias(criterias);
			filterId = arrayItemFilterService.merge(filter);
			if(filterId != null) {
				jRes.put("filterId", filterId);
				jRes.setStatus("suc");
			}
		}
		return jRes;
	}
	
}
