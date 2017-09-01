package cn.sowell.datacenter.model.basepeople;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Sheet;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.people.status.ImportStatus;

import com.abc.mapping.entity.SocialEntity;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.query.querypeople.Criteria;

public interface ABCExecuteService {
	/**
	 * 
	 * @param data
	 * @return
	 */
	SocialEntity createSocialEntity(Map<String, String> data);
	
	People mergePeople(Map<String, String> data) throws IOException;

	/**
	 * 查询人口数据
	 * @param criterias
	 * @param pageInfo
	 * @return
	 */
	List<People> queryPeopleList(List<Criteria> criterias, PageInfo pageInfo);

	/**
	 * 根据peopleCode获得人口数据
	 * @param peopleCode
	 * @return
	 */
	People getPeople(String peopleCode);

	List<People> queryPeopleList(Function<ABCNode, List<Criteria>> handler,
			PageInfo pageInfo);

	/**
	 * 导入人口
	 * @param sheet
	 */
	void importPeople(Sheet sheet);

	/**
	 * 导入人口，并传入一个状态对象用于检测和控制导入状态
	 * @param sheet
	 * @param importStatus
	 * @throws ImportBreakException 
	 */
	void importPeople(Sheet sheet, ImportStatus importStatus) throws ImportBreakException;

	/**
	 * 删除人口
	 * @param peopleCode
	 */
	void deletePeople(String peopleCode);

}
