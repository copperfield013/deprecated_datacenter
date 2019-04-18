package cn.sowell.datacenter.api2.controller.auth;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.datacenter.api2.controller.Api2Constants;
import cn.sowell.datacenter.model.admin.service.AdminUserService;

@Controller
@RequestMapping(Api2Constants.URI_BASE + "/auth")
public class Api2AuthController {
	@Resource
	AdminUserService uService;
	
	@ResponseBody
	@RequestMapping("/token")
	public ResponseJSON getToken(@RequestParam String username, @RequestParam String password) {
		JSONObjectResponse jRes = new JSONObjectResponse();
		if(username.isEmpty() || password.isEmpty()) {
			jRes.setStatus("error");
			jRes.put("errorMsg", "账号密码不能为空");
		}else {
			String token = uService.renderToken("api2", username, password);
			if(token != null) {
				jRes.setStatus("suc");
				jRes.put("token", token);
			}else {
				jRes.setStatus("error");
				jRes.put("errorMsg", "密码错误或用户不存在");
			}
		}
		return jRes;
	}
}
