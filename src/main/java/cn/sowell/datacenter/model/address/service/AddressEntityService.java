package cn.sowell.datacenter.model.address.service;

import java.util.List;

import cn.sowell.copframe.dto.page.PageInfo;

import com.abc.extface.dto.AddressCode;
import com.abc.extface.dto.SplitedAddressEntity;


public interface AddressEntityService {
	
	List<SplitedAddressEntity> queryAddressStrList(String addressStr, PageInfo pageInfo);
	
	List<SplitedAddressEntity> findTheSameAddress(String addressCode, PageInfo pageInfo);
	
	List<SplitedAddressEntity> queryNotTheSameAddressList(String addressStr, String addressCode, PageInfo pageInfo);

	List<AddressCode> getAddressCodeList();
	
	void deleteAddressEntity(String addressStr);

}
