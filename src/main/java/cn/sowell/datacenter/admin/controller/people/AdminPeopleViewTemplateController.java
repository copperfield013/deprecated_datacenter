package cn.sowell.datacenter.admin.controller.people;

import java.util.List;

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
import cn.sowell.copframe.dto.ajax.JsonArrayResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.admin.service.SystemAdminService;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateData;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateField;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateGroup;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;
import cn.sowell.datacenter.model.system.pojo.SystemAdmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(AdminConstants.URI_PEOPLEDATA + "/viewtmpl")
public class AdminPeopleViewTemplateController {

	@Resource
	PeopleDictionaryService dictService;

	@Resource
	SystemAdminService adminService;
	
	Logger logger = Logger.getLogger(AdminPeopleViewTemplateController.class);
	
	@RequestMapping("/to_create")
	public String toCreate(){
		return AdminConstants.JSP_PEOPLEDATA_VIEWTMPL + "/viewtmpl_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/field_json")
	public ResponseJSON fieldJson(){
		List<PeopleCompositeDictionaryItem> infoList = dictService.getAllInfo(null);
		JsonArrayResponse jRes = new JsonArrayResponse();
		for (PeopleCompositeDictionaryItem info : infoList) {
			jRes.add(JSON.toJSON(info));
		}
		return jRes;
	}
	
	@RequestMapping("/list")
	public String tmplList(Model model){
		UserIdentifier user = UserUtils.getCurrentUser();
		List<PeopleTemplateData> tmplList = dictService.getAllTemplateList(user, null, false);
		SystemAdmin sysAdmin = adminService.getSystemAdminByUserId((Long) user.getId());
		model.addAttribute("tmplList", tmplList);
		model.addAttribute("sysAdmin", sysAdmin);
		return AdminConstants.JSP_PEOPLEDATA_VIEWTMPL + "/viewtmpl_list.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON saveTmpl(@RequestBody JsonRequest jReq){
		JSONObjectResponse jRes = new JSONObjectResponse();
		PeopleTemplateData data = parseToTmplData(jReq.getJsonObject());
		try {
			dictService.mergeTemplate(data);
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("保存模板时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	@RequestMapping("/update/{tmplId}")
	public String update(@PathVariable Long tmplId, Model model){
		PeopleTemplateData tmpl = dictService.getTemplate(tmplId);
		JSONObject tmplJson = (JSONObject) JSON.toJSON(tmpl);
		model.addAttribute("tmpl", tmpl);
		model.addAttribute("tmplJson", tmplJson);
		return AdminConstants.JSP_PEOPLEDATA_VIEWTMPL + "/viewtmpl_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/remove/{tmplId}")
	public AjaxPageResponse remove(@PathVariable Long tmplId){
		try {
			UserIdentifier user = UserUtils.getCurrentUser();
			dictService.removeTemplate(user, tmplId);
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
			adminService.setTmplAsDefault(tmplId, user);
			return AjaxPageResponse.REFRESH_LOCAL("设置成功");
		} catch (Exception e) {
			logger.error("设置用户默认模板失败", e);
			return AjaxPageResponse.FAILD("操作失败");
		}
	}
	
	
	private PeopleTemplateData parseToTmplData(JSONObject jo) {
		if(jo != null){
			PeopleTemplateData data = new PeopleTemplateData();
			data.setTmplId(jo.getLong("tmplId"));
			data.setName(jo.getString("name"));
			JSONArray jGroups = jo.getJSONArray("groups");
			if(jGroups != null && !jGroups.isEmpty()){
				int i = 0;
				for (Object ele : jGroups) {
					if(ele instanceof JSONObject){
						JSONObject jGroup = (JSONObject) ele;
						PeopleTemplateGroup group = new PeopleTemplateGroup();
						group.setId(jGroup.getLong("id"));
						group.setTitle(jGroup.getString("title"));
						group.setOrder(i++);
						data.getGroups().add(group);
						JSONArray jFields = jGroup.getJSONArray("fields");
						if(jFields != null && !jFields.isEmpty()){
							int j = 0;
							for (Object ele1 : jFields) {
								if(ele1 instanceof JSONObject){
									JSONObject jField = (JSONObject) ele1;
									PeopleTemplateField field = new PeopleTemplateField();
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
