package cn.sowell.datacenter.admin.controller.basepeople;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.BasePeopleDictionaryCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeopleItem;
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
    public String update(@PathVariable Long id,Model model){

        model.addAttribute("dictionaryDataDto",   basePeopleService.getDicById(id));
        return AdminConstants.JSP_BASEPEOPLE + "/dictionary_detail.jsp";
    }


    @ResponseBody
    @RequestMapping("/save")
    public AjaxPageResponse dosave(TBasePeopleDictionaryEntity tBasePeopleDictionaryEntity){
        try {
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
    public AjaxPageResponse doDelte(@PathVariable Long id){
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



    @ResponseBody
    @RequestMapping("/do_enumdelete/{id}")
    public AjaxPageResponse do_enumdelete(@PathVariable Long id){
        try {
            BasePeopleItem basePeopleItem = new BasePeopleItem();
            basePeopleItem.setcId(id);
            basePeopleService.deleteObj(basePeopleItem);
            return AjaxPageResponse.REFRESH_LOCAL("删除成功");
        } catch (Exception e) {
            logger.error("删除失败", e);
            return AjaxPageResponse.FAILD("删除失败");
        }
    }

    @RequestMapping("/addEnum/{dicid}")
    public String addEnum(@PathVariable String dicid,Model model){
        BasePeopleItem basePeopleItem = new BasePeopleItem();
        basePeopleItem.setcDictionaryId(dicid);
        model.addAttribute("ItemDto", basePeopleItem);
        return AdminConstants.JSP_BASEPEOPLE + "/item_detail.jsp";
    }


    @RequestMapping("/EditEnum/{dicid}")
    public String EditEnum(@PathVariable String dicid,Model model){
        BasePeopleItem basePeopleItem = new BasePeopleItem();
        basePeopleItem.setcDictionaryId(dicid);
        model.addAttribute("ItemDto", basePeopleItem);
        return AdminConstants.JSP_BASEPEOPLE + "/item_detail.jsp";
    }

    @ResponseBody
    @RequestMapping("/SaveEnum")
    public AjaxPageResponse EditEnum( BasePeopleItem basePeopleItem) {
        try {
            basePeopleService.saveOrUpdate(basePeopleItem);
            return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("保存成功", "item_list_"+basePeopleItem.getcDictionaryId());
        } catch (Exception e) {

            logger.error("保存失败", e);
            return AjaxPageResponse.FAILD("保存失败");
        }


    }

    @RequestMapping("/UpdateEnum/{id}")
    public String UpdateEnum(@PathVariable Long id,Model model){
        model.addAttribute("ItemDto", basePeopleService.getEnumById(id));
        return AdminConstants.JSP_BASEPEOPLE + "/item_detail.jsp";
    }

}