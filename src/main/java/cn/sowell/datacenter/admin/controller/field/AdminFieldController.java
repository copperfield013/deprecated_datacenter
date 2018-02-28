package cn.sowell.datacenter.admin.controller.field;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.JsonArrayResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;
import cn.sowell.datacenter.model.dict.service.DictionaryService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/admin/field")
public class AdminFieldController {

    Logger logger = Logger.getLogger(AdminFieldController.class);
    @Resource
	PeopleDictionaryService dictService;
    
    @Resource
    DictionaryService dService;
    
    
    @ResponseBody
	@RequestMapping("/json/{module}")
	public ResponseJSON fieldJson(@PathVariable String module){
		List<DictionaryComposite> infoList = dService.getAllComposites(module);
		JsonArrayResponse jRes = new JsonArrayResponse();
		for (DictionaryComposite info : infoList) {
			jRes.add(JSON.toJSON(info));
		}
		return jRes;
	}

}
