package cn.sowell.datacenter.admin.controller.basepeople;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.sowell.datacenter.model.basepeople.dto.ResultDto;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;

import com.alibaba.fastjson.JSONArray;


@Controller
@RequestMapping("/admin/people")
public class AdminBasePeopleController {
	
	@Resource
	BasePeopleService basePeopleService;
	
	private TransportClient client;
	
	Logger logger = Logger.getLogger(AdminBasePeopleController.class);
	
	@RequestMapping("/list")
	public String list(BasePeopleCriteria criteria, Model model, PageInfo pageInfo){
		List<BasePeople> list = basePeopleService.queryList(criteria, pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_BASEPEOPLE + "/people_list.jsp";
	}
	
	@RequestMapping("/add")
	public String add(BasePeople p){
		return AdminConstants.JSP_BASEPEOPLE + "/people_add.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/do_add")
	public AjaxPageResponse doAdd(BasePeople p){
		try {
			basePeopleService.create(p);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功", "people_list");
		} catch (Exception e) {
			logger.error("添加失败", e);
			return AjaxPageResponse.FAILD("添加失败");
		}
	}
	
	
	@RequestMapping("/update/{id}")
	public String update(@PathVariable Long id, Model model){
		BasePeople people = basePeopleService.getPeople(id);
		model.addAttribute("people", people);
		return AdminConstants.JSP_BASEPEOPLE + "/people_update.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/do_update")
	public AjaxPageResponse doUpdate(BasePeople people){
		try {
			basePeopleService.update(people);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功", "people_list");
			//return AjaxPageResponse.REFRESH_LOCAL("123");
		} catch (Exception e) {
			logger.error("修改失败", e);
			return AjaxPageResponse.FAILD("修改失败");
		}
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
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable Long id, Model model){
		BasePeople people = basePeopleService.getPeople(id);
		model.addAttribute("people", people);
		return AdminConstants.JSP_BASEPEOPLE + "/people_detail.jsp";
	}
	
	@ResponseBody
    @RequestMapping("titleSearch")
    public JSONArray  esearch(String txt, HttpServletResponse response) {
    	JSONArray tSearch = null;
    	System.out.println("1");
    	try{	    	    
    		tSearch=basePeopleService.titleSearchByEs(txt.trim());
    		return tSearch;
    	}
		catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
		}
    	
    }


	/**
	 * 根据字段 返回字段的类型 文本框？时间控件？ 下拉框？ 选择框？
	 *   默认的值
	 * @param peopleid
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value="smartSearch",method = RequestMethod.POST, headers="Accept=application/json")
	public ResultDto smartsearch(long peopleid, HttpServletResponse response){
		BasePeople people = basePeopleService.getPeople(peopleid);
		ResultDto Res = new ResultDto();
		Res.setData(people);
		Res.setType("1");
		Res.setStatus("success");
		return Res;
	}







}