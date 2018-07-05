package cn.sowell.datacenter.ws.impl;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import cn.sowell.datacenter.entityResolver.FieldService;
import cn.sowell.datacenter.entityResolver.config.DBFusionConfigContextFactory;
import cn.sowell.datacenter.entityResolver.config.ModuleConfigureMediator;
import cn.sowell.datacenter.ws.DatacenterReloadService;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@WebService(endpointInterface="cn.sowell.datacenter.ws.DatacenterReloadService")
public class DatacenterReloadServiceImpl implements DatacenterReloadService, InitializingBean{
	@Resource
	DBFusionConfigContextFactory fFactory;
	
	@Resource
	ModuleConfigureMediator moduleMediator;
	
	@Resource
	FieldService fService;
	
	@Resource
	TemplateService tService;
	
	
	Logger logger = Logger.getLogger(DatacenterReloadServiceImpl.class);
	
	
	@Override
	public String syncModule() {
		logger.info("接口通知模块数据刷新");
		fService.refreshFields();
		moduleMediator.refresh();
		fFactory.sync();
		tService.clearCache();
		return "同步成功";
	}
	
	@Override
	public void syncField() {
		fService.refreshFields();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tService.loadCache();
	}
	
}
