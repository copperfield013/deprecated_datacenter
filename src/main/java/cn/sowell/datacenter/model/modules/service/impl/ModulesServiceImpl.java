package cn.sowell.datacenter.model.modules.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import com.abc.application.FusionContext;
import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.CriteriaFactory;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.model.abc.service.ABCExecuteService;
import cn.sowell.datacenter.model.abc.service.impl.FusionContextFactoryDC;
import cn.sowell.datacenter.model.dict.service.DictionaryService;
import cn.sowell.datacenter.model.modules.EntityPropertyParser;
import cn.sowell.datacenter.model.modules.pojo.EntityHistoryItem;
import cn.sowell.datacenter.model.modules.pojo.ModuleMeta;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.tmpl.bean.QueryEntityParameter;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Repository
public class ModulesServiceImpl implements ModulesService{
	
	@Resource
	ABCExecuteService abcService;
	
	@Resource
	FusionContextFactoryDC fFactory;
	
	@Resource
	TemplateService tService;
	
	@Resource
	DictionaryService dictService;
	
	Map<String, ModuleMeta> moduleMap;
	
	public ModulesServiceImpl() {
		moduleMap = new HashMap<String, ModuleMeta>();
		ModuleMeta peopleModule = new ModuleMeta();
		peopleModule.setKey(DataCenterConstants.MODULE_KEY_PEOPLE);
		peopleModule.setTitle("人口");
		ModuleMeta addressModule = new ModuleMeta();
		addressModule.setKey(DataCenterConstants.MODULE_KEY_ADDRESS);
		addressModule.setTitle("地址");
		moduleMap.put(addressModule.getKey(), addressModule);
		moduleMap.put(peopleModule.getKey(), peopleModule);
	}
	
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
		Map<Long, NormalCriteria> vCriteriaMap = getCriteriasFromRequest(
				new ServletRequestParameterPropertyValues(request, "criteria", "_"), 
				CollectionUtils.toMap(ltmpl.getCriterias(), c->c.getId())); 
		ListTemplateParameter param = new ListTemplateParameter();
		param.setListTemplate(ltmpl);
		param.setNormalCriteriaMap(vCriteriaMap);
		param.setUser(user);
		return param;
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
	
	@Override
	public List<Criteria> toCriterias(Collection<NormalCriteria> nCriterias){
		FusionContext context = fFactory.getContext(FusionContextFactoryDC.KEY_BASE);
		CriteriaFactory criteriaFactory = new CriteriaFactory(context);
		ArrayList<Criteria> cs = new ArrayList<Criteria>();
		nCriterias.forEach(nCriteria->{
			TemplateListCriteria criteria = nCriteria.getCriteria();
			if(TextUtils.hasText(nCriteria.getValue())){
				String comparator = criteria.getComparator();
				if("t1".equals(comparator)){
					cs.add(criteriaFactory.createLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("t2".equals(comparator)){
					cs.add(criteriaFactory.createLeftLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("t3".equals(comparator)){
					cs.add(criteriaFactory.createRightLikeQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("t4".equals(comparator)){
					cs.add(criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}else if("s1".equals(comparator)){
					cs.add(criteriaFactory.createQueryCriteria(nCriteria.getAttributeName(), nCriteria.getValue()));
				}
			}
		});
		return cs;
	}
	
	@Override
	public List<EntityPropertyParser> queryEntities(QueryEntityParameter param) {
		List<Entity> list = abcService.queryModuleEntities(param);
		return CollectionUtils.toList(list, entity->abcService.getModuleEntityParser(param.getModule(), entity));
	}

	
	@Override
	public ModuleMeta getModule(String moduleKey) {
		return moduleMap.get(moduleKey);
	}
	
	
	@Override
	public EntityPropertyParser getEntity(String module, String code, Date date) {
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
			EntityPropertyParser parser = abcService.getModuleEntityParser(module, entity);
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
	
}
