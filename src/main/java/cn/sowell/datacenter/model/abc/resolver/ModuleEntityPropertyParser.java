package cn.sowell.datacenter.model.abc.resolver;

import java.util.List;
import java.util.Map;

import com.abc.dto.ErrorInfomation;

import cn.sowell.datacenter.model.abc.resolver.impl.ArrayItemPropertyParser;

public interface ModuleEntityPropertyParser extends CEntityPropertyParser{
	
	String getId();

	String getTitle();
	
	String getCode();
	
	Map<String, List<ArrayItemPropertyParser>> getArrayMap();

	
	List<ErrorInfomation> getErrors();

	void setErrors(List<ErrorInfomation> errors);


	List<ArrayItemPropertyParser> getCompositeArray(String compositeName);

	

}
