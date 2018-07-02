package cn.sowell.datacenter.ws.impl;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import cn.sowell.datacenter.entityResolver.FieldService;
import cn.sowell.datacenter.entityResolver.config.DBFusionConfigContextFactory;
import cn.sowell.datacenter.ws.DatacenterReloadService;

@WebService(endpointInterface="cn.sowell.datacenter.ws.DatacenterReloadService")
public class DatacenterReloadServiceImpl implements DatacenterReloadService{
	@Resource
	DBFusionConfigContextFactory fFactory;
	
	@Resource
	FieldService fService;
	Logger logger = Logger.getLogger(DatacenterReloadServiceImpl.class);
	
	
	@Override
	public String syncModule() {
		logger.info("接口通知模块数据刷新");
		fFactory.sync();
		fService.refreshFields();
		return "同步成功";
	}
	
	@Override
	public void syncField() {
		fService.refreshFields();
	}
	
}
