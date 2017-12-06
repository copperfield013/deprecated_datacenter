package cn.sowell.datacenter.admin.controller.field;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import org.apache.log4j.Logger;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.dto.FieldDataDto;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/admin/field")
public class fieldController {

    Logger logger = Logger.getLogger(fieldController.class);

    @Resource
    BasePeopleService basePeopleService;

    @RequestMapping("/list")
    public String list(  PageInfo pageInfo,  Model model){
         List<FieldDataDto> list = basePeopleService.queryFieldList(pageInfo);
         model.addAttribute("list", list);
         model.addAttribute("pageInfo", pageInfo);
//       model.addAttribute("criteria", criteria);
        return AdminConstants.JSP_FIELD + "/field_list.jsp";
    }

    @RequestMapping("/add")
    public String add( ){
        return AdminConstants.JSP_FIELD + "/field_add.jsp";
    }




    @ResponseBody
    @RequestMapping("/do_add")
    public AjaxPageResponse doAdd(FieldDataDto field){
        try {
            basePeopleService.addField(field);
            return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "field_list");
        } catch (Exception e) {
            logger.error("添加失败", e);
            return AjaxPageResponse.FAILD("添加失败");
        }
    }



    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model){
        FieldDataDto fieldDataDto = basePeopleService.queryFieldById(id);
        model.addAttribute("fieldDataDto", fieldDataDto);
        return AdminConstants.JSP_FIELD + "/field_detail.jsp";
    }


    @ResponseBody
    @RequestMapping("/do_update")
    public AjaxPageResponse doUpdate(FieldDataDto field){
        try {
            basePeopleService.addField(field);
            return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("更新成功", "field_list");
        } catch (Exception e) {
            logger.error("更新失败", e);
            return AjaxPageResponse.FAILD("更新失败");
        }
    }


}
