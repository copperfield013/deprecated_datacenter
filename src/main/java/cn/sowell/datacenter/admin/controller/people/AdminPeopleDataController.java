package cn.sowell.datacenter.admin.controller.people;

import java.io.IOException;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.sun.javafx.logging.PulseLogger;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.abc.dto.ErrorInfomation;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JsonResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.peopledata.status.ImportStatus;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/peopledata")
public class AdminPeopleDataController {
	

	@Resource
	ABCExecuteService abcService;

	@Resource
	PeopleDataService peopleService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	Logger logger = Logger.getLogger(AdminPeopleDataController.class);
	@org.springframework.web.bind.annotation.InitBinder
    public void InitBinder(ServletRequestDataBinder binder) {
        System.out.println("执行了InitBinder方法");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
    } 
	
	@RequestMapping("/list")
	public String list(PeopleDataCriteria criteria, PageInfo pageInfo, Model model){
		List<PeopleData> list = peopleService.query(criteria, pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_PEOPLEDATA + "/peopledata_list.jsp";
	}
	
	@RequestMapping("/add")
	public String add(){
		return AdminConstants.JSP_PEOPLEDATA + "/peopledata_add.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/do_add")
	public AjaxPageResponse doAdd(WebRequest request, PeopleData people){
		try {
			abcService.savePeople(people);
			/*Map<String, String> data = new HashMap<String, String>();
			request.getParameterMap().forEach((name, val) -> {data.put(name, val[0]);});
			abcService.mergePeople(data);*/
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "peopledata_list");
		} catch (Exception e) {
			logger.error("添加失败", e);
			return AjaxPageResponse.FAILD("添加失败");
		}
	}
	
	@RequestMapping("/update/{peopleCode}")
	public String update(@PathVariable String peopleCode, Model model){
		PeopleData people = peopleService.getPeople(peopleCode);
		model.addAttribute("people", people);
		return AdminConstants.JSP_PEOPLEDATA + "/peopledata_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/do_update")
	public AjaxPageResponse doUpdate(PeopleData people){
		try {
			Assert.notNull(people.getPeopleCode());
			abcService.savePeople(people);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功", "peopledata_list");
		} catch (Exception e) {
			logger.error("添加失败", e);
			return AjaxPageResponse.FAILD("修改失败");
		}
	}


	
	
	@RequestMapping("/import")
	public String peopleImport(){
		return AdminConstants.JSP_PEOPLEDATA + "/peopledata_import.jsp";
	}
	
	private static final String KEY_IMPORT_STATUS = "importStatus";
	
	@ResponseBody
	@RequestMapping("/do_import")
	public JsonResponse doImport(MultipartFile file, @RequestParam String sheetName, @RequestParam String dataType, HttpSession session){
		JsonResponse jRes = new JsonResponse();
		jRes.setStatus("error");
		String uuid = TextUtils.uuid();
		jRes.put("uuid", uuid);
		ImportStatus beforeStatus = (ImportStatus) session.getAttribute(KEY_IMPORT_STATUS + uuid);
		if(beforeStatus != null){
			jRes.put("msg", "导入仍在进行");
		}else{
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
					session.setAttribute(KEY_IMPORT_STATUS + uuid, importStatus);
					Thread thread = new Thread(()->{
						try {
							abcService.importPeople(sheet, importStatus, dataType);
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
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/status_of_import")
	public JsonResponse statusOfImport(HttpSession session, @RequestParam String uuid){
		JsonResponse jRes = new JsonResponse();
		ImportStatus importStatus = (ImportStatus) session.getAttribute(KEY_IMPORT_STATUS + uuid);
		if(importStatus != null){
			jRes.put("totalCount", importStatus.getTotal());
			jRes.put("current", importStatus.getCurrent());
			jRes.put("message", importStatus.getCurrentMessage());
			jRes.put("lastInterval", importStatus.lastInterval());
			if(importStatus.breaked()){
				jRes.put("breaked", true);
				session.removeAttribute(KEY_IMPORT_STATUS + uuid);
			}else if(importStatus.ended()){
				jRes.put("ended", true);
				session.removeAttribute(KEY_IMPORT_STATUS + uuid);
			}
			jRes.setStatus("suc");
		}else{
			jRes.setStatus("no found import progress");
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/break_import")
	public JsonResponse breakImport(HttpSession session, @RequestParam String uuid){
		JsonResponse jRes = new JsonResponse();
		ImportStatus importStatus = (ImportStatus) session.getAttribute(KEY_IMPORT_STATUS + uuid);
		if(importStatus != null){
			importStatus.breakImport();
			jRes.setStatus("suc");
		}
		return jRes;
	}
	
	
	
	@RequestMapping("/detail/{peopleCode}")
	public String detail(@PathVariable String peopleCode, String datetime, Model model){
		Date date = null;
		date = dateFormat.parse(datetime);
		PeopleData people = peopleService.getHistoryPeople(peopleCode, date);
		//PeopleData people = peopleService.getPeople(peopleCode);
		model.addAttribute("people", people);
		model.addAttribute("datetime", datetime);
		model.addAttribute("peopleCode", peopleCode);
		return AdminConstants.JSP_PEOPLEDATA + "/peopledata_detail.jsp";
	}
	
	@RequestMapping("/history/{peopleCode}")
	public String history(@PathVariable String peopleCode){
		return AdminConstants.JSP_PEOPLEDATA + "/peopledata_history.jsp";
	}
	
	
	@ResponseBody
	@RequestMapping("/do_delete/{peopleCode}")
	public AjaxPageResponse delete(@PathVariable String peopleCode){
		try {
			abcService.deletePeople(peopleCode);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
}
