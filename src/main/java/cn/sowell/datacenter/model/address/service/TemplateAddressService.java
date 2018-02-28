package cn.sowell.datacenter.model.address.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.address.pojo.AddressData;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;

public interface TemplateAddressService {
	/**
	 * 
	 * @param criteriaMap 
	 * @param pageInfo
	 * @return
	 */
	List<AddressData> queryAddressList(Set<NormalCriteria> criteriaMap,
			PageInfo pageInfo);

	AddressData getAddress(String code);
	
	/**
	 * 
	 * @param code
	 * @param date
	 * @return
	 */
	AddressData getHistoryAddress(String code, Date date);
}
