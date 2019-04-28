package cn.sowell.datacenter.api2.controller.meta;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.datacenter.api2.controller.Api2Constants;
import cn.sowell.datacenter.model.config.service.ConfigUserService;
import cn.sowell.datacenter.model.modules.service.EntityConvertService;
import cn.sowell.dataserver.model.abc.service.ModuleEntityService;
import cn.sowell.dataserver.model.tmpl.service.ArrayItemFilterService;
import cn.sowell.dataserver.model.tmpl.service.DetailTemplateService;
import cn.sowell.dataserver.model.tmpl.service.ListCriteriaFactory;

@Controller
@RequestMapping(Api2Constants.URI_META + "/entity")
public class Api2MetaEntityController {
	
	@Resource
	ArrayItemFilterService arrayItemFilterService;
	
	@Resource
	ModuleEntityService entityService;
	
	@Resource
	EntityConvertService entityConvertService;
	
	@Resource
	ConfigUserService userService;
	
	@Resource
	DetailTemplateService dtmplService;

	@Resource
	ListCriteriaFactory lcriteriFacrory;
	
	
}
