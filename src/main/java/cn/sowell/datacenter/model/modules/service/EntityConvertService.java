package cn.sowell.datacenter.model.modules.service;

import java.util.List;

import com.abc.dto.ErrorInfomation;
import com.alibaba.fastjson.JSONArray;

import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.modules.bean.EntityDetail;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;

public interface EntityConvertService {
	EntityDetail convertEntityDetail(ModuleEntityPropertyParser entity, TemplateGroup tmplGroup);

	JSONArray toHistoryItems(List<EntityHistoryItem> historyItems, Long currentId);

	JSONArray toErrorItems(List<ErrorInfomation> errors); 
}
