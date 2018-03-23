package cn.sowell.datacenter.model.abc.resolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sowell.copframe.utils.FormatUtils;

public class PropertyNamePartitions {
	
	static Pattern pattern = Pattern.compile("^([^\\[\\]\\s]+)(\\[(\\d+)\\])?$");
	
	private String mainPartition;
	private Integer index;
	private int defaultIndex = 0;
	
	public PropertyNamePartitions(String propName) {
		Matcher matcher = pattern.matcher(propName);
		if(matcher.matches()) {
			mainPartition = matcher.group(1);
			index = FormatUtils.toInteger(matcher.group(3));
		}else {
			throw new RuntimeException("字段名称格式不正确[" + propName + "]");
		}
	}

	public String getMainPartition() {
		return mainPartition;
	}

	public Integer getIndex() {
		return index == null? getDefaultIndex(): index;
	}

	public int getDefaultIndex() {
		return defaultIndex;
	}

	public void setDefaultIndex(int defaultIndex) {
		this.defaultIndex = defaultIndex;
	}

}

