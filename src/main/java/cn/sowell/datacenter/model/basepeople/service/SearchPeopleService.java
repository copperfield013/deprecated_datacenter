package cn.sowell.datacenter.model.basepeople.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dto.page.PageInfo;

public interface SearchPeopleService {
	
	public JSONArray peopleSearch(String name,String idCode,String address,String content, PageInfo pageInfo);
	
	public void eSearchUpdate(JSONObject json,String id);
}
