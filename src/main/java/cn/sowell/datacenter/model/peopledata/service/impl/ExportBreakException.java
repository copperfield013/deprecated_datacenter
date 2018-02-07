package cn.sowell.datacenter.model.peopledata.service.impl;

public class ExportBreakException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4489272101712025471L;
	
	private String msg;
	
	public ExportBreakException() {
		// TODO Auto-generated constructor stub
	}

	
	public ExportBreakException(String msg) {
		this.msg = msg;
	}


	public String getMsg() {
		return msg;
	}
	
	

}
