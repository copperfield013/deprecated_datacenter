package cn.sowell.datacenter.admin.controller;

import java.util.LinkedHashMap;
import java.util.Map;

public interface AdminConstants {
	@SuppressWarnings("serial")
	final Map<String, String> ERROR_CODE_MAP = new LinkedHashMap<String, String>(){
		{
			put("1", "用户名或密码错误");
			put("2", "");
		}
	};

	/**
	 * 管理端的访问基本路径
	 */
	final String URI_BASE = "/admin";
	
	/**
	 * 人口模块
	 */
	final String URI_PEOPLEDATA = URI_BASE + "/peopledata";
	
	/**
	 * 管理端jsp资源的基本路径
	 */
	final String JSP_BASE = "/admin";

	/**
	 * 
	 */
	final String JSP_DEMO = JSP_BASE + "/demo";
	
	
	/**
	 *  管理端人口资源路径
	 */
	final String JSP_BASEPEOPLE = JSP_BASE + "/basepeople";


	final String JSP_FIELD = JSP_BASE + "/basefield";

	final String JSP_PEOPLEDATA = JSP_BASE + "/peopledata";
	
	final String JSP_SEARCH = JSP_BASE + "/search";


	/**
	 * 字段属性类型
	 */
	//下拉
	final String[] FRELD ={"2","3","4"};;
	
	final String JSP_ADDRESS = JSP_BASE + "/address";
	
	final String JSP_POSITION = JSP_BASE + "/position";
	
	final String JSP_SPECIAL_POSITION = JSP_BASE + "/specialposition";

	final String JSP_PEOPLEDATA_VIEWTMPL = JSP_PEOPLEDATA + "/viewtmpl";

	/**
	 * 默认的系统id
	 */
	final long SYS_DEF = 1;
	
}
