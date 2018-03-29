package cn.sowell.datacenter.admin.controller.tmpl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.datacenter.admin.controller.AdminConstants;

@Controller
@RequestMapping(AdminConstants.URI_TMPL + "/group")
public class AdminTemplateGruopController {
	
	Logger logger = Logger.getLogger(AdminTemplateGruopController.class);
	
	@RequestMapping("/list/{module}")
	public String list(String module) {
		return AdminConstants.JSP_TMPL_GROUP + "/tmpl_group_list.jsp";
	}
	
	
	
}
