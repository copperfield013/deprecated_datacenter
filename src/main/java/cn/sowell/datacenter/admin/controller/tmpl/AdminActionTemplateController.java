package cn.sowell.datacenter.admin.controller.tmpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.config.service.ConfigureService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateActionTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
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
	
	static Logger logger = Logger.getLogger(AdminActionTemplateController.class);
	
	
	@RequestMapping("/list/{moduleName}")
	public String list(Model model, @PathVariable String moduleName) {
		ModuleMeta moduleMeta = mService.getModule(moduleName);
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
	
	
	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON save(@RequestBody JsonRequest jReq) {
		JSONObjectResponse jRes = new JSONObjectResponse();
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
						}
						
					}
					
				}
			}
			return data; 
		}
		return null;
	}
}
