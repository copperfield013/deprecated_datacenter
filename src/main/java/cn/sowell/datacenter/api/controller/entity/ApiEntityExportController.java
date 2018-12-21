package cn.sowell.datacenter.api.controller.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.PollStatusResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.admin.controller.modules.AdminModulesExportController;
import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.dataserver.model.modules.bean.ExportDataPageInfo;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.modules.pojo.criteria.NormalCriteria;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@Controller
@RequestMapping("/api/entity/export")
public class ApiEntityExportController {
	@Resource
	TemplateService tService;
	
	@Resource
	ModulesService mService;
	
	@Resource
	ExportService eService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	AuthorityService authService;
	
	Logger logger = Logger.getLogger(AdminModulesExportController.class);
	
	@ResponseBody
	@RequestMapping("/start/{menuId}")
	public ResponseJSON doImport(
			@PathVariable Long menuId,
			@RequestBody JsonRequest jReq, ApiUser user){
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
		TemplateListTemplate ltmpl = tService.getListTemplate(tmplGroup.getListTemplateId());
		
		JSONObjectResponse jRes = new JSONObjectResponse();
		JSONObject json = jReq.getJsonObject();
		WorkProgress progress = new WorkProgress();
		progress.getDataMap().put("menuId", menuId);
		progress.getDataMap().put("menuTitle", menu.getTitle());
		String scope = json.getString("scope");
		Boolean withDetail = json.getBoolean("withDetail");
		boolean isCurrentScope = "current".equals(scope),
				isAllScope = "all".equals(scope);
		if(isCurrentScope || isAllScope){
			JSONObject parameters = json.getJSONObject("parameters");
			if(ltmpl != null){
				MutablePropertyValues pvs = new MutablePropertyValues();
				String prefix = "criteria_";
				parameters.forEach((key, val)->{
					if(key.startsWith(prefix)){
						String name = key.substring(prefix.length());
						if(val instanceof JSONArray){
							for (Object item : (JSONArray)val) {
								pvs.add(name, item);
							}
						}else{
							pvs.add(name, val);
						}
					}
				});
				ExportDataPageInfo ePageInfo = new ExportDataPageInfo();
				CommonPageInfo pageInfo = new CommonPageInfo();
				ePageInfo.setPageInfo(pageInfo);
				ePageInfo.setScope(scope);
				ePageInfo.setRangeStart(json.getInteger("rangeStart"));
				ePageInfo.setRangeEnd(json.getInteger("rangeEnd"));
				pageInfo.setPageNo(parameters.getInteger("pageNo"));
				pageInfo.setPageSize(parameters.getInteger("pageSize"));
				Map<Long, NormalCriteria> vCriteriaMap = mService.getCriteriasFromRequest(pvs, CollectionUtils.toMap(ltmpl.getCriterias(), c->c.getId()));
				progress.getDataMap().put("exportPageInfo", ePageInfo);
				progress.getDataMap().put("withDetail", withDetail);
				TemplateDetailTemplate dtmpl = Boolean.TRUE.equals(withDetail)? tService.getDetailTemplate(tmplGroup.getDetailTemplateId()): null;
				eService.startWholeExport(progress, ltmpl, dtmpl, new HashSet<NormalCriteria>(vCriteriaMap.values()), ePageInfo, user);
				user.setCache(AdminConstants.EXPORT_ENTITY_STATUS_UUID, progress.getUUID());
			}
		}
		jRes.put("uuid", progress.getUUID());
		return jRes;
	}
	

	@ResponseBody
	@RequestMapping("/status/{uuid}")
	public PollStatusResponse statusOfExport(@PathVariable String uuid, Boolean interrupted){
		PollStatusResponse status = new PollStatusResponse();
		status.setStatus("error");
		status.put("uuid", uuid);
		WorkProgress progress = eService.getExportProgress(uuid);
		if(progress != null){
			progress.veni();
			if(Boolean.TRUE.equals(interrupted)){
				eService.stopExport(uuid);
				status.setBreaked();
			}else{
				status.setCurrent(progress.getCurrent());
				status.setTotalCount(progress.getTotal());
				if(progress.isCompleted()) {
					status.setCompleted();
				}else if(progress.isBreaked()) {
					status.setBreaked();
				}
				status.setStatusMessage(progress.getLastMessage());
				status.putData(progress.getResponseData());
				
				
				status.setSuccessStatus();
			}
		}else{
			status.setStatusMessage("导出已超时，请重新导出");
		}
		return status;
	}
	
	@RequestMapping("/download/{uuid}")
	public ResponseEntity<byte[]> download(@PathVariable String uuid){
		AbstractResource resource = eService.getDownloadResource(uuid);
		if(resource != null) {
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentDispositionFormData("attachment", new String(
						("导出数据-" + dateFormat.format(new Date(), "yyyyMMddHHmmss") + ".xlsx").getBytes("UTF-8"),
						"iso-8859-1"));
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(resource.getFile()), headers, HttpStatus.CREATED);
			} catch (Exception e) {
				logger.error("下载导出文件时发生错误", e);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@ResponseBody
	@RequestMapping("/export_detail/{menuId}/{code}")
	public ResponseJSON exportDetail(
			@PathVariable Long menuId, 
			@PathVariable String code,
			Long historyId, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		SideMenuLevel2Menu menu = authService.vaidateUserL2MenuAccessable(user, menuId);
		TemplateGroup tmplGroup = tService.getTemplateGroup(menu.getTemplateGroupId());
		TemplateDetailTemplate dtmpl = tService.getDetailTemplate(tmplGroup.getDetailTemplateId());
		
		String moduleName = tmplGroup.getModule();
		ModuleEntityPropertyParser entity = null;
		EntityHistoryItem lastHistory = mService.getLastHistoryItem(moduleName, code, user);
		if(historyId != null) {
			if(lastHistory != null && !historyId.equals(lastHistory.getId())) {
				entity = mService.getHistoryEntityParser(moduleName, code, historyId, user);
			}
        }
        if(entity == null) {
        	entity = mService.getEntity(moduleName, code, null, user);
        }
		
		try {
			String uuid = eService.exportDetailExcel(entity, dtmpl);
			if(uuid != null) {
				jRes.put("uuid", uuid);
				jRes.setStatus("suc");
			}
		} catch (Exception e) {
			logger.error("导出时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}
	
	
	@ResponseBody
	@RequestMapping("/work/{uuid}")
	public ResponseJSON loadWork(@PathVariable String uuid, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		WorkProgress progress = eService.getExportProgress(uuid);
		if(progress != null) {
			jRes.put("menuId", progress.getDataMap().get("menuId"));
			jRes.put("menuTitle", progress.getDataMap().get("menuTitle"));
			jRes.put("uuid", uuid);
			ExportDataPageInfo ePageInfo = (ExportDataPageInfo) progress.getDataMap().get("exportPageInfo");
			jRes.put("scope", ePageInfo.getScope());
			jRes.put("rangeStart", ePageInfo.getRangeStart());
			jRes.put("rangeEnd", ePageInfo.getRangeEnd());
			jRes.put("withDetail", progress.getDataMap().get("withDetail"));
		}
		return jRes;
	}
}