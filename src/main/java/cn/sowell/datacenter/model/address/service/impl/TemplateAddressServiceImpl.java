package cn.sowell.datacenter.model.address.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.query.criteria.Criteria;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.abc.service.ABCExecuteService;
import cn.sowell.datacenter.model.address.pojo.AddressData;
import cn.sowell.datacenter.model.address.service.TemplateAddressService;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.peopledata.service.impl.EntityTransfer;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;

@Service
public class TemplateAddressServiceImpl implements TemplateAddressService {

	@Resource
	ListTemplateService ltmplService;
	
	@Resource
	ABCExecuteService abcService;
	
	@Resource
	ModulesService mService;
	
	@Override
	public List<AddressData> queryAddressList(Set<NormalCriteria> criteriaMap,
			PageInfo pageInfo) {
		List<Criteria> cs = mService.toCriterias(criteriaMap);
		List<Entity> list = abcService.queryAddressList(cs, pageInfo);
		List<AddressData> res = new ArrayList<AddressData>();
		list.forEach(e->res.add(transfer(e)));
		return res;
	}

	EntityTransfer eTransfer = new EntityTransfer();
	
	private AddressData transfer(Entity entity) {
		
		if(entity != null){
			AddressData people = new AddressData();
			eTransfer.bind(entity, people);
			return people;
		}
		return null;
	}

	@Override
	public AddressData getAddress(String code) {
		Entity entity = abcService.getAddressEntity(code);
		return transfer(entity);
	}
	
	@Override
	public AddressData getHistoryAddress(String code, Date date) {
		List<ErrorInfomation> errors = new ArrayList<ErrorInfomation>();
		Entity address;
		if(date != null) {
			address = abcService.getHistoryAddress(code, date, errors);
		}else {
			address = abcService.getAddressEntity(code);
		}
		AddressData addressData = transfer(address);
		if(addressData != null){
			addressData.setErrors(errors);
		}
		return addressData;
	}
}
