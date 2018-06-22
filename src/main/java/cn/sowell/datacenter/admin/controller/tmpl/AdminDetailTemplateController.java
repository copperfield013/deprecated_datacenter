package cn.sowell.datacenter.admin.controller.tmpl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copFrame.dto.choose.ChooseTablePage;
import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.modules.pojo.ModuleMeta;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailField;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/dtmpl")
public class AdminDetailTemplateController {

	@Resource
	SystemAdminService adminService;
	
	@Resource
	TemplateService tService;
	
	Logger logger = Logger.getLogger(AdminDetailTemplateController.class);

	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	ModulesService mService;
	
	@RequestMapping("/to_create/{module}")
	public String toCreate(@PathVariable String module, Model model){
		ModuleMeta moduleMeta = mService.getModule(module);
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_update.jsp";
	}
	
	@RequestMapping("/list/{module}")
	public String tmplList(Model model, @PathVariable String module){
		UserIdentifier user = UserUtils.getCurrentUser();
		ModuleMeta moduleMeta = mService.getModule(module);
		List<TemplateDetailTemplate> tmplList = tService.getAllDetailTemplateList(module, user, null, false);
		TemplateDetailTemplate defDetailTemplate = tService.getDefaultDetailTemplate(user, module);
		model.addAttribute("tmplList", tmplList);
		model.addAttribute("defaultTemplate", defDetailTemplate);
		model.addAttribute("module", moduleMeta);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_list.jsp";
	}
	

	@RequestMapping("/choose/{module}")
	public String dialogList(@PathVariable String module, Model model) {
		UserIdentifier user = UserUtils.getCurrentUser();
		List<TemplateDetailTemplate> tmplList = tService.getAllDetailTemplateList(module, user, null, false);
		ChooseTablePage<TemplateDetailTemplate> tpage = new ChooseTablePage<TemplateDetailTemplate>(
				"ltmpl-choose-list", "ltmpl_");
		tpage
			.setPageInfo(null)
			.setAction(AdminConstants.URI_TMPL + "/dtmpl/choose/" + module)
			.setIsMulti(false)
			.setTableData(tmplList, handler->{
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
	@RequestMapping("/save")
	public ResponseJSON saveTmpl(@RequestBody JsonRequest jReq){
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateDetailTemplate data = parseToTmplData(jReq.getJsonObject());
		try {
			tService.mergeTemplate(data);
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("保存模板时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	@RequestMapping("/update/{tmplId}")
	public String update(@PathVariable Long tmplId, Model model){
		TemplateDetailTemplate tmpl = tService.getDetailTemplate(tmplId);
		JSONObject tmplJson = (JSONObject) JSON.toJSON(tmpl);
		ModuleMeta moduleMeta = mService.getModule(tmpl.getModule());
		model.addAttribute("module", moduleMeta);
		model.addAttribute("tmpl", tmpl);
		model.addAttribute("tmplJson", tmplJson);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/remove/{tmplId}")
	public AjaxPageResponse remove(@PathVariable Long tmplId){
		try {
			UserIdentifier user = UserUtils.getCurrentUser();
			tService.removeTemplate(user, tmplId, DataCenterConstants.TEMPLATE_TYPE_DETAIL);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/as_default/{tmplId}")
	public AjaxPageResponse setTmplAsDefault(@PathVariable Long tmplId){
		UserIdentifier user = UserUtils.getCurrentUser();
		try {
			tService.setTemplateAsDefault(user, tmplId, DataCenterConstants.TEMPLATE_TYPE_DETAIL);
			return AjaxPageResponse.REFRESH_LOCAL("设置成功");
		} catch (Exception e) {
			logger.error("设置用户默认模板失败", e);
			return AjaxPageResponse.FAILD("操作失败");
		}
	}
	
	
	private TemplateDetailTemplate parseToTmplData(JSONObject jo) {
		if(jo != null){
			TemplateDetailTemplate data = new TemplateDetailTemplate();
			data.setId(jo.getLong("tmplId"));
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
	
}
