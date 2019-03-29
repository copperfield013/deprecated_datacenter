package cn.sowell.datacenter.api2.controller.entity;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.api2.controller.Api2Constants;

@Controller
@RequestMapping(Api2Constants.URI_ENTITY + "/curd")
public class Api2EntityCURDController {
	@RequestMapping("/load_entities_queried/{queryKey}")
	public ResponseJSON loadEntitiesQueried(
			@PathVariable String queryKey, 
			String entityCodes, HttpSession session) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		
		
		return jRes;
	}
}
