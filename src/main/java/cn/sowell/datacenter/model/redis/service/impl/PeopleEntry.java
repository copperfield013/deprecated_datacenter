package cn.sowell.datacenter.model.redis.service.impl;

import java.io.Serializable;

import cn.sowell.copframe.jedica.CommonRedisEntry;

import com.abc.record.impl.Record;

public class PeopleEntry extends CommonRedisEntry<Record> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3534925390297215809L;
	private String key;
	
	public PeopleEntry(String key, Record record) {
		this.key = key;
		super.value = record;
	}
	
	@Override
	public String getKey() {
		return key;
	}
	
}
