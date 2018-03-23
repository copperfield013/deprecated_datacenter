package cn.sowell.datacenter.model.abc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.people.ExportDataPageInfo;
import cn.sowell.datacenter.model.basepeople.EntityPagingQueryProxy;
import cn.sowell.datacenter.model.basepeople.pojo.ExcelModel;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.EntityPropertyParser;
import cn.sowell.datacenter.model.modules.pojo.EntityHistoryItem;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.status.ImportStatus;
import cn.sowell.datacenter.model.tmpl.bean.QueryEntityParameter;

public interface ABCExecuteService {
	/**
	 * 
	 * @param data
	 * @return
	 */
	//Entity createEntity(Map<String, String> data);
	
	//Entity mergePeople(Map<String, String> data) throws IOException;

	/**
	 * 根据peopleCode获得人口数据
	 * @param peopleCode
	 * @return
	 */
	Entity getPeople(String peopleCode);

	List<Entity> queryPeopleList(List<Criteria> criterias, PageInfo pageInfo);
	
	List<Entity> queryPeopleList(Function<String, List<Criteria>> handler,
			PageInfo pageInfo);

	List<Entity> queryAddressList(List<Criteria> criterias, PageInfo pageInfo);
	
	List<Entity> queryAddressList(Function<String, List<Criteria>> handler,
			PageInfo pageInfo);
	
	/**
	 * 查询指定模块的
	 * @param param
	 * @return
	 */
	List<Entity> queryModuleEntities(QueryEntityParameter param);
	
	/**
	 * 导入人口
	 * @param sheet
	 */
	void importPeople(Sheet sheet, String dataType);

	/**
	 * 导入人口，并传入一个状态对象用于检测和控制导入状态
	 * @param sheet
	 * @param importStatus
	 * @param dataType
	 * @throws ImportBreakException 
	 */
	void importPeople(Sheet sheet, ImportStatus importStatus, String dataType) throws ImportBreakException;

	/**
	 * 删除人口
	 * @param peopleCode
	 */
	void deletePeople(String peopleCode);

	/**
	 * 
	 * @param peopleCode
	 * @param date
	 * @return
	 */
	Entity getHistoryPeople(String peopleCode, Date date, List<ErrorInfomation> errors);

	/**
	 * 
	 * @param code
	 * @param date
	 * @param errors
	 * @return
	 */
	Entity getHistoryAddress(String code, Date date, List<ErrorInfomation> errors);
	
	Entity savePeople(PeopleData people);

	Workbook downloadPeople(List<Map<String, Object>> listmap, List<TBasePeopleDictionaryEntity> keys, List<String[]> columnLists, ExcelModel model, String path);

	/**
	 * 查询人口的历史信息
	 * @param module
	 * @param code
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<EntityHistoryItem> queryHistory(String module, String code, Integer pageNo, Integer pageSize);

	/**
	 * 
	 * @param cs
	 * @param pageInfo
	 * @return
	 */
	EntityPagingQueryProxy getPeopleQueryProxy(List<Criteria> cs,
			ExportDataPageInfo pageInfo);

	/**
	 * 根据code获得统一地址entity对象
	 * @param code
	 * @return
	 */
	Entity getAddressEntity(String code);

	/**
	 * 
	 * @param param
	 * @param errors
	 * @return
	 */
	Entity getHistoryEntity(QueryEntityParameter param, List<ErrorInfomation> errors);
	/**
	 * 根据模块名和code获得实体对象
	 * @param module
	 * @param code
	 * @return
	 */
	Entity getModuleEntity(String module, String code);

	/**
	 * 根据code删除实体
	 * @param code
	 */
	void delete(String code);

	/**
	 * 保存或者修改数据库中的实体对象
	 * @param module
	 * @param propMap
	 * @return
	 */
	String mergeEntity(String module, Map<String, Object> propMap);
	
	/**
	 * 根据模块和code获得对应的的实体的转化对象
	 * @param module
	 * @param code
	 * @return
	 */
	EntityPropertyParser getModuleEntityParser(String module, String code);

	EntityPropertyParser getModuleEntityParser(String module, Entity entity);


}
