package cn.sowell.datacenter.admin.controller.searchpop;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.basepeople.service.SearchPeopleService;

@Controller
@RequestMapping("/admin/search")
public class SearchPopController {
	@Resource
	SearchPeopleService searchPeopleService;
	
	Logger logger = Logger.getLogger(SearchPopController.class);
	
	@RequestMapping("/list")
	public String list(){
		
		return AdminConstants.JSP_SEARCH + "/search_list.jsp";
	}
	
	@ResponseBody
    @RequestMapping(value="peopleSearch",method = RequestMethod.POST, headers="Accept=application/json")
    public JSONArray  esearch(String name,String idCode,String address,String content, PageInfo pageInfo,HttpServletResponse response) {
    	JSONArray tSearch = null;
    	try{
    		tSearch=searchPeopleService.peopleSearch(name.trim(),idCode.trim(),address.trim(),content.trim(),pageInfo);
    		return tSearch;
    	}
		catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
		}
    	
    }

}
