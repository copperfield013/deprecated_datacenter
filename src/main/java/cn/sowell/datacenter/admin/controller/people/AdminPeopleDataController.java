package cn.sowell.datacenter.admin.controller.people;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JsonResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.people.pojo.PeopleData;
import cn.sowell.datacenter.model.people.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.people.service.PeopleService;
import cn.sowell.datacenter.model.people.status.ImportStatus;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/peopledata")
public class AdminPeopleDataController {
	

	@Resource
	ABCExecuteService abcService;

	@Resource
	PeopleService peopleService;
	
	Logger logger = Logger.getLogger(AdminPeopleDataController.class);
	
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
	public AjaxPageResponse doAdd(WebRequest request){
		try {
			Map<String, String> data = new HashMap<String, String>();
			request.getParameterMap().forEach((name, val) -> {
				data.put(name, val[0]);
			});
			abcService.mergePeople(data);
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
	public AjaxPageResponse doUpdate(WebRequest request){
		try {
			Map<String, String> data = new HashMap<String, String>();
			request.getParameterMap().forEach((name, val) -> {
				data.put(name, val[0]);
			});
			Assert.notNull(data.get("peopleCode"));
			abcService.mergePeople(data);
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
	public JsonResponse doImport(MultipartFile file, String sheetName, HttpSession session){
		JsonResponse jRes = new JsonResponse();
		jRes.setStatus("error");
		if(session.getAttribute(KEY_IMPORT_STATUS) != null){
			jRes.put("msg", "尚有导入任务没有完成。");
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
					session.setAttribute(KEY_IMPORT_STATUS, importStatus);
					Thread thread = new Thread(()->{
						try {
							abcService.importPeople(sheet, importStatus);
						} catch (Exception e) {
							logger.error("", e);
						}finally{
							try {
								workbook.close();
								session.removeAttribute(KEY_IMPORT_STATUS);
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
	public JsonResponse statusOfImport(HttpSession session){
		JsonResponse jRes = new JsonResponse();
		ImportStatus importStatus = (ImportStatus) session.getAttribute(KEY_IMPORT_STATUS);
		if(importStatus != null){
			System.out.println("AAAAAAA\n\n\n\n\n\n" + importStatus.getCurrentMessage());
			jRes.put("totalCount", importStatus.getTotal());
			jRes.put("current", importStatus.getCurrent());
			jRes.put("message", importStatus.getCurrentMessage());
		}else{
			jRes.setStatus("no found import progress");
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/break_import")
	public JsonResponse breakImport(HttpSession session){
		JsonResponse jRes = new JsonResponse();
		ImportStatus importStatus = (ImportStatus) session.getAttribute(KEY_IMPORT_STATUS);
		if(importStatus != null){
			importStatus.breakImport();
			jRes.setStatus("suc");
		}
		return jRes;
	}
	
	
	
	@RequestMapping("/detail/{peopleCode}")
	public String detail(@PathVariable String peopleCode, Model model){
		PeopleData people = peopleService.getPeople(peopleCode);
		model.addAttribute("people", people);
		return AdminConstants.JSP_PEOPLEDATA + "/peopledata_detail.jsp";
	}
	
	@RequestMapping("/do_delete/{peopleCode}")
	public AjaxPageResponse delete(@PathVariable String peopleCode){
		try {
			abcService.deletePeople(peopleCode);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
}
