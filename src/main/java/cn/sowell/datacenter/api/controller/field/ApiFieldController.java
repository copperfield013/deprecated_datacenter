package cn.sowell.datacenter.api.controller.field;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.model.config.service.AuthorityService;
import cn.sowell.dataserver.model.dict.pojo.DictionaryOption;
import cn.sowell.dataserver.model.dict.pojo.OptionItem;
import cn.sowell.dataserver.model.dict.service.DictionaryService;

@Controller
@RequestMapping("/api/field")
public class ApiFieldController {
	
	@Resource
	DictionaryService dictService;
	
	@Resource
	AuthorityService authService;
	
	@ResponseBody
	@RequestMapping("/options")
	public ResponseJSON getOptions(@RequestParam Set<Long> fieldIds, ApiUser user) {
		JSONObjectResponse res = new JSONObjectResponse();
		Map<Long, List<OptionItem>> optionsMap = dictService.getOptionsMap(fieldIds);
		JSONObject map = new JSONObject();
		String keyPrefix = "field_";
		optionsMap.forEach((fieldId, options)->{
			map.put(keyPrefix + fieldId, options);
		});
		res.put("keyPrefix", keyPrefix);
		res.put("optionsMap", map);
		return res;
	}
	
	@ResponseBody
	@RequestMapping("/cas_ops/{optGroupId}")
	public ResponseJSON casOptions(@PathVariable Long optGroupId, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		List<DictionaryOption> options = dictService.queryOptions(optGroupId);
		jRes.put("options", options);
		jRes.setStatus("suc");
		return jRes;
	}
}
