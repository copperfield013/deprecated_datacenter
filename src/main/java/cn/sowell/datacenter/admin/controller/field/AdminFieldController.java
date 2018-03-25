package cn.sowell.datacenter.admin.controller.field;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonArrayResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.dict.service.DictionaryService;

@Controller
@RequestMapping("/admin/field")
public class AdminFieldController {

    Logger logger = Logger.getLogger(AdminFieldController.class);
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
    
	@ResponseBody
	@RequestMapping("/enum_json")
	public ResponseJSON enumJson(){
		JSONObjectResponse jRes = new JSONObjectResponse();
		List<DictionaryOption> itemList = dService.getAllOptions();
		if(itemList != null){
			JSONObject jo = jRes.getJsonObject();
			itemList.forEach(item->{
				String key = item.getGroupId().toString();
				JSONArray array = jo.getJSONArray(key);	
				if(array == null){
					array = new JSONArray();
					jo.put(key, array);
				}
				JSONObject jItem = new JSONObject();
				jItem.put("view", item.getTitle());
				jItem.put("value", item.getTitle());
				array.add(jItem);
			});
		}
		return jRes;
	}

}
