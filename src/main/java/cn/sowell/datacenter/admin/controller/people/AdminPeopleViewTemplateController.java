package cn.sowell.datacenter.admin.controller.people;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.JsonArrayResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(AdminConstants.URI_PEOPLEDATA + "/viewtmpl")
public class AdminPeopleViewTemplateController {

	@Resource
	PeopleDictionaryService dictService;
	
	@RequestMapping("/to_create")
	public String toCreate(){
		return AdminConstants.JSP_PEOPLEDATA_VIEWTMPL + "/viewtmpl_create.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/field_json")
	public ResponseJSON fieldJson(){
		List<PeopleCompositeDictionaryItem> infoList = dictService.getAllInfo(null);
		JsonArrayResponse jRes = new JsonArrayResponse();
		for (PeopleCompositeDictionaryItem info : infoList) {
			jRes.add(JSON.toJSON(info));
		}
		return jRes;
	}
	
}
