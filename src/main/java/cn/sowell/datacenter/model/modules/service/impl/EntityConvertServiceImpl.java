package cn.sowell.datacenter.model.modules.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.abc.dto.ErrorInfomation;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.datacenter.entityResolver.EntityConstants;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.ArrayItemPropertyParser;
import cn.sowell.datacenter.model.modules.bean.EntityArrayItemDetail;
import cn.sowell.datacenter.model.modules.bean.EntityDetail;
import cn.sowell.datacenter.model.modules.service.EntityConvertService;
import cn.sowell.dataserver.model.dict.pojo.DictionaryComposite;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailField;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.service.DetailTemplateService;

@Service
public class EntityConvertServiceImpl implements EntityConvertService{

	@Resource
	DetailTemplateService dtmplService;
	
	@Override
	public EntityDetail convertEntityDetail(ModuleEntityPropertyParser entity, TemplateGroup tmplGroup) {
		Assert.notNull(entity);
		EntityDetail detail = new EntityDetail(entity.getCode(), entity.getTitle());
		TemplateDetailTemplate dtmpl = dtmplService.getTemplate(tmplGroup.getDetailTemplateId());
		for (TemplateDetailFieldGroup group : dtmpl.getGroups()) {
			DictionaryComposite composite = group.getComposite();
			if(composite != null) {
				if(Integer.valueOf(1).equals(composite.getIsArray())) {
					List<ArrayItemPropertyParser> arrayItems = entity.getCompositeArray(composite.getName());
					if(arrayItems != null) {
						List<EntityArrayItemDetail> arrayItemDetails = new ArrayList<>();
						detail.getArrayMap().put(group.getId().toString(), arrayItemDetails);
						int index = 0;
						for (ArrayItemPropertyParser arrayItem : arrayItems) {
							EntityArrayItemDetail arrayItemDetail = new EntityArrayItemDetail(arrayItem.getCode());
							arrayItemDetail.setIndex(index++);
							if(composite.getRelationKey() != null) {
								arrayItemDetail.setRelationlabel(arrayItem.getFormatedProperty(composite.getName() + "." + EntityConstants.LABEL_KEY));
							}
							for (TemplateDetailField field : group.getFields()) {
								String fieldValue = arrayItem.getFormatedProperty(field.getFieldName());
								arrayItemDetail.getFieldMap().put(field.getId().toString(), fieldValue);
							}
							arrayItemDetails.add(arrayItemDetail);
						}
					}
				}
			}else {
				for (TemplateDetailField field : group.getFields()) {
					String fieldValue = entity.getFormatedProperty(field.getFieldName());
					detail.getFieldMap().put(field.getId().toString(), fieldValue);
				}
			}
		}
		return detail;
	}

	@Override
	public JSONArray toHistoryItems(List<EntityHistoryItem> historyItems, Long currentId) {
		JSONArray aHistoryItems = new JSONArray();
		if(historyItems != null) {
			boolean hasCurrentId = currentId != null;
			for (EntityHistoryItem historyItem : historyItems) {
				JSONObject jHistoryItem = new JSONObject();
				aHistoryItems.add(jHistoryItem);
				jHistoryItem.put("id", historyItem.getId());
				jHistoryItem.put("userName", historyItem.getUserName());
				jHistoryItem.put("time", historyItem.getTime());
				jHistoryItem.put("monthKey", historyItem.getMonthKey());
				if(hasCurrentId && historyItem.getId().equals(currentId)) {
					jHistoryItem.put("current", true);
				}
			}
			if(!hasCurrentId && !aHistoryItems.isEmpty()) {
				((JSONObject)aHistoryItems.get(0)).put("current", true);
			}
		}
		return aHistoryItems;
	}

	@Override
	public JSONArray toErrorItems(List<ErrorInfomation> errors) {
		JSONArray jArray = new JSONArray();
		if(errors != null) {
			for (ErrorInfomation error : errors) {
				JSONObject jError = new JSONObject();
				jError.put("id", error.getId().longValue());
				jError.put("code", error.getError_code());
				jError.put("content", error.getError_content());
				jError.put("message", error.getError_str());
				jArray.add(jError);
			}
		}
		return jArray;
	}

}