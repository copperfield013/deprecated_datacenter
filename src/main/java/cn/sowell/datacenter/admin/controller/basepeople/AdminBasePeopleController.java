package cn.sowell.datacenter.admin.controller.basepeople;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.admin.controller.demo.AdminDemoController;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.People;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;


@Controller
@RequestMapping("/admin/people")
public class AdminBasePeopleController {
	
	@Resource
	BasePeopleService basePeopleService;
	
	Logger logger = Logger.getLogger(AdminBasePeopleController.class);
	
	@RequestMapping("/list")
	public String list(BasePeopleCriteria criteria, Model model, PageInfo pageInfo){
		List<People> list = basePeopleService.queryList(criteria, pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_PEOPLE + "/people_list.jsp";
	}
	
	@RequestMapping("/add")
	public String add(People p){
		return AdminConstants.JSP_PEOPLE + "/people_add.jsp";
	}
	
	@RequestMapping("/do_add")
	public String doAdd(People p){
		return AdminConstants.JSP_PEOPLE + "/people_add.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/do_delete/{id}")
	public AjaxPageResponse doDelte(@PathVariable Long id){
		try {
			basePeopleService.delete(id);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
}
