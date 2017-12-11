package cn.sowell.datacenter.model.address.dao;

import java.util.List;

import com.abc.extface.dto.AddressCode;
import com.abc.extface.dto.AddressEntity;
import com.abc.extface.dto.SplitedAddressEntity;

import cn.sowell.copframe.dto.page.PageInfo;


public interface AddressDao {

	List<SplitedAddressEntity> getAddressStrList(String addressStr, PageInfo pageInfo);
	
	List<SplitedAddressEntity> getTheSameAddress(String addressCode, PageInfo pageInfo);
	
	List<SplitedAddressEntity> getNotTheSameAddressList(String addressStr, String addressCode, PageInfo pageInfo);
	
	List<AddressCode> getAddressCodeList();
	
	void delete(SplitedAddressEntity splitedAddressEntity);
}
