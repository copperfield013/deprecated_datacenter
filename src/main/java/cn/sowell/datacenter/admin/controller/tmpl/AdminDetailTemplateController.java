package cn.sowell.datacenter.admin.controller.tmpl;

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
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/dtmpl")
@PreAuthorize("hasAuthority(@confAuthenService.getAdminConfigAuthen())")
public class AdminDetailTemplateController {


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
		ModuleMeta moduleMeta = mService.getModule(module);
		List<TemplateDetailTemplate> tmplList = tService.queryDetailTemplates(module);
		Map<Long, Set<TemplateGroup>> relatedGroupsMap = tService.getDetailTemplateRelatedGroupsMap(CollectionUtils.toSet(tmplList, dtmpl->dtmpl.getId()));
		model.addAttribute("tmplList", tmplList);
		model.addAttribute("module", moduleMeta);
		model.addAttribute("relatedGroupsMap", relatedGroupsMap);
		return AdminConstants.JSP_TMPL_DETAIL + "/dtmpl_list.jsp";
	}
	

	@RequestMapping("/choose/{module}")
	public String dialogList(@PathVariable String module, String except, Model model) {
		List<TemplateDetailTemplate> tmplList = tService.queryDetailTemplates(module);
		if(TextUtils.hasText(except)) {
			Set<Long> excepts = TextUtils.split(except, ",", HashSet::new, FormatUtils::toLong);
			tmplList = tmplList.stream().filter(tmpl->!excepts.contains(tmpl.getId())).collect(Collectors.toList());
		}
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
	@RequestMapping("/switch_groups/{dtmplId}/{targetDtmplId}")
	public AjaxPageResponse switchGroups(@PathVariable Long dtmplId, @PathVariable Long targetDtmplId) {
		try {
			TemplateDetailTemplate dtmpl = tService.getDetailTemplate(dtmplId),
									targerTemplate = tService.getDetailTemplate(targetDtmplId);
			if(dtmpl != null) {
				if(targerTemplate != null) {
					tService.switchAllGroupsDetailTemplate(dtmplId, targetDtmplId);
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
			tService.removeDetailTemplate(tmplId);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	@RequestMapping("/group_list/{dtmplId}")
	public String groupList(@PathVariable Long dtmplId, Model model) {
		TemplateDetailTemplate dtmpl = tService.getDetailTemplate(dtmplId);
		Set<TemplateGroup> tmplGroups = tService.getDetailTemplateRelatedGroups(dtmplId);
		model.addAttribute("tmplGroups", tmplGroups);
		model.addAttribute("tmplType", "detail");
		model.addAttribute("tmplId", dtmplId);
		model.addAttribute("tmpl", dtmpl);
		return AdminConstants.JSP_TMPL_GROUP + "/tmpl_group_list_from_tmpl.jsp";
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
						group.setSelectionTemplateId(jGroup.getLong("selectionTemplateId"));
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
	
	
}
