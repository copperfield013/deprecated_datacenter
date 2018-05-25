package cn.sowell.datacenter.admin.controller.modules;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.spring.file.FileUtils;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.entityResolver.ImportCompositeField;
import cn.sowell.datacenter.entityResolver.config.ModuleMeta;
import cn.sowell.datacenter.entityResolver.impl.RelationEntityProxy;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.pojo.ImportStatus;
import cn.sowell.datacenter.model.modules.pojo.ImportTemplateCriteria;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplate;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplateField;
import cn.sowell.datacenter.model.modules.service.ModulesImportService;
import cn.sowell.datacenter.model.modules.service.ModulesService;

@Controller
@RequestMapping(AdminConstants.URI_MODULES + "/import")
public class AdminModulesImportController {
	private static final String SESSION_KEY_FIELD_NAMES = "field_names_";

	@Resource
	ModulesImportService impService;
	
	@Resource
	ModulesService mService;
	
	@Resource
	FileUtils fileUtils;
	
	Logger logger = Logger.getLogger(AdminModulesImportController.class);
	
	
	@RequestMapping("/go/{module}")
	public String goImport(@PathVariable String module, Model model) {
		ModuleMeta mMeta = mService.getModule(module);
		model.addAttribute("module", mMeta);
		model.addAttribute("composites", impService.getImportCompositeMap(module).values());
		return AdminConstants.JSP_MODULES + "/modules_import_tmpl.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/resolve_file")
	public ResponseJSON resolveFile(MultipartFile file) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		String fileName = file.getOriginalFilename();
		String suffix = null;
    	Workbook wk = null;
    	try {
    		if(fileName.endsWith(".xls")){
    			wk = new HSSFWorkbook(file.getInputStream());
    			suffix = ".xls";
    		}if(fileName.endsWith("xlsx")){
    			wk = new XSSFWorkbook(file.getInputStream());
    			suffix = ".xlsx";
    		}else{
    			jRes.put("msg", "文件格式错误，只支持xls和xlsx格式的文件。");
    		}
    	} catch (IOException e1) {
    		jRes.put("msg", "读取文件时发生错误");
    	}
    	if(wk != null){
    		int sheetCount = wk.getNumberOfSheets();
    		LinkedHashSet<String> names = new LinkedHashSet<>();
    		for (int i = 0; i < sheetCount; i++) {
				names.add(wk.getSheetName(i));
			}
    		jRes.put("names", names);
    		String uuid = TextUtils.uuid();
			try {
				fileUtils.saveFile(uuid + suffix, file.getInputStream());
				jRes.put("fileName", uuid + suffix);
				jRes.setStatus("suc");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return jRes;
	}
	
	@ResponseBody
    @RequestMapping("/do/{module}")
	public ResponseJSON doImport(
			MultipartFile file,
			@PathVariable String module,
			@RequestParam String dataType, 
			HttpSession session) {
		JSONObjectResponse jRes = new JSONObjectResponse();
        jRes.setStatus("error");
        ModuleMeta mData = mService.getModule(module);
        if(mData != null) {
        	String uuid = TextUtils.uuid();
        	jRes.put("uuid", uuid);
        	Workbook wk = null;
        	try {
        		String fileName = file.getOriginalFilename();
				if(fileName.endsWith(".xls")){
					wk = new HSSFWorkbook(file.getInputStream());
        		}if(fileName.endsWith("xlsx")){
        			wk = new XSSFWorkbook(file.getInputStream());
        		}else{
        			jRes.put("msg", "文件格式错误，只支持xls和xlsx格式的文件。");
        		}
        	} catch (IOException e1) {
        		jRes.put("msg", "读取文件时发生错误");
        	}
        	if(wk != null){
        		Sheet sheet = wk.getSheetAt(0);
        		if(sheet != null){
        			final Workbook workbook = wk;
        			ImportStatus importStatus = new ImportStatus();
        			session.setAttribute(AdminConstants.KEY_IMPORT_STATUS + uuid, importStatus);
        			Thread thread = new Thread(()->{
        				try {
        					impService.importData(sheet, importStatus, module, dataType);
        				} catch (ImportBreakException e) {
        					logger.error("导入被用户停止", e);
        				} catch (Exception e) {
        					logger.error("导入时发生未知异常", e);
        				}finally{
        					try {
        						workbook.close();
        					} catch (Exception e) {
        						logger.error("关闭workbook时发生错误", e);
        					}
        				}
        			});
        			thread.start();
        			jRes.setStatus("suc");
        			jRes.put("msg", "开始导入");
        		}else{
        			jRes.put("msg", "Excel文件内不存在表格");
        		}
        	}
        }else {
        	jRes.put("msg", "模块名不正确");
        }
        return jRes;
	}
	
	@ResponseBody
    @RequestMapping("/status")
    public JSONObjectResponse statusOfImport(HttpSession session, @RequestParam String uuid){
    	JSONObjectResponse jRes = new JSONObjectResponse();
        ImportStatus importStatus = (ImportStatus) session.getAttribute(AdminConstants.KEY_IMPORT_STATUS + uuid);
        if(importStatus != null){
            jRes.put("totalCount", importStatus.getTotal());
            jRes.put("current", importStatus.getCurrent());
            jRes.put("message", importStatus.getCurrentMessage());
            jRes.put("lastInterval", importStatus.lastInterval());
            if(importStatus.breaked()){
                jRes.put("breaked", true);
                session.removeAttribute(AdminConstants.KEY_IMPORT_STATUS + uuid);
            }else if(importStatus.ended()){
                jRes.put("ended", true);
                session.removeAttribute(AdminConstants.KEY_IMPORT_STATUS + uuid);
            }
            jRes.setStatus("suc");
        }else{
            jRes.setStatus("no found import progress");
        }
        return jRes;
    }
	
	@ResponseBody
    @RequestMapping("/break")
    public JSONObjectResponse breakImport(HttpSession session, @RequestParam String uuid){
        JSONObjectResponse jRes = new JSONObjectResponse();
        ImportStatus importStatus = (ImportStatus) session.getAttribute(AdminConstants.KEY_IMPORT_STATUS + uuid);
        if(importStatus != null){
            importStatus.breakImport();
            jRes.setStatus("suc");
        }
        return jRes;
    }
	
	@RequestMapping("/tmpl/{module}/{composite}")
	public String tmpl(
			@PathVariable String module,
			@PathVariable String composite,
			Model model
			) {
		UserIdentifier user = UserUtils.getCurrentUser();
		ModuleMeta mMeta = mService.getModule(module);
		Set<ImportCompositeField> fields = impService.getImportCompositeFields(module, composite);
		ImportTemplateCriteria criteria = new ImportTemplateCriteria();
		criteria.setModule(module);
		criteria.setUserId((Long) user.getId());
		criteria.setComposite(composite);
		List<ModuleImportTemplate> tmpls = impService.getImportTemplates(criteria);
		model.addAttribute("tmpls", tmpls);
		model.addAttribute("fields", fields);
		model.addAttribute("module", mMeta);
		model.addAttribute("compositeName", composite);
		model.addAttribute("relationLabelKey", RelationEntityProxy.LABEL_KEY);
		
		return AdminConstants.JSP_MODULES + "/modules_import_download.jsp";
	}
	
	@RequestMapping("/tmpl/show/{tmplId}")
	public String showTemplate(@PathVariable Long tmplId, Model model) {
		ModuleImportTemplate tmpl = impService.getImportTempalte(tmplId);
		model.addAttribute("tmpl", tmpl);
		model.addAttribute("tmplFieldsJson", JSON.toJSON(tmpl.getFields()));
		return tmpl(tmpl.getModule(), tmpl.getComposite(), model);
	}
	
	@ResponseBody
	@RequestMapping("/submit_field_names")
	public ResponseJSON submitFieldNames(@RequestBody JsonRequest jReq, HttpSession session) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		JSONObject reqJson = jReq.getJsonObject();
		ModuleImportTemplate tmpl = toImportTemplate(reqJson);
		if(tmpl != null) {
			String uuid = TextUtils.uuid();
			session.setAttribute(SESSION_KEY_FIELD_NAMES + uuid, tmpl);
			jRes.put("uuid", uuid);
		}
		return jRes;
	}
	
	@RequestMapping("/download_tmpl/{uuid}")
	public ResponseEntity<byte[]> download(@PathVariable String uuid, HttpSession session){
		ModuleImportTemplate tmpl = (ModuleImportTemplate) session.getAttribute(SESSION_KEY_FIELD_NAMES + uuid);
		if(tmpl != null) {
			try {
				byte[] tmplInputStream = impService.createImportTempalteBytes(tmpl);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentDispositionFormData("attachment", new String(
						(tmpl.getTitle() + ".xlsx").getBytes("UTF-8"),
						"iso-8859-1"));
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<byte[]>(tmplInputStream, headers, HttpStatus.CREATED);
			} catch (Exception e) {
				logger.error("下载导出文件时发生错误", e);
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/save_tmpl")
	public ResponseJSON saveTmpl(@RequestBody JsonRequest jReq) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		JSONObject reqJson = jReq.getJsonObject();
		try {
			ModuleImportTemplate tmpl = toImportTemplate(reqJson);
			Long tmplId = impService.saveTemplate(tmpl);
			jRes.put("tmplId", tmplId);
			jRes.put("tmplTitle", tmpl.getTitle());
			jRes.setStatus("suc");
		} catch (Exception e) {
			logger.error("保存导入模板时发生错误", e);
			jRes.setStatus("error");
		}
		return jRes;
	}

	private ModuleImportTemplate toImportTemplate(JSONObject reqJson) {
		String composite = reqJson.getString("composite"),
				module = reqJson.getString("module");
		Assert.hasText(composite);
		Assert.hasText(module);
		
		JSONArray fieldArray = reqJson.getJSONArray("fields");
		if(fieldArray != null && !fieldArray.isEmpty()) {
			UserIdentifier user = UserUtils.getCurrentUser();
			Set<ModuleImportTemplateField> fields = new LinkedHashSet<ModuleImportTemplateField>();
			CollectionUtils.appendTo(fieldArray, fields, item->{
				JSONObject fieldItem = (JSONObject) item; 
				ModuleImportTemplateField field = new ModuleImportTemplateField();
				field.setId(fieldItem.getLong("id"));
				field.setFieldName(fieldItem.getString("fieldName"));
				field.setTitle(fieldItem.getString("title"));
				field.setFieldPattern(fieldItem.getString("fieldPattern"));
				field.setFieldIndex(fieldItem.getInteger("fieldIndex"));
				return field;
			});
			int order = 1;
			for (ModuleImportTemplateField field : fields) {
				field.setOrder(order++);
			}
			String title = reqJson.getString("title");
			if(!TextUtils.hasText(title)) {
				title = "导入模板";
			}
			ModuleImportTemplate importTmpl = new ModuleImportTemplate();
			importTmpl.setTitle(title);
			importTmpl.setFields(fields);
			importTmpl.setComposite(composite);
			importTmpl.setModule(module);
			importTmpl.setCreateUserId((Long) user.getId());
			Long tmplId = reqJson.getLong("tmplId");
			if(tmplId != null) {
				importTmpl.setId(tmplId);
			}
			return importTmpl;
		}
		return null;
	}
	
	
	
	
}
