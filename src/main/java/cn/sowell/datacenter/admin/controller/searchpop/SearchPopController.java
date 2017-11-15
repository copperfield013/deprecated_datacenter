package cn.sowell.datacenter.admin.controller.searchpop;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.service.SearchPeopleService;
import cn.sowell.datacenter.model.esbasic.service.EsBasicService;

@Controller
@RequestMapping("/admin/search")
public class SearchPopController {
	@Resource
	SearchPeopleService searchPeopleService;
	
	@Resource
	EsBasicService esBasicService;
	
	Logger logger = Logger.getLogger(SearchPopController.class);
	
	@RequestMapping("/list")
	public String list(){
		
		return AdminConstants.JSP_SEARCH + "/search_list.jsp";
	}
	
	@ResponseBody
    @RequestMapping(value="peopleSearch",method = RequestMethod.POST, headers="Accept=application/json")
    public JSONObject  esearch(String name,String idCode,String address,String content,Integer pageNo,PageInfo pageInfo,HttpServletResponse response) {
    	try{
    		pageInfo.setPageNo(pageNo);
    		JSONArray searchPeople = new JSONArray();
    		searchPeople = searchPeopleService.peopleSearch(name.trim(),idCode.trim(),address.trim(),content.trim(),pageInfo);
    		JSONObject json = new JSONObject();
    		json.put("people",searchPeople);
    		json.put("page", pageInfo);
    		return json;
    	}
		catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
		}
    	
    }
	
	@ResponseBody
	@RequestMapping("/do_delete/{id}")
	public AjaxPageResponse doDelte(@PathVariable String id){
		try {
			esBasicService.eSearchDelete(id);;
			return AjaxPageResponse.REFRESH_LOCAL("删除成功");
		} catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败");
		}
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable String id, Model model){
		Map<String, Object> search = esBasicService.eSearchGet(id);
		model.addAttribute("search", search);
		return AdminConstants.JSP_SEARCH + "/search_detail.jsp";
	}


}
