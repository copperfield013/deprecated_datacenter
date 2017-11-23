package cn.sowell.datacenter.model.address.service;

import java.util.List;

import com.abc.extface.dto.AddressCode;
import com.abc.extface.dto.AddressElement;
import com.abc.extface.dto.AddressEntity;
import com.abc.extface.dto.SplitedAddressEntity;

import cn.sowell.copframe.dto.page.PageInfo;


public interface AddressEntityService {
	
	List<SplitedAddressEntity> queryAddressStrList(String addressStr, PageInfo pageInfo);
	
	List<SplitedAddressEntity> findTheSameAddress(String addressCode, PageInfo pageInfo);

	List<AddressCode> getAddressCodeList();
	
	void deleteAddressEntity(String addressStr);

}
