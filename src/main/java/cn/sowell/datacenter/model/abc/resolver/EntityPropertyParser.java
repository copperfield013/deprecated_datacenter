package cn.sowell.datacenter.model.abc.resolver;

import java.util.List;
import java.util.Map;

import com.abc.dto.ErrorInfomation;

public interface EntityPropertyParser {

	String getCode();

	String getId();

	String getTitle();

	Map<String, Object> getMap();

	Object getProperty(String propertyName);

	Object getProperty(String propertyName, String propType);

	List<ErrorInfomation> getErrors();

	void setErrors(List<ErrorInfomation> errors);
	

}
