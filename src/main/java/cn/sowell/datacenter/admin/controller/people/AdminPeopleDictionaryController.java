package cn.sowell.datacenter.admin.controller.people;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.dict.pojo.DictionaryOption;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(AdminConstants.URI_PEOPLEDATA + "/dict")
public class AdminPeopleDictionaryController {
	
	@Resource
	PeopleDictionaryService dictService;
	
	@ResponseBody
	@RequestMapping("/enum_json")
	public ResponseJSON enumJson(){
		JSONObjectResponse jRes = new JSONObjectResponse();
		List<DictionaryOption> itemList = dictService.getAllEnumList();
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
