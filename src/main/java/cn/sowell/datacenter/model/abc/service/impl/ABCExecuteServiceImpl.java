package cn.sowell.datacenter.model.abc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.application.BizFusionContext;
import com.abc.application.FusionContext;
import com.abc.application.RemovedFusionContext;
import com.abc.dto.ErrorInfomation;
import com.abc.extface.dto.RecordHistory;
import com.abc.mapping.entity.Entity;
import com.abc.panel.Discoverer;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;
import com.abc.query.criteria.Criteria;
import com.abc.query.entity.impl.EntitySortedPagedQuery;
import com.abc.record.HistoryTracker;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfig;
import cn.sowell.datacenter.model.abc.resolver.FusionContextFactoryDC;
import cn.sowell.datacenter.model.abc.service.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.dao.PropertyDictionaryDao;
import cn.sowell.datacenter.model.modules.EntityPropertyParser;
import cn.sowell.datacenter.model.modules.bean.EntityPagingQueryProxy;
import cn.sowell.datacenter.model.modules.bean.EntityQueryAdapter;
import cn.sowell.datacenter.model.modules.bean.ExportDataPageInfo;
import cn.sowell.datacenter.model.modules.pojo.EntityHistoryItem;
import cn.sowell.datacenter.model.tmpl.bean.QueryEntityParameter;


@Service
public class ABCExecuteServiceImpl implements ABCExecuteService{

	Logger logger = Logger.getLogger(ABCExecuteService.class);
	
	@Resource
	FusionContextFactoryDC fFactory;
	
	@Resource
	PropertyDictionaryDao dictionartDao;
	
	
	@Override
	public EntityPagingQueryProxy getModuleQueryProxy(String entityConfig, List<Criteria> cs, ExportDataPageInfo ePageInfo) {
		FusionContextConfig config = fFactory.getConfig(entityConfig);
		BizFusionContext context = fFactory.getContext(entityConfig);
		Discoverer discoverer=PanelFactory.getDiscoverer(context);
		EntitySortedPagedQuery sortedPagedQuery = discoverer.discover(cs, "编辑时间");
		
		PageInfo pageInfo = ePageInfo.getPageInfo();
		if("all".equals(ePageInfo.getScope())){
			return new EntityQueryAdapter(sortedPagedQuery, config.getConfigResolver(), ePageInfo.getQueryCacheCount());
		}else{
			return new EntityQueryAdapter(sortedPagedQuery, config.getConfigResolver(), pageInfo.getPageSize());
		}
	}
	
	
	private List<Entity> queryEntityList(String entityConfig, List<Criteria> criterias, PageInfo pageInfo){
		BizFusionContext context = fFactory.getContext(entityConfig);
		Discoverer discoverer=PanelFactory.getDiscoverer(context);
		
		EntitySortedPagedQuery sortedPagedQuery = discoverer.discover(criterias, "编辑时间");
		sortedPagedQuery.setPageSize(pageInfo.getPageSize());
		pageInfo.setCount(sortedPagedQuery.getAllCount());
		List<Entity> peoples = sortedPagedQuery.visit(pageInfo.getPageNo());
		return peoples;
	}
	
	@Override
	public List<Entity> queryModuleEntities(QueryEntityParameter param) {
		return queryEntityList(
				fFactory.mapDefaultModuleEntityConfig(param.getModule()), 
				param.getCriterias(), 
				param.getPageInfo());
	}
	
	
	private Entity getHistoryEntity(String configKey, String code,
			Date date, List<ErrorInfomation> errors) {
		BizFusionContext context = fFactory.getContext(configKey);
		Discoverer discoverer=PanelFactory.getDiscoverer(context);
		
		HistoryTracker tracker = discoverer.track(code, date);
		List<ErrorInfomation> errorInfomations = tracker.getErrorInfomations();
		if(errors != null && errorInfomations != null && !errorInfomations.isEmpty()){
			errors.addAll(errorInfomations);
		}
		return tracker.getEntity();
	}
	
	@Override
	public Entity getHistoryEntity(QueryEntityParameter param, List<ErrorInfomation> errors) {
		return getHistoryEntity(
				fFactory.mapDefaultModuleEntityConfig(param.getModule()), 
				param.getCode(), 
				param.getHistoryTime(), 
				errors);
	}
	
	@Override
	public List<EntityHistoryItem> queryHistory(String module, String code,
			Integer pageNo, Integer pageSize) {
		BizFusionContext context = fFactory.getContext(fFactory.mapDefaultModuleEntityConfig(module));
		Discoverer discoverer=PanelFactory.getDiscoverer(context);
		
		List<RecordHistory> historyList = discoverer.trackHistory(code, pageNo, pageSize);
		
		List<EntityHistoryItem> list = new ArrayList<EntityHistoryItem>();
		historyList.forEach(history->{
			EntityHistoryItem item = new EntityHistoryItem();
			item.setId(FormatUtils.toLong(history.getId()));
			item.setTime(history.getCreationTime());
			item.setUserName(toUserName(history.getUsergroupId()));
			item.setDesc(history.getContent());
			list.add(item);
		});
		return list;
	}

	private String toUserName(String usergroupId) {
		return "户户户";
	}
	
	@Override
	public Entity getModuleEntity(String module, String code) {
		BizFusionContext context = fFactory.getContext(fFactory.mapDefaultModuleEntityConfig(module));
		Discoverer discoverer=PanelFactory.getDiscoverer(context);
		Entity result=discoverer.discover(code);
		return result;
	}
	

	@Override
	public void delete(String code) {
		RemovedFusionContext appInfo=new RemovedFusionContext(code, null, "list-delete" );
		if(!PanelFactory.getIntegration().remove(appInfo)){
			throw new RuntimeException("删除失败");
		}
	}
	
	@Override
	public String mergeEntity(String module, Map<String, Object> propMap) {
		String configName = fFactory.mapDefaultModuleEntityConfig(module);
		FusionContextConfig config = fFactory.getConfig(configName);
		if(config.getConfigResolver() == null) {
			throw new RuntimeException("config[" + configName + "]没有注入对应的ConfigResolver对象");
		}
		Entity entity = config.getConfigResolver().createEntity(propMap);
		if(entity != null) {
			BizFusionContext context = fFactory.getContext(configName);
			context.setSource(FusionContext.SOURCE_COMMON);
			
			Integration integration=PanelFactory.getIntegration();
			return integration.integrate(entity, context);
		}else {
			throw new RuntimeException("无法创建config[" + configName + "]的entity对象");
		}
	}	
	
	@Override
	public EntityPropertyParser getModuleEntityParser(String module, String code) {
		return getModuleEntityParser(module, getModuleEntity(module, code));
	}
	
	@Override
	public EntityPropertyParser getModuleEntityParser(String module, Entity entity) {
		String configName = fFactory.mapDefaultModuleEntityConfig(module);
		FusionContextConfig config = fFactory.getConfig(configName);
		return config.getConfigResolver().createParser(entity);
	}


}
