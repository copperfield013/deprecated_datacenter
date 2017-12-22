package cn.sowell.datacenter.model.peopledata.service;

import java.util.List;

import cn.sowell.datacenter.model.peopledata.pojo.PeopleCompositeDictionaryItem;


public interface PeopleDictionaryService {

	
	List<PeopleCompositeDictionaryItem> getAllInfo(String code);

}
