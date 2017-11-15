package cn.sowell.datacenter.admin.controller.basepeople;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.BasePeopleDictionaryCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/peopleDictionary")
public class PeopleDateDcitionaryController {

    @Resource
    BasePeopleService basePeopleService;

    private TransportClient client;

    Logger logger = Logger.getLogger(PeopleDateDcitionaryController.class);

    @org.springframework.web.bind.annotation.InitBinder
    public void InitBinder(ServletRequestDataBinder binder) {
        //System.out.println("执行了InitBinder方法");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping("/add")
    public String add(Model model){
        TBasePeopleDictionaryEntity  tBasePeopleDictionaryEntity = new TBasePeopleDictionaryEntity();
        model.addAttribute("dictionaryDataDto", tBasePeopleDictionaryEntity);
        return AdminConstants.JSP_BASEPEOPLE + "/dictionary_detail.jsp";
    }


    @RequestMapping("/update/{id}")
    public String update(@PathVariable String id,Model model){
        basePeopleService.getDicById(id);
        model.addAttribute("dictionaryDataDto",   basePeopleService.getDicById(id));
        return AdminConstants.JSP_BASEPEOPLE + "/dictionary_detail.jsp";
    }


    @ResponseBody
    @RequestMapping("/save")
    public AjaxPageResponse dosave(TBasePeopleDictionaryEntity tBasePeopleDictionaryEntity){
        try {
            if(TextUtils.hasText(tBasePeopleDictionaryEntity.getcId())) {
            }else{
                tBasePeopleDictionaryEntity.setcId(null);
            }
            basePeopleService.saveOrUpdate(tBasePeopleDictionaryEntity);
            return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("保存成功", "dictionary_list");
        } catch (Exception e) {
            logger.error("保存失败", e);
            return AjaxPageResponse.FAILD("保存失败");
        }
    }



    @RequestMapping("/list")
    public String diclist(BasePeopleDictionaryCriteria criteria, Model model, PageInfo pageInfo){
        List<TBasePeopleDictionaryEntity> list = basePeopleService.querydicList(criteria, pageInfo);
        model.addAttribute("list", list);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("criteria", criteria);
        return AdminConstants.JSP_BASEPEOPLE + "/dictionary_list.jsp";
    }


    @ResponseBody
    @RequestMapping("/do_delete/{id}")
    public AjaxPageResponse doDelte(@PathVariable String id){
        try {
            TBasePeopleDictionaryEntity tBasePeopleDictionaryEntity = new TBasePeopleDictionaryEntity();
            tBasePeopleDictionaryEntity.setcId(id);
            basePeopleService.deleteObj(tBasePeopleDictionaryEntity);
            return AjaxPageResponse.REFRESH_LOCAL("删除成功");
        } catch (Exception e) {
            logger.error("删除失败", e);
            return AjaxPageResponse.FAILD("删除失败");
        }
    }

}