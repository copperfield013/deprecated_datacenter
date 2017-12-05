package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.Assert;

public class PropertyParser implements Map<String, Object>{
	private Object obj;
	private ExpressionParser parser;
	
	public PropertyParser(Object obj, ExpressionParser parser) {
		Assert.notNull(parser);
		this.obj = obj;
		this.parser = parser;
	}
	
	public Object getPropertyValue(String expEl){
		Expression exp = parser.parseExpression(expEl);
		return exp.getValue(obj);
	}
	
	public void setPropertyValue(String expEl,Object value){
		Expression exp = parser.parseExpression(expEl);
		exp.setValue(obj, value);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object get(Object key) {
		return getPropertyValue((String) key);
	}

	@Override
	public Object put(String key, Object value) {
		setPropertyValue(key,value);
		return null;
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Object> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
