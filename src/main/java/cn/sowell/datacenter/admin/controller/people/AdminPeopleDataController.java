package cn.sowell.datacenter.admin.controller.people;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.sowell.datacenter.model.basepeople.pojo.BasePeopleDicInfomation;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeopleItem;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleInformationEntity;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;
import cn.sowell.datacenter.model.peopledata.service.PojoService;
import cn.sowell.datacenter.model.peopledata.service.impl.PropertyParser;
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

import cn.sowell.copframe.common.property.PropertyPlaceholder;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JsonResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.ExcelModelCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.ExcelModel;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.criteria.PeopleDataCriteria;
import cn.sowell.datacenter.model.peopledata.service.PeopleButtService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.peopledata.status.ImportStatus;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/peopledata")
public class AdminPeopleDataController {


    @Resource
    ABCExecuteService abcService;

    @Resource
    PeopleDataService peopleService;

    @Resource
    FrameDateFormat dateFormat;

    @Resource
    PeopleButtService buttService;

    @Resource
    PojoService pojoService;

    @Resource
    BasePeopleService basePeopleService;




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
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_update_new.jsp";
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


    @RequestMapping("/smart/{peopleCode}")
    public String smart(@PathVariable String peopleCode, Model model){

        PeopleData people = peopleService.getPeople(peopleCode);
        PropertyParser parser = pojoService.createPropertyParser(people);
        List<TBasePeopleDictionaryEntity> list = basePeopleService.dicListByUser();

        List<BasePeopleItem> itemList  =basePeopleService.dicItemByUser();
        model.addAttribute("peopleMap", parser);
        model.addAttribute("dic", list);
        model.addAttribute("itemList", itemList);
        model.addAttribute("peopleCode", peopleCode);
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_smart.jsp";
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

    @ResponseBody
    @RequestMapping("/smart_search")
    public JsonResponse smartsearch(String peopleCode, String type, String field, HttpServletResponse response) {
        JsonResponse jRes = new JsonResponse();
        try {
            PeopleData people = peopleService.getPeople(peopleCode);
            jRes.put("data", JSON.toJSON(people));
            jRes.put("type", type);
            jRes.setStatus("success");
            if(Arrays.asList(AdminConstants.FRELD).contains(type)){
                jRes.put("fieldList", buttService.fieldList(field));
            }
        } catch (Exception e){
            jRes.setStatus("faild");
        }
        return jRes;
    }

    @ResponseBody
    @RequestMapping("/do_smart_update")
    public AjaxPageResponse doUpdate(String peopleCode, @RequestParam Map<String,String> map){
        try {
            buttService.updatePeople(peopleCode, map);
            return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功", "peopledata_list");
        } catch (Exception e) {
            logger.error(e);
            return AjaxPageResponse.FAILD("修改失败");
        }
    }



    @ResponseBody
    @RequestMapping(value="titleSearch")
    public JsonResponse esearch(String txt) {
        JsonResponse jsonResponses = new JsonResponse();
        try{
            jsonResponses.put("data",buttService.titleSearch(txt.trim()));
            return jsonResponses;
        }
        catch(Exception e) {
            e.printStackTrace();
            return jsonResponses;
        }

    }

    @RequestMapping("/output")
    public String output(ExcelModelCriteria criteria, PageInfo pageInfo, Model model){
        List<ExcelModel> list = buttService.queryModel(criteria,pageInfo,"0");
        model.addAttribute("list", list);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("criteria", criteria);
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_output.jsp";
    }

    @RequestMapping("/outputAdd/{modelId}")
    public String outputAdd(@PathVariable Long modelId, Model model){
        ExcelModel excelModel = buttService.getExcelModel(modelId);
        List<TBasePeopleDictionaryEntity> list = new ArrayList<TBasePeopleDictionaryEntity>();
        List<TBasePeopleInformationEntity> infoList = buttService.getDicInfo();
        if(modelId !=0){
            list = buttService.getDicByModelId(modelId);
        }
        model.addAttribute("id", modelId);
        model.addAttribute("model", excelModel);
        model.addAttribute("list", list);
        model.addAttribute("infolist", infoList);
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_output_add.jsp";
    }

    @RequestMapping("/outputDetail/{modelId}")
    public String outputDetail(@PathVariable Long modelId, Model model){
        ExcelModel excelModel = buttService.getExcelModel(modelId);
        List<TBasePeopleDictionaryEntity> list = buttService.getDicByModelId(modelId);
        model.addAttribute("id", modelId);
        model.addAttribute("model", excelModel);
        model.addAttribute("list", list);
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_output_detail.jsp";
    }

    @ResponseBody
    @RequestMapping("/do_outputAdd")
    public AjaxPageResponse do_outputAdd(ExcelModel model, @RequestParam String[] list){
        try {
            if(model.getId()==null){
                buttService.addExcelList(model,list);
                return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("生成模板成功", "people_output");
            }
            else{
                buttService.updateExcelList(model,list);
                return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改模板成功", "people_output");
            }
        } catch (Exception e) {
            logger.error(e);
            return AjaxPageResponse.FAILD("生成模板失败");
        }
    }

    @ResponseBody
    @RequestMapping("/output_delete/{modelId}")
    public AjaxPageResponse outputDelete(@PathVariable Long modelId){
        try {
            buttService.deleteModel(modelId);
            return AjaxPageResponse.REFRESH_LOCAL("删除成功");
        } catch (Exception e) {
            logger.error("删除失败", e);
            return AjaxPageResponse.FAILD("删除失败");
        }
    }

    @ResponseBody
    @RequestMapping("/do_download/{modelId}")
    public JsonResponse downloadExcel(HttpServletRequest request,HttpServletResponse response,
                                      @PathVariable Long modelId,PeopleDataCriteria criteria, PageInfo pageInfo) throws IOException{
        String fileName="excel文件";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            String path =request.getSession().getServletContext().getRealPath(PropertyPlaceholder.getProperty("excel_model_path"));
            List<TBasePeopleDictionaryEntity> keys = buttService.getColumnNames(modelId);//map中的key
            List<String[]> columnLists = buttService.columnLists(keys);
            List<Map<String, Object>> listmap = peopleService.queryMap(criteria, pageInfo,keys);
            ExcelModel model = buttService.getExcelModel(modelId);
            abcService.downloadPeople(listmap,keys,columnLists,model,path).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        os.close();
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
            if (out != null)
                out.close();
        }
        return null;
    }


    /***
     *
     * @param criteria
     * @param pageInfo
     * @param model
     * @return
     * 字段模版展示页
     */

    @RequestMapping("/model")
    public String model(ExcelModelCriteria criteria, PageInfo pageInfo, Model model) {
        List<ExcelModel> list = buttService.queryModel(criteria, pageInfo, "1");
        model.addAttribute("list", list);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("criteria", criteria);
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_model.jsp";
    }


    @RequestMapping("/modelAdd/{modelId}")
    public String modelAdd(@PathVariable Long modelId, Model model){
        ExcelModel excelModel = buttService.getExcelModel(modelId);
        List<TBasePeopleDictionaryEntity> list = new ArrayList<TBasePeopleDictionaryEntity>();
        List<TBasePeopleInformationEntity> infoList = buttService.getDicInfo();
        if(modelId !=0){
            list = buttService.getDicByModelId(modelId);
        }
        model.addAttribute("id", modelId);
        model.addAttribute("model", excelModel);
        model.addAttribute("list", list);
        model.addAttribute("infolist", infoList);
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_model_add.jsp";
    }


    @RequestMapping("/modelDetail/{modelId}")
    public String modelDetail(@PathVariable Long modelId, Model model){
        ExcelModel excelModel = buttService.getExcelModel(modelId);
        List<TBasePeopleDictionaryEntity> list = buttService.getDicByModelId(modelId);
        model.addAttribute("id", modelId);
        model.addAttribute("model", excelModel);
        model.addAttribute("list", list);
        return AdminConstants.JSP_PEOPLEDATA + "/peopledata_model_detail.jsp";
    }


}
