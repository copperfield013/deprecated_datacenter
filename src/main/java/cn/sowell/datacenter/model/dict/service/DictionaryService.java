package cn.sowell.datacenter.model.dict.service;

import java.util.List;

import cn.sowell.datacenter.model.dict.pojo.DictionaryComposite;

public interface DictionaryService {

	List<DictionaryComposite> getAllComposites(String module);
	
}
