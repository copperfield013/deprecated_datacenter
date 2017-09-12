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

	final String JSP_PEOPLEDATA = JSP_BASE + "/peopledata";
	
}
