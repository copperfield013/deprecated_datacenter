package cn.sowell.datacenter.model.modules.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import com.abc.application.FusionContext;
import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;
import com.google.common.base.Supplier;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.entityResolver.FusionContextConfigFactory;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.config.ModuleMeta;
import cn.sowell.datacenter.model.abc.service.ABCExecuteService;
import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;
import cn.sowell.datacenter.model.dict.service.DictionaryService;
import cn.sowell.datacenter.model.modules.bean.EntityPagingIterator;
import cn.sowell.datacenter.model.modules.bean.EntityPagingQueryProxy;
import cn.sowell.datacenter.model.modules.bean.ExportDataPageInfo;
import cn.sowell.datacenter.model.modules.pojo.EntityHistoryItem;
import cn.sowell.datacenter.model.modules.service.ModulesImportService;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.tmpl.bean.QueryEntityParameter;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Service
public class ModulesServiceImpl implements ModulesService{
	
	@Resource
	ABCExecuteService abcService;
	
	@Resource
	FusionContextConfigFactory fFactory;
	
	@Resource
	TemplateService tService;
	
	@Resource
	DictionaryService dictService;
	
	@Resource
	ModulesImportService impService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	@Override
	public ListTemplateParameter exractTemplateParameter(Long tmplId,
			String module,
			HttpServletRequest request) {
		TemplateListTempalte ltmpl = null;
		UserIdentifier user = UserUtils.getCurrentUser();
		if(tmplId == null){
			ltmpl = tService.getDefaultListTemplate(user, module);
		}else{
			ltmpl = tService.getListTemplate(tmplId);
		}
		if(ltmpl == null) {
			return null;
		}else {
			Map<Long, NormalCriteria> vCriteriaMap = getCriteriasFromRequest(
					new ServletRequestParameterPropertyValues(request, "criteria", "_"), 
					CollectionUtils.toMap(ltmpl.getCriterias(), c->c.getId())); 
			ListTemplateParameter param = new ListTemplateParameter();
			param.setListTemplate(ltmpl);
			param.setNormalCriteriaMap(vCriteriaMap);
			param.setUser(user);
			return param;
		}
	}
	
	@Override
	public Map<Long, NormalCriteria> getCriteriasFromRequest(
			MutablePropertyValues pvs, Map<Long, TemplateListCriteria> criteriaMap) {
		 Map<Long, NormalCriteria> map = new HashMap<Long, NormalCriteria>();
		 pvs.getPropertyValueList().forEach(pv->{
			 Long criteriaId = FormatUtils.toLong(pv.getName());
			 if(criteriaId != null){
				 TemplateListCriteria criteria = criteriaMap.get(criteriaId);
				 if(criteria != null){
					 NormalCriteria ncriteria = new NormalCriteria(criteria);
					 //TODO: 需要将fieldKey转换成attributeName
					 ncriteria.setAttributeName(criteria.getFieldKey());
					 ncriteria.setValue(FormatUtils.toString(pv.getValue()));
					 map.put(criteriaId, ncriteria);
				 }
			 }
		 });
		 criteriaMap.forEach((criteriaId, criteria)->{
			 if(TextUtils.hasText(criteria.getDefaultValue()) && !map.containsKey(criteriaId)){
				 NormalCriteria nCriteria = new NormalCriteria(criteria);
				 //TODO: 需要将fieldKey转换成attributeName
				 nCriteria.setAttributeName(criteria.getFieldKey());
				 nCriteria.setValue(criteria.getDefaultValue());
				 map.put(criteriaId, nCriteria);
			 }
		 });;
		return map;
	}
	
	private Criteria createCriteria(NormalCriteria nCriteria, Supplier<Criteria> nFieldHandler, BiFunction<String, String, Criteria> relationFieldHandler) {
		DictionaryComposite composite = nCriteria.getComposite();
		if(composite != null && composite.getRelationSubdomain() != null) {
			String compositeName = composite.getName();
			return relationFieldHandler.apply(compositeName, nCriteria.getAttributeName().substring(compositeName.length() + 1));
		}else {
			return nFieldHandler.get();
		}
	}
	
	
	@Override
	public List<Criteria> toCriterias(Collection<NormalCriteria> nCriterias, String module){
		FusionContext context = fFactory.getDefaultConfig(module).createContext();
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);
		ArrayList<Criteria> cs = new ArrayList<Criteria>();
		nCriterias.forEach(nCriteria->{
			TemplateListCriteria criteria = nCriteria.getCriteria();
			if(TextUtils.hasText(nCriteria.getValue())){
				String attributeName = nCriteria.getAttributeName();
				if(attributeName.contains(".")) {
					nCriteria.setComposite(dictService.getCurrencyCacheCompositeByFieldId(module, nCriteria.getCriteria().getFieldId()));
				}
				String comparator = criteria.getComparator();
				if("t1".equals(comparator)){
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()), 
							(compositeName, suffix)->criteriaFactory.createLikeQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									nCriteria.getValue()
									)
							));
				}else if("t2".equals(comparator)){
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createLeftLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()), 
							(compositeName, suffix)->criteriaFactory.createLeftLikeQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									nCriteria.getValue()
									)
							));
				}else if("t3".equals(comparator)){
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createRightLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()), 
							(compositeName, suffix)->criteriaFactory.createRightLikeQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									nCriteria.getValue()
									)
							));
				}else if("t4".equals(comparator)){
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()), 
							(compositeName, suffix)->criteriaFactory.createQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									nCriteria.getValue()
									)
							));
				}else if("s1".equals(comparator)){
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()), 
							(compositeName, suffix)->criteriaFactory.createQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									nCriteria.getValue()
									)
							));
				}else if("d1".equals(comparator)) {
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()), 
							(compositeName, suffix)->criteriaFactory.createQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									nCriteria.getValue()
									)
							));
				}else if("d2".equals(comparator)) {
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createOpenBetweenQueryCriteria(nCriteria.getAttributeName(), null, nCriteria.getValue()), 
							(compositeName, suffix)->criteriaFactory.createOpenBetweenQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									null, nCriteria.getValue()
									)
							));
				}else if("d3".equals(comparator)) {
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createOpenBetweenQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue(), null), 
							(compositeName, suffix)->criteriaFactory.createOpenBetweenQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									nCriteria.getValue(),
									null
									)
							));
				}else if("l1".equals(comparator)) {
					Set<String> valueSet = new HashSet<>();
					
					if(nCriteria.getValue() != null) {
						for(String val : nCriteria.getValue().split(",")) {
							valueSet.add(val);
						}
					}
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createIncludeQueryCriteria(nCriteria.getAttributeName(), valueSet), 
							(compositeName, suffix)->criteriaFactory.createIncludeQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									valueSet
									)
							));
				}else if("dr1".equals(comparator)) {
					Date[] range = dateFormat.splitDateRange(nCriteria.getValue());
					String[] rangeStr = new String[2];
					rangeStr[0] = dateFormat.formatDate(range[0]);
					rangeStr[1] = dateFormat.formatDate(range[1]);
					cs.add(createCriteria(nCriteria, 
							()->criteriaFactory.createOpenBetweenQueryCriteria(nCriteria.getAttributeName(), rangeStr[0], rangeStr[1]), 
							(compositeName, suffix)->criteriaFactory.createOpenBetweenQueryCriteria(
									compositeName, 
									criteria.getRelationLabel(),
									suffix, 
									rangeStr[0], rangeStr[1]
									)
							));
				
					
				}
			}
		});
		return cs;
	}
	
	
	@Override
	public List<ModuleEntityPropertyParser> queryEntities(QueryEntityParameter param) {
		List<Entity> list = abcService.queryModuleEntities(param);
		return CollectionUtils.toList(list, entity->abcService.getModuleEntityParser(param.getModule(), entity));
	}

	
	@Override
	public ModuleMeta getModule(String moduleKey) {
		return fFactory.getModuleMeta(moduleKey);
	}
	
	
	@Override
	public ModuleEntityPropertyParser getEntity(String module, String code, Date date) {
		Entity entity = null;
		List<ErrorInfomation> errors = new ArrayList<ErrorInfomation>();
		if(date == null) {
			entity = abcService.getModuleEntity(module, code);
		}else {
			QueryEntityParameter param = new QueryEntityParameter();
			param.setModule(module);
			param.setCode(code);
			param.setHistoryTime(date);
			entity = abcService.getHistoryEntity(param, errors);
		}
		if(entity != null) {
			ModuleEntityPropertyParser parser = abcService.getModuleEntityParser(module, entity);
			parser.setErrors(errors);
			return parser;
		}else {
			return null;
		}
	}
	
	@Override
	public List<EntityHistoryItem> queryHistory(String module, String code, Integer pageNo, Integer pageSize) {
		return abcService.queryHistory(module, code, pageNo, pageSize);
	}


	@Override
	public void deleteEntity(String code) {
		abcService.delete(code);
	}
	
	@Override
	public String mergeEntity(String module, Map<String, Object> map) {
		return abcService.mergeEntity(module, map);
	}
	
	@Override
	public String fuseEntity(String module, Map<String, Object> map) {
		return abcService.fuseEntity(module, map);
	}
	
	@Override
	public EntityPagingIterator queryIterator(TemplateListTempalte ltmpl, Set<NormalCriteria> nCriterias,
			ExportDataPageInfo ePageInfo) {
		PageInfo pageInfo = ePageInfo.getPageInfo();
		List<Criteria> cs = toCriterias(nCriterias, ltmpl.getModule());
		String configName = fFactory.getDefaultConfigId(ltmpl.getModule());
		EntityPagingQueryProxy proxy = abcService.getModuleQueryProxy(configName, cs, ePageInfo);
		int dataCount = pageInfo.getPageSize();
		int startPageNo = pageInfo.getPageNo();
		int totalCount = proxy.getTotalCount();
		int ignoreCount = 0;
		if(totalCount < pageInfo.getPageSize()) {
			dataCount = totalCount;
			startPageNo = 1;
		}
		if("all".equals(ePageInfo.getScope())){
			dataCount = proxy.getTotalCount();
			startPageNo = 1;
			if(ePageInfo.getRangeStart() != null){
				ignoreCount = ePageInfo.getRangeStart() - 1;
				if(ePageInfo.getRangeEnd() != null){
					dataCount = ePageInfo.getRangeEnd() - ePageInfo.getRangeStart() + 1;
				}else{
					dataCount -= ePageInfo.getRangeStart() - 1;
				}
			}else if(ePageInfo.getRangeEnd() != null){
				dataCount = ePageInfo.getRangeEnd();
			}
		}
		return new EntityPagingIterator(totalCount, dataCount, ignoreCount, startPageNo, proxy);
	}


	
	
	
	
}
