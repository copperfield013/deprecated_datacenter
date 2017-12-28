package cn.sowell.datacenter.common.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.sowell.datacenter.common.property.translator.DefaultPropertyValueTranslator;
/**
 * 
 * <p>Title: PropertyValueTranslatorSet</p>
 * <p>Description: </p><p>
 * 字段转换器集合对象。<br/>
 * 该对象可以按顺序存放多个转换器，默认已经有一个{@link DefaultPropertyValueTranslator}对象。<br/>
 * parser在转换时，会从对应的转换器集合对象中，按照存放顺序的先后，倒序获得第一个匹配的转换器，并使用该转换器对字段进行转换。<br/>
 * </p>
 * @author Copperfield Zhang
 * @date 2017年12月28日 下午4:14:50
 */
public class PropertyValueTranslatorSet{
	
	private LinkedHashSet<PropertyValueTranslator> translatorSet;
			
	/**
	 * 转换器集合默认构造函数。<br/>
	 * 默认起始状态下放置一个{@link DefaultPropertyValueTranslator}对象
	 */
	public PropertyValueTranslatorSet() {
		translatorSet = new LinkedHashSet<PropertyValueTranslator>();
		translatorSet.add(new DefaultPropertyValueTranslator());
	}
	
	/**
	 * 获得当前的所有转换器（备份）
	 * @return
	 */
	public Set<PropertyValueTranslator> getTranslatorSet() {
		return new LinkedHashSet<PropertyValueTranslator>(translatorSet);
	}
	
	/**
	 * 添加多个转换器（如果转换器原本已经存在，那么顺序将被刷新）
	 * @param translatorSet
	 */
	public void setTranslatorSet(
			LinkedHashSet<PropertyValueTranslator> translatorSet) {
		this.translatorSet.addAll(translatorSet);
	}
	
	/**
	 * 传入字段对象，转换器集合将匹配到最新的符合转换条件的转换器，并返回
	 * @param composite
	 * @return
	 */
	public PropertyValueTranslator getTranslator(
			PropertyValueComposite composite) {
		boolean isGet = composite instanceof GetPropertyValueComposite,
				isSet = composite instanceof SetPropertyValueComposite;
		if(isGet || isSet){
			//倒序
			ArrayList<PropertyValueTranslator> array = new ArrayList<PropertyValueTranslator>(translatorSet);
			Collections.reverse(array);
			//遍历所有转换器
			for (PropertyValueTranslator translator : array) {
				if(isGet && translator.canGet((GetPropertyValueComposite) composite)){
					return translator;
				}else if(isSet && translator.canSet((SetPropertyValueComposite) composite)){
					return translator;
				}
			}
		}
		return null;
	}
	
	/**
	 * 集合中添加一个转换器对象
	 * @param translator
	 * @return
	 */
	public PropertyValueTranslatorSet add(PropertyValueTranslator translator){
		if(translator != null){
			this.translatorSet.add(translator);
		}
		return this;
	}
	
}
