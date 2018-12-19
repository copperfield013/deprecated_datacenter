package cn.sowell.datacenter.api.controller.entity;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.PollStatusResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.spring.file.FileUtils;
import cn.sowell.copframe.web.poll.ProgressPollableThread;
import cn.sowell.copframe.web.poll.ProgressPollableThreadFactory;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.datacenter.admin.controller.modules.AdminModulesImportController;
import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.model.config.pojo.SideMenuLevel2Menu;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.datacenter.model.modules.exception.ImportBreakException;
import cn.sowell.datacenter.model.modules.service.ModulesImportService;
import cn.sowell.dataserver.model.modules.pojo.ModuleMeta;
import cn.sowell.dataserver.model.modules.service.ModulesService;

@Controller
@RequestMapping("/api/entity/import")
public class ApiEntityImportController {

	@Resource
	ModulesImportService impService;
	
	@Resource
	ModulesService mService;
	
	@Resource
	FileUtils fileUtils;
	
	@Resource
	AuthorityService authService;
	
	Logger logger = Logger.getLogger(AdminModulesImportController.class);
	
	
	ProgressPollableThreadFactory pFactory = new ProgressPollableThreadFactory() {
		{
			setThreadExecutor(new ThreadPoolExecutor(6, 10, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()));
		}
	};
	
	
	@ResponseBody
    @RequestMapping("/start/{menuId}")
	public ResponseJSON startImport(
			@RequestParam MultipartFile file,
			@PathVariable Long menuId, ApiUser user) {
		SideMenuLevel2Menu menu = authService.vaidateL2MenuAccessable(menuId);
		JSONObjectResponse jRes = new JSONObjectResponse();
        jRes.setStatus("error");
        ModuleMeta mData = mService.getModule(menu.getTemplateModule());
        if(mData != null) {
        	
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
        			WorkProgress progress = new WorkProgress();
                	jRes.put("uuid", progress.getUUID());
                	ProgressPollableThread thread = pFactory.createThread(progress, p->{
                		impService.importData(sheet, p, menu.getTemplateModule(), user);
                	}, (p,e)->{
                		if(e instanceof ImportBreakException) {
							logger.error("导入被用户停止", e);
						}else {
							logger.error("导入时发生未知异常", e);
						}
                	}, e->{
                		try {
    						workbook.close();
    					} catch (Exception e1) {
    						logger.error("关闭workbook时发生错误", e1);
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
    @RequestMapping("/status/{uuid}")
    public PollStatusResponse statusOfImport(HttpSession session,
    		@PathVariable String uuid, 
    		Boolean interrupted, 
    		Integer msgIndex, ApiUser user){
		PollStatusResponse status = new PollStatusResponse();
		WorkProgress progress = pFactory.getProgress(uuid);
        if(progress != null){
            if(progress.isCompleted()){
            	status.setCompleted();
            	pFactory.removeProgress(uuid);
            }else {
            	if(interrupted == true) {
            		pFactory.stopWork(uuid);
            	}
            	if(progress.isBreaked()) {
            		status.setBreaked();
            		pFactory.removeProgress(uuid);
            	}
            }
            status.setCurrent(progress.getCurrent());
        	status.setTotalCount(progress.getTotal());
        	if(msgIndex != null) {
        		status.setMessageSequeue(progress.getLogger().getMessagesFrom(msgIndex));
        	}
        	status.put("message", progress.getLastMessage());
        	status.put("lastInterval", progress.getLastItemInterval());
        	status.setUUID(uuid);
        	status.setSuccessStatus();
        }else{
        	status.setStatus("no found import progress");
        }
        return status;
    }
}
