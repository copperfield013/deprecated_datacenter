package cn.sowell.datacenter.admin.controller.tmpl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/ltmpl")
public class AdminListTemplateController {
	
	@Resource
	ListTemplateService tService;
	
	@Resource
	SystemAdminService adminService;
	
	Logger logger = Logger.getLogger(AdminListTemplateController.class);
	
	
	@RequestMapping("/list")
	public String list(Model model){
		UserIdentifier user = UserUtils.getCurrentUser();
		List<TemplateListTmpl> ltmplList = tService.queryLtmplList(user);
		model.addAttribute("ltmplList", ltmplList);
		model.addAttribute("sysAdmin", adminService.getSystemAdminByUserId(user.getId()));
		return AdminConstants.JSP_TMPL_LIST + "/ltmpl_list.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/as_default/{ltmplId}")
	public AjaxPageResponse asDefault(@PathVariable Long ltmplId){
		try {
			adminService.setListTemplateAsDefault(ltmplId, UserUtils.getCurrentUser());
			return AjaxPageResponse.REFRESH_LOCAL("操作成功");
		} catch (Exception e) {
			logger.error("设置默认列表模板时发生错误", e);
		}
		return AjaxPageResponse.FAILD("操作失败");
	}
	
	@RequestMapping("/add")
	public String add(){
		return AdminConstants.JSP_TMPL_LIST + "/ltmpl_update.jsp";
	}
	
	@RequestMapping("/update/{ltmplId}")
	public String update(@PathVariable Long ltmplId, Model model){
		TemplateListTmpl ltmpl = tService.getListTemplate(ltmplId);
		JSONArray columnDataJSON = toColumnData(ltmpl.getColumns());
		JSONObject tmplDataJSON = toLtmplData(ltmpl);
		model.addAttribute("ltmpl", ltmpl);
		model.addAttribute("tmplDataJSON", tmplDataJSON);
		model.addAttribute("columnDataJSON", columnDataJSON);
		return AdminConstants.JSP_TMPL_LIST + "/ltmpl_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/remove/{ltmplId}")
	public AjaxPageResponse remove(@PathVariable Long ltmplId){
		try {
			tService.removeListTemplate(ltmplId);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除模板时发生错误", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	
	private JSONObject toLtmplData(TemplateListTmpl ltmpl) {
		JSONObject json = new JSONObject();
		json.put("title", ltmpl.getTitle());
		json.put("unmodifiable", ltmpl.getUnmodifiable());
		json.put("defaultOrderFieldId", ltmpl.getDefaultOrderFieldId());
		json.put("defaultOrderDirection", ltmpl.getDefaultOrderDirection());
		json.put("defaultPageSize", ltmpl.getDefaultPageSize());
		json.put("id", ltmpl.getId());
		return json;
	}

	private JSONArray toColumnData(Set<TemplateListColumn> columns) {
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
		TemplateListTmpl tmpl = generateLtmplData(jReq);
		try {
			tService.saveListTemplate(tmpl);
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("保存列表模板时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}

	private TemplateListTmpl generateLtmplData(JsonRequest jReq) {
		TemplateListTmpl tmpl = null;
		if(jReq != null && jReq.getJsonObject() != null){
			JSONObject json = jReq.getJsonObject();
			tmpl = new TemplateListTmpl();
			tmpl.setId(json.getLong("tmplId"));
			tmpl.setTitle(json.getString("title"));
			tmpl.setDefaultPageSize(json.getInteger("defPageSize"));
			tmpl.setDefaultOrderFieldId(json.getLong("defOrderFieldId"));
			tmpl.setDefaultOrderDirection(json.getString("defOrderDir"));
			tmpl.setCreateUserId((Long) UserUtils.getCurrentUser().getId());
			tmpl.setUnmodifiable(null);
			JSONArray columnData = json.getJSONArray("columnData");
			if(columnData != null){
				Set<TemplateListColumn> columns = new LinkedHashSet<TemplateListColumn>();
				int i = 0;
				for (Object c : columnData) {
					JSONObject src = (JSONObject) c;
					TemplateListColumn column = new TemplateListColumn();
					column.setTitle(src.getString("title"));
					column.setOrderable(src.getInteger("orderable"));
					column.setCreateUserId(tmpl.getCreateUserId());
					if(src.getString("specField") != null){
						column.setSpecialField(src.getString("specField"));
					}else{
						column.setFieldId(src.getLong("fieldId"));
						column.setFieldKey(src.getString("fieldKey"));
					}
					column.setOrder(i++);
					columns.add(column);
				}
				tmpl.setColumns(columns);
			}
		}
		return tmpl;
	}
	
	
	
	
	
}
