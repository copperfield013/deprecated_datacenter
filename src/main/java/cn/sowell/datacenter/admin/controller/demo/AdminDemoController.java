package cn.sowell.datacenter.admin.controller.demo;

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
import cn.sowell.datacenter.model.demo.criteria.DemoCriteria;
import cn.sowell.datacenter.model.demo.pojo.PlainDemo;
import cn.sowell.datacenter.model.demo.service.DemoService;

@Controller
@RequestMapping("/admin/demo")
public class AdminDemoController {
	
	@Resource
	DemoService demoService;
	
	Logger logger = Logger.getLogger(AdminDemoController.class);
	
	@RequestMapping("/list")
	public String list(DemoCriteria criteria, Model model, PageInfo pageInfo){
		List<PlainDemo> list = demoService.queryList(criteria, pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_DEMO + "/demo_list.jsp";
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable Long id, Model model){
		PlainDemo demo = demoService.getDemo(id);
		model.addAttribute("demo", demo);
		return AdminConstants.JSP_DEMO + "/demo_detail.jsp";
	}
	
	@RequestMapping("/add")
	public String add(PlainDemo demo){
		return AdminConstants.JSP_DEMO + "/demo_add.jsp";
	}
	
	
	@ResponseBody
	@RequestMapping("/do_add")
	public AjaxPageResponse doAdd(PlainDemo demo){
		try {
			demoService.create(demo);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "demo_list");
		} catch (Exception e) {
			logger.error("添加失败", e);
			return AjaxPageResponse.FAILD("添加失败");
		}
	}

	@RequestMapping("/update/{id}")
	public String update(@PathVariable Long id, Model model){
		PlainDemo demo = demoService.getDemo(id);
		model.addAttribute("demo", demo);
		return AdminConstants.JSP_DEMO + "/demo_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/do_update")
	public AjaxPageResponse doUpdate(PlainDemo demo){
		try {
			demoService.update(demo);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功", "demo_list");
		} catch (Exception e) {
			logger.error("修改失败", e);
			return AjaxPageResponse.FAILD("修改失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/do_delete/{id}")
	public AjaxPageResponse doDelte(@PathVariable Long id){
		try {
			demoService.delete(id);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
}
