package cn.sowell.datacenter.api2.controller.meta;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.api2.controller.Api2Constants;
import cn.sowell.datacenter.common.ApiUser;
import cn.sowell.datacenter.common.RequestParameterMapComposite;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.impl.ABCNodeProxy;
import cn.sowell.datacenter.model.admin.pojo.ABCUser;
import cn.sowell.datacenter.model.config.service.ConfigUserService;
import cn.sowell.datacenter.model.modules.bean.EntityDetail;
import cn.sowell.datacenter.model.modules.service.EntityConvertService;
import cn.sowell.dataserver.model.abc.service.EntityQueryParameter;
import cn.sowell.dataserver.model.abc.service.ModuleEntityService;
import cn.sowell.dataserver.model.modules.pojo.EntityHistoryItem;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.service.ArrayItemFilterService;

@Controller
@RequestMapping(Api2Constants.URI_META + "/user")
public class Api2UserController {
	
	@Resource
	ConfigUserService userService;
	
	@Resource
	ArrayItemFilterService arrayItemFilterService;
	
	@Resource
	ModuleEntityService entityService;
	
	@Resource
	EntityConvertService entityConvertService;
	
	static {
		JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue.getMask();
	}
	
	@ResponseBody
	@RequestMapping("/current_user")
	public JSONObjectResponse currentUser(ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		JSONObject jUser = new JSONObject();
		jUser.put("id", user.getId());
		jUser.put("username", user.getUsername());
		jUser.put("nickname", user.getNickname());
		jUser.put("token", user.getToken());
		JSONArray authorities = new JSONArray();
		if(user.getAuthorities() != null) {
			user.getAuthorities().forEach(auth->authorities.add(auth.getAuthority()));
		}
		jUser.put("authorities", authorities);
		jRes.put("user", jUser);
		
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/detail")
	public ResponseJSON detail(Long historyId, Long dtmplId, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		TemplateDetailTemplate dtmpl = userService.getUserDetailTemplate(dtmplId);
		if(dtmpl != null){
			
			String moduleName = userService.getUserModuleName();
			String code = null;
			if(user instanceof ABCUser) {
				code = ((ABCUser) user).getCode();
			}
			
			ModuleEntityPropertyParser entity = null;
			EntityQueryParameter param = new EntityQueryParameter(moduleName, code, user);
			param.setArrayItemCriterias(arrayItemFilterService.getArrayItemFilterCriterias(dtmpl.getId(), user));
			EntityHistoryItem lastHistory = entityService.getLastHistoryItem(param);
			if(historyId != null) {
				if(lastHistory != null && !historyId.equals(lastHistory.getId())) {
					entity = entityService.getHistoryEntityParser(param, historyId, null);
				}
	        }
	        if(entity == null) {
	        	entity = entityService.getEntityParser(param);
	        }
			
	        if(entity == null) {
				jRes.setStatus("error");
				jRes.put("message", "没有找到实体");
			}else {
				//用模板组合解析，并返回可以解析为json的对象
				EntityDetail detail = entityConvertService.convertEntityDetail(entity, dtmpl);
				
				jRes.put("dtmpl", dtmpl);
				jRes.put("entity", detail);
				jRes.put("errors", entityConvertService.toErrorItems(entity.getErrors()));
				jRes.put("historyId", historyId);
				jRes.setStatus("suc");
			}
		}
		return jRes;
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public ResponseJSON save(RequestParameterMapComposite composite, ApiUser user) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		if(user instanceof ABCUser) {
			ABCUser abcUser = (ABCUser) user;
			try {
				Map<String, Object> map = composite.getMap();
				map.put(ABCNodeProxy.CODE_PROPERTY_NAME_NORMAL, abcUser.getCode());
				userService.mergeUserEntity(composite.getMap(), abcUser);
				jRes.setStatus("suc");
				return jRes;
			} catch (Exception e) {
				jRes.setStatus("save error");
				jRes.put("message", e.getMessage());
			}
		}else {
			jRes.setStatus("error");
			jRes.setStatus("apiUser is not a ABCUser");
		}
		return jRes;
	}
}
