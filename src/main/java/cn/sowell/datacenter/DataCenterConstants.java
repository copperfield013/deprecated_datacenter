package cn.sowell.datacenter;

import java.util.HashMap;
import java.util.Map;

import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;

public interface DataCenterConstants {

	final Integer VALUE_TRUE = 1;
	final String COMMON_SPLITER = ";";
	
	final String TEMPLATE_TYPE_LIST = "list"; 
	final String TEMPLATE_TYPE_DETAIL = "detail";
	
	final String MODULE_KEY_PEOPLE = "people";
	final String MODULE_KEY_ADDRESS = "address";
	final Map<Class<?>, String> _TEMPLATE_MAP = new HashMap<>();
	static String mapTemplateType(Class<?> templateClass) {
		if(TemplateListTempalte.class.equals(templateClass)) {
			return TEMPLATE_TYPE_LIST;
		}else if(TemplateDetailTemplate.class.equals(templateClass)) {
			return TEMPLATE_TYPE_DETAIL;
		}
		throw new RuntimeException(templateClass + "的类型没有被维护");
	}
	

}
