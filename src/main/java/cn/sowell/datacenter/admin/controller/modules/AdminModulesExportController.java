package cn.sowell.datacenter.admin.controller.modules;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.CommonPageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.modules.bean.ExportDataPageInfo;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_MODULES + "/export")
public class AdminModulesExportController {
	@Resource
	TemplateService tService;
	
	@Resource
	ModulesService mService;
	
	@Resource
	ExportService eService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	Logger logger = Logger.getLogger(AdminModulesExportController.class);
	
	@ResponseBody
	@RequestMapping("/start/{module}")
	public ResponseJSON doImport(
			@RequestBody JsonRequest jReq, HttpSession session){
		JSONObjectResponse jRes = new JSONObjectResponse();
		JSONObject json = jReq.getJsonObject();
		String uuid = TextUtils.uuid();
		String scope = json.getString("scope");
		boolean isCurrentScope = "current".equals(scope),
				isAllScope = "all".equals(scope);
		if(isCurrentScope || isAllScope){
			JSONObject parameters = json.getJSONObject("parameters");
			Long tmplId = parameters.getLong("tmplId");
			if(tmplId != null){
				TemplateListTempalte ltmpl = tService.getListTemplate(tmplId);
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
					eService.startExport(uuid, ltmpl, new HashSet<NormalCriteria>(vCriteriaMap.values()), ePageInfo);
				}
			}
		}
		jRes.put("uuid", uuid);
		session.setAttribute(AdminConstants.EXPORT_PEOPLE_STATUS_UUID, uuid);
		return jRes;
	}
	

	@ResponseBody
	@RequestMapping("/status")
	public ResponseJSON statusOfExport(String uuid, Boolean interrupted){
		JSONObjectResponse jRes = new JSONObjectResponse();
		jRes.setStatus("error");
		jRes.put("uuid", uuid);
		ExportStatus exportStatus = eService.getExportStatus(uuid);
		if(exportStatus != null){
			exportStatus.check();
			if(Boolean.TRUE.equals(interrupted)){
				exportStatus.setBreaked();
				jRes.setStatus("breaked");
			}else{
				jRes.put("current", exportStatus.getCurrent());
				jRes.put("totalCount", exportStatus.getTotalCount());
				jRes.put("currentData", exportStatus.getCurrentData());
				jRes.put("totalData", exportStatus.getTotalData());
				jRes.put("completed", exportStatus.isCompleted());
				jRes.put("statusMsg", exportStatus.getMessage());
				jRes.setStatus("suc");
			}
		}else{
			jRes.put("statusMsg", "导出已超时，请重新导出");
		}
		return jRes;
	}
	
	@RequestMapping("/download/{uuid}")
	public ResponseEntity<byte[]> download(@PathVariable String uuid){
		AbstractResource resource = eService.getDownloadResource(uuid);
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
		return null;
	}
}
