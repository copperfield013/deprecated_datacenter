package cn.sowell.datacenter.admin.controller.basepeople;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.sowell.datacenter.model.basepeople.BasePeopleDictionaryCriteria;
import cn.sowell.datacenter.model.basepeople.dto.ResultDto;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
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

	@org.springframework.web.bind.annotation.InitBinder
	public void InitBinder(ServletRequestDataBinder binder) {
		//System.out.println("执行了InitBinder方法");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}

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
		BasePeople people = basePeopleService.getPeopleById(id);
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
		BasePeople people = basePeopleService.getPeopleById(id);
		model.addAttribute("people", people);
		return AdminConstants.JSP_BASEPEOPLE + "/people_detail.jsp";
	}
	
	@ResponseBody
    @RequestMapping(value="titleSearch",method = RequestMethod.POST, headers="Accept=application/json")
    public JSONArray  esearch(String txt, HttpServletResponse response) {
    	JSONArray tSearch = null;
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



	@ResponseBody
	@RequestMapping(value="smartSearch",method = RequestMethod.POST, headers="Accept=application/json")
	public ResultDto smartsearch(long peopleid,String type,String field, HttpServletResponse response) {
		ResultDto Res = new ResultDto();
		try {
			BasePeople people = basePeopleService.getPeopleById(peopleid);
			Res.setData(people);
			Res.setType(type);
			Res.setStatus("success");
			if(Arrays.asList(AdminConstants.FRELD).contains(field)){
			Res.setFieldList(basePeopleService.FieldList(field));
			}
		} catch (Exception e){
			Res.setStatus("failed");
		}
		return  Res;
	}

	/**
	 *
	 * @param criteria
	 * @param model
	 * @param pageInfo
	 * @return
	 */
	@RequestMapping("/diclist")
	public String diclist(BasePeopleDictionaryCriteria criteria, Model model, PageInfo pageInfo){
		List<TBasePeopleDictionaryEntity> list = basePeopleService.querydicList(criteria, pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("criteria", criteria);
		return AdminConstants.JSP_BASEPEOPLE + "/dictionary_list.jsp";
	}


	@RequestMapping("/itemlist/{field}")
	public String itemlist(@PathVariable String field, Model model, PageInfo pageInfo){
		model.addAttribute("list", basePeopleService.FieldList(field));
		return AdminConstants.JSP_BASEPEOPLE + "/item_list.jsp";
	}

}