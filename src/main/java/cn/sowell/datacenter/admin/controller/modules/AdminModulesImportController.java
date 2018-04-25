package cn.sowell.datacenter.admin.controller.modules;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.entityResolver.config.ModuleMeta;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.pojo.ImportStatus;
import cn.sowell.datacenter.model.modules.service.ModulesImportService;
import cn.sowell.datacenter.model.modules.service.ModulesService;

@Controller
@RequestMapping(AdminConstants.URI_MODULES + "/import")
public class AdminModulesImportController {
	@Resource
	ModulesImportService impService;
	
	@Resource
	ModulesService mService;
	
	Logger logger = Logger.getLogger(AdminModulesImportController.class);
	
	
	@RequestMapping("/go/{module}")
	public String goImport(@PathVariable String module, Model model) {
		ModuleMeta mMeta = mService.getModule(module);
		model.addAttribute("module", mMeta);
		model.addAttribute("composites", impService.getImportCompositeMap(module).values());
		return AdminConstants.JSP_MODULES + "/modules_import_tmpl.jsp";
	}
	
	@ResponseBody
    @RequestMapping("/do/{module}")
	public ResponseJSON doImport(
			MultipartFile file, 
			@PathVariable String module,
			@RequestParam String sheetName, 
			@RequestParam String dataType, 
			HttpSession session) {
		JSONObjectResponse jRes = new JSONObjectResponse();
        jRes.setStatus("error");
        ModuleMeta mData = mService.getModule(module);
        if(mData != null) {
        	String uuid = TextUtils.uuid();
        	jRes.put("uuid", uuid);
        	String fileName = file.getOriginalFilename();
        	Workbook wk = null;
        	try {
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
        		Sheet sheet = wk.getSheet(sheetName);
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
        			jRes.put("msg", "Excel文件内不存在名称为" + sheetName + "的表格");
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
}
