package cn.sowell.datacenter.common.property;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.Assert;

public class PropertyParser implements Map<String, Object>{
	private Object obj;
	private ExpressionParser parser;
	private PropertyValueTranslatorSet translatorSet;
	
	public PropertyParser(Object obj, ExpressionParser parser, PropertyValueTranslatorSet translators) {
		Assert.notNull(parser);
		Assert.notNull(translators);
		this.obj = obj;
		this.parser = parser;
		this.translatorSet = translators;
	}
	
	
	public Object getPropertyValue(String expEl){
		Expression exp = parser.parseExpression(expEl);
		GetPropertyValueComposite composite = new DefaultGetPropertyValueComposite(obj, exp);
		PropertyValueTranslator translator = translatorSet.getTranslator(composite);
		return translator != null? translator.getValue(composite): null;
	}
	

	public void setPropertyValue(String expEl, Object value){
		Expression exp = parser.parseExpression(expEl);
		SetPropertyValueComposite composite = new DefaultSetPropertyValueComposite(obj, exp, value);
		PropertyValueTranslator translator = translatorSet.getTranslator(composite);
		if(translator != null){
			translator.setValue(composite);
		}
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
