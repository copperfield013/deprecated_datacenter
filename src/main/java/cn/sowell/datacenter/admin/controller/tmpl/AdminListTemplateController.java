package cn.sowell.datacenter.admin.controller.tmpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.common.choose.ChooseTablePage;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.dataserver.model.dict.service.DictionaryService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;
import cn.sowell.dataserver.model.tmpl.service.ListTemplateService;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/ltmpl")
@PreAuthorize("hasAuthority(@confAuthenService.getAdminConfigAuthen())")
public class AdminListTemplateController {
	
	Logger logger = Logger.getLogger(AdminListTemplateController.class);

	@Resource
	ListTemplateService ltmplService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	DictionaryService dictService;

	@Resource
	private ModulesService mService;
	
	@Resource
	ConfigureService configService;
	
	@RequestMapping("/list/{moduleName}")
	public String list(Model model, @PathVariable String moduleName){
		ModuleMeta moduleMeta = mService.getModule(moduleName);
		List<TemplateListTemplate> ltmplList = ltmplService.queryAll(moduleName);
		Map<Long, List<TemplateGroup>> relatedGroupsMap = ltmplService.getRelatedGroupsMap(CollectionUtils.toSet(ltmplList, ltmpl->ltmpl.getId()));
		model.addAttribute("modulesJson", configService.getSiblingModulesJson(moduleName));
		model.addAttribute("ltmplList", ltmplList);
		model.addAttribute("module", moduleMeta);
		model.addAttribute("relatedGroupsMap", relatedGroupsMap);
		return AdminConstants.JSP_TMPL_LIST + "/ltmpl_list.jsp";
	}
	
	@RequestMapping("/choose/{module}")
	public String dialogList(@PathVariable String module, String except, Model model) {
		List<TemplateListTemplate> list = ltmplService.queryAll(module);
		if(TextUtils.hasText(except)) {
			Set<Long> excepts = TextUtils.split(except, ",", HashSet::new, FormatUtils::toLong);
			list = list.stream().filter(tmpl->!excepts.contains(tmpl.getId())).collect(Collectors.toList());
		}
		ChooseTablePage<TemplateListTemplate> tpage = new ChooseTablePage<TemplateListTemplate>(
				"ltmpl-choose-list", "ltmpl_");
		tpage
			.setPageInfo(null)
			.setAction(AdminConstants.URI_TMPL + "/ltmpl/choose/" + module)
			.setIsMulti(false)
			.setTableData(list, handler->{
				handler
					.setDataKeyGetter(data->"ltmpl_" + data.getId())
					.addColumn("模板名", (cell, data)->cell.setText(data.getTitle()))
					.addColumn("创建时间", (cell, data)->cell.setText(dateFormat.formatDateTime(data.getCreateTime())))
					;
			})
			;
		
		model.addAttribute("tpage", tpage);
		return AdminConstants.PATH_CHOOSE_TABLE;
	}
	
	@ResponseBody
	@RequestMapping("/switch_groups/{ltmplId}/{targetLtmplId}")
	public AjaxPageResponse switchGroups(@PathVariable Long ltmplId, @PathVariable Long targetLtmplId) {
		try {
			TemplateListTemplate ltmpl = ltmplService.getTemplate(ltmplId),
									targerTemplate = ltmplService.getTemplate(targetLtmplId);
			if(ltmpl != null) {
				if(targerTemplate != null) {
					ltmplService.switchAllRelatedGroups(ltmplId, targetLtmplId);
					return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("切换成功", ltmpl.getModule() + "_dtmpl_list");
				}else {
					throw new Exception("切换详情模板的列表模板[id=" + targetLtmplId + "]不存在");
				}
			}else {
				throw new Exception("原详情模板[id=" + ltmplId + "]不存在");
			}
		} catch (Exception e) {
			logger.error("切换时发生错误", e);
		}
		return AjaxPageResponse.FAILD("切换失败");
	}
	
	
	@RequestMapping("/add/{module}")
	public String add(@PathVariable String module, Model model){
		ModuleMeta moduleMeta = mService.getModule(module);
		model.addAttribute("fieldInputTypeMap", JSON.toJSON(dictService.getFieldInputTypeMap()));
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_TMPL_LIST + "/ltmpl_update.jsp";
	}
	
	@RequestMapping("/update/{ltmplId}")
	public String update(@PathVariable Long ltmplId, Model model){
		TemplateListTemplate ltmpl = ltmplService.getTemplate(ltmplId);
		JSONArray columnDataJSON = toColumnData(ltmpl.getColumns());
		JSONObject tmplDataJSON = toLtmplData(ltmpl);
		JSONArray criteriaDataJSON = toCriteriaData(ltmpl.getCriterias());
		ModuleMeta moduleMeta = mService.getModule(ltmpl.getModule());
		model.addAttribute("module", moduleMeta);
		model.addAttribute("ltmpl", ltmpl);
		model.addAttribute("tmplDataJSON", tmplDataJSON);
		model.addAttribute("columnDataJSON", columnDataJSON);
		model.addAttribute("criteriaDataJSON", criteriaDataJSON);
		return AdminConstants.JSP_TMPL_LIST + "/ltmpl_update.jsp";
	}
	
	
	@RequestMapping("/group_list/{ltmplId}")
	public String groupList(@PathVariable Long ltmplId, Model model) {
		TemplateListTemplate ltmpl = ltmplService.getTemplate(ltmplId);
		List<TemplateGroup> tmplGroups = ltmplService.getRelatedGroups(ltmplId);
		model.addAttribute("tmplGroups", tmplGroups);
		model.addAttribute("tmplType", "list");
		model.addAttribute("tmplId", ltmplId);
		model.addAttribute("tmpl", ltmpl);
		return AdminConstants.JSP_TMPL_GROUP + "/tmpl_group_list_from_tmpl.jsp";
	}
	
	
	private JSONArray toCriteriaData(List<TemplateListCriteria> list) {
		JSONArray array = new JSONArray();
		for (TemplateListCriteria criteria : list) {
			Object item = JSON.toJSON(criteria);
			array.add(item);
		}
		return array;
	}

	@ResponseBody
	@RequestMapping("/remove/{ltmplId}")
	public AjaxPageResponse remove(@PathVariable Long ltmplId){
		try {
			ltmplService.remove(ltmplId);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除模板时发生错误", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	
	private JSONObject toLtmplData(TemplateListTemplate ltmpl) {
		JSONObject json = new JSONObject();
		json.put("title", ltmpl.getTitle());
		json.put("unmodifiable", ltmpl.getUnmodifiable());
		json.put("defaultOrderFieldId", ltmpl.getDefaultOrderFieldId());
		json.put("defaultOrderDirection", ltmpl.getDefaultOrderDirection());
		json.put("defaultPageSize", ltmpl.getDefaultPageSize());
		json.put("id", ltmpl.getId());
		return json;
	}

	private JSONArray toColumnData(List<TemplateListColumn> columns) {
		JSONArray json = new JSONArray();
		for (TemplateListColumn column : columns) {
			JSONObject col = new JSONObject();
			col.put("id", column.getId());
			col.put("fieldId", column.getFieldId());
			String compositeName = "",
					fieldName = column.getFieldKey();
			if(fieldName != null && fieldName.contains("\\.")){
				int dotIndex = fieldName.lastIndexOf("\\.");
				compositeName = fieldName.substring(0, dotIndex);
				fieldName = fieldName.substring(dotIndex + 1, fieldName.length());
			}
			col.put("fieldAvailable", column.getFieldAvailable());
			col.put("compositeName", compositeName);
			col.put("fieldName", fieldName);
			col.put("title", column.getTitle());
			col.put("specialField", column.getSpecialField());
			json.add(col);
		}
		return json;
	}

	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON save(@RequestBody JsonRequest jReq){
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateListTemplate tmpl = generateLtmplData(jReq);
		try {
			ltmplService.merge(tmpl);
			//ltService.saveListTemplate(tmpl);
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("保存列表模板时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}

	private TemplateListTemplate generateLtmplData(JsonRequest jReq) {
		TemplateListTemplate tmpl = null;
		if(jReq != null && jReq.getJsonObject() != null){
			JSONObject json = jReq.getJsonObject();
			tmpl = new TemplateListTemplate();
			tmpl.setId(json.getLong("tmplId"));
			tmpl.setTitle(json.getString("title"));
			tmpl.setDefaultPageSize(json.getInteger("defPageSize"));
			tmpl.setDefaultOrderFieldId(json.getLong("defOrderFieldId"));
			tmpl.setDefaultOrderDirection(json.getString("defOrderDir"));
			tmpl.setCreateUserCode((String) UserUtils.getCurrentUser().getId());
			tmpl.setUnmodifiable(null);
			tmpl.setModule(json.getString("module"));
			JSONArray columnData = json.getJSONArray("columnData");
			if(columnData != null){
				List<TemplateListColumn> columns = new ArrayList<TemplateListColumn>();
				int i = 0;
				for (Object c : columnData) {
					JSONObject src = (JSONObject) c;
					TemplateListColumn column = new TemplateListColumn();
					column.setTitle(src.getString("title"));
					column.setOrderable(src.getInteger("orderable"));
					if(src.getString("specField") != null){
						column.setSpecialField(src.getString("specField"));
					}else{
						column.setFieldId(src.getLong("fieldId"));
					}
					column.setOrder(i++);
					columns.add(column);
				}
				tmpl.setColumns(columns);
			}
			
			JSONArray criteriaData = json.getJSONArray("criteriaData");
			if(criteriaData != null){
				List<TemplateListCriteria> criterias = new ArrayList<TemplateListCriteria>();
				int order = 0;
				for (Object e : criteriaData) {
					JSONObject item = (JSONObject) e;
					TemplateListCriteria criteria = new TemplateListCriteria();
					criteria.setRelation("and");
					criteria.setId(item.getLong("id"));
					criteria.setTitle(item.getString("title"));
					criteria.setOrder(order++);
					if(item.getBooleanValue("fieldAvailable")) {
						criteria.setFieldId(item.getLong("fieldId"));
						criteria.setCompositeId(item.getLong("compositeId"));
						criteria.setRelationLabel(item.getString("relationLabel"));
						//条件需要显示
						criteria.setComparator(item.getString("comparator"));
						criteria.setInputType(item.getString("inputType"));
						criteria.setDefaultValue(item.getString("defVal"));
						Boolean queryShow = item.getBoolean("queryShow");
						if(queryShow != null && queryShow){
							criteria.setQueryShow(1);
							criteria.setPlaceholder(item.getString("placeholder"));
						}
					}else {
						criteria.setFieldUnavailable();
					}
					criterias.add(criteria);
				}
				tmpl.setCriterias(criterias);
			}
			
		}
		return tmpl;
	}
	
	@ResponseBody
	@RequestMapping("/copy/{ltmplId}/{targetModuleName}")
	public ResponseJSON copy(@PathVariable Long ltmplId, @PathVariable String targetModuleName) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		try {
			Long newTmplId = ltmplService.copy(ltmplId, targetModuleName);
			if(newTmplId != null) {
				jRes.setStatus("suc");
				jRes.put("newTmplId", newTmplId);
			}
		} catch (Exception e) {
			logger.error("复制列表模板时发生错误", e);
		}
		return jRes;
	}
	
	
	
}
