package cn.sowell.datacenter.model.basepeople;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Sheet;

import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.status.ImportStatus;

public interface ABCExecuteService {
	/**
	 * 
	 * @param data
	 * @return
	 */
	//Entity createEntity(Map<String, String> data);
	
	//Entity mergePeople(Map<String, String> data) throws IOException;

	/**
	 * 查询人口数据
	 * @param criterias
	 * @param pageInfo
	 * @return
	 */
	List<Entity> queryPeopleList(List<Criteria> criterias, PageInfo pageInfo);

	/**
	 * 根据peopleCode获得人口数据
	 * @param peopleCode
	 * @return
	 */
	Entity getPeople(String peopleCode);

	List<Entity> queryPeopleList(Function<String, List<Criteria>> handler,
			PageInfo pageInfo);

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

	Entity savePeople(PeopleData people);

	

	
}
