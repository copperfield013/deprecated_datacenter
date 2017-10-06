package cn.sowell.datacenter.model.peopledata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum DateType {
	DATE("yyyy-MM-dd"),
	DATE_TIME("yyyy-MM-dd HH-mm-ss"),
	NULL(null);
	
	private DateFormat df;
	private DateType(String pattern) {
		if(pattern != null) {
			df = new SimpleDateFormat(pattern);
		}
	}
	
	public String format(Date date) {
		if(df != null && date != null) {
			return df.format(date);
		}
		return null;
	}
	
}
