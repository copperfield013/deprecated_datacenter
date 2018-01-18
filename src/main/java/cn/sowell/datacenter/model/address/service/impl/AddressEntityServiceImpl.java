package cn.sowell.datacenter.model.address.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.address.dao.AddressDao;
import cn.sowell.datacenter.model.address.service.AddressEntityService;

import com.abc.extface.dto.AddressCode;
import com.abc.extface.dto.SplitedAddressEntity;

@Service
public class AddressEntityServiceImpl implements AddressEntityService {

	@Resource
	AddressDao addressDao;
	
	@Override
	public List<SplitedAddressEntity> queryAddressStrList(String addressStr, PageInfo pageInfo) {
		return addressDao.getAddressStrList(addressStr, pageInfo);
	}

	@Override
	public List<SplitedAddressEntity> findTheSameAddress(String addressCode, PageInfo pageInfo) {
		return addressDao.getTheSameAddress(addressCode, pageInfo);
	}
	
	@Override
	public List<SplitedAddressEntity> queryNotTheSameAddressList(String addressStr, String addressCode, PageInfo pageInfo) {
		return addressDao.getNotTheSameAddressList(addressStr, addressCode, pageInfo);
	}


	@Override
	public List<AddressCode> getAddressCodeList() {
		return addressDao.getAddressCodeList();
	}

	@Override
	public void deleteAddressEntity(String addressStr) {
		SplitedAddressEntity splitedAddressEntity = new SplitedAddressEntity();
		splitedAddressEntity.setName(addressStr);
		addressDao.delete(splitedAddressEntity);
	}
}
