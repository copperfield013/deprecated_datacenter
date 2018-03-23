package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RecordEntity;

import cn.sowell.copframe.spring.binder.ClassPropertyComposite;
import cn.sowell.copframe.spring.binder.FieldRefectUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.peopledata.DateType;
import cn.sowell.datacenter.model.peopledata.EntityElement;
import cn.sowell.datacenter.model.peopledata.EntityRecord;
import cn.sowell.datacenter.model.peopledata.EntityRelation;

public class EntityTransfer {
	
	private static final int TYPE_ATTRIBUTE = 0;
	private static final int TYPE_RECORD = 1;
	private static final int TYPE_RELATION = 2;
	Map<Class<?>, FieldRefectUtils<?>> map = new HashMap<Class<?>, FieldRefectUtils<?>>();
	
	Logger logger = Logger.getLogger(EntityTransfer.class);
	
	@SuppressWarnings("unchecked")
	<T> FieldRefectUtils<T> getFieldRefrectUtils(Class<T> clazz, Function<ClassPropertyComposite, String> propertyNameGetter){
		FieldRefectUtils<?> result = map.get(clazz);
		if(result == null){
			result = new FieldRefectUtils<T>(clazz, propertyNameGetter);
			map.put(clazz, result);
		}
		return (FieldRefectUtils<T>) result;
	}
	
	
	<T> FieldRefectUtils<T> getFieldRefrectUtils(Class<T> clazz){
		return getFieldRefrectUtils(clazz, property->{
			EntityElement anno = property.getFieldAnno(EntityElement.class);
			if(anno != null && TextUtils.hasText(anno.value())){
				return anno.value();
			}
			return property.getFieldName();
		});
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void bind(Entity source, Object target){
		if(source != null && target != null){
			FieldRefectUtils<?> frUtils = getFieldRefrectUtils(target.getClass());
			frUtils.iterateField((propName, composite) -> {
				//检测是否忽略该字段
				if(!checkIgnored(composite)){
					Object value = null;
					//检测字段类型
					int fieldType = checkFieldType(composite);
					try {
						switch (fieldType) {
							case TYPE_ATTRIBUTE:
								//转换一般字段
								value = transferNormalValue(source, propName, composite);
								break;
							case TYPE_RECORD:
							case TYPE_RELATION:
								List<RecordEntity> relations = null;
								relations = source.getRelations(propName);
								if(relations != null && relations.size() > 0){
									//检测字段要设置的值是否是集合，如果是集合，返回集合对象；如果不是集合，返回实例对象
									Object o = checkCollection(composite);
									if(o instanceof Collection){
										for (RecordEntity recordEntity : relations) {
											//实例化字段对象
											Object recordData = instantiateRecordData(composite);
											bind(recordEntity.getEntity(), recordData);
											((Collection) o).add(recordData);
										}
									}else{
										bind(relations.get(0).getEntity(), o);
									}
									value = o;
								}
								break;
						}
						
						composite.setValue(target, value);
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});
		}
	}
	/**
	 * 检测字段要设置的值是否是集合，如果是集合，返回集合对象；如果不是集合，返回实例对象
	 * @param composite
	 * @return
	 */
	private Object checkCollection(ClassPropertyComposite composite) {
		Class<?> fieldType = composite.getFieldType();
		if(Collection.class.isAssignableFrom(fieldType)){
			if(fieldType.isInterface()){
				if(List.class.isAssignableFrom(fieldType)){
					return new ArrayList<Object>();
				}else if(Set.class.isAssignableFrom(fieldType)){
					return new LinkedHashSet<Object>();
				}
			}else{
				return BeanUtils.instantiate(fieldType);
			}
		}else{
			return instantiateRecordData(composite);
		}
		return null;
	}
	/**
	 * 实例化对象
	 * @param composite
	 * @return
	 */
	private Object instantiateRecordData(ClassPropertyComposite composite) {
		Class<?> fieldType = composite.getFieldType();
		EntityRecord recordAnno = composite.getFieldAnno(EntityRecord.class);
		EntityRelation relationAnno = composite.getFieldAnno(EntityRelation.class);
		if(recordAnno != null && !Object.class.equals(recordAnno.elementClass())){
			fieldType = recordAnno.elementClass();
		}else if(relationAnno != null && !Object.class.equals(recordAnno.elementClass())){
			fieldType = relationAnno.elementClass();
		}
		return BeanUtils.instantiate(fieldType);
	}

	/**
	 * 转换一般字段
	 * @param source 
	 * @param propName
	 * @param composite
	 * @return
	 */
	private Object transferNormalValue(Entity source, String propName,
			ClassPropertyComposite composite) {
		Class<?> fieldType = composite.getFieldType();
		EntityElement anno = composite.getFieldAnno(EntityElement.class);
		if(fieldType.equals(String.class)){
			if(anno != null){
				if(DateType.DATE.equals(anno.dateType())){
					return source.getDateStringValue(propName);
				}else if(DateType.DATE_TIME.equals(anno.dateType())){
					return source.getDateTimeStringValue(propName);
				}
			}
			return source.getStringValue(propName);
		}else if(fieldType.equals(Integer.class) || fieldType.equals(Integer.TYPE)){
			return source.getIntegerValue(propName);
		}else if(fieldType.equals(Long.class) || fieldType.equals(Long.TYPE)){
			return source.getLongValue(propName);
		}else if(Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)){
			return source.getFloatValue(propName);
		}else if(Date.class.equals(fieldType)){
			return source.getDateValue(propName);
		}else{
			return null;
		}
		
	}

	/**
	 * 检测字段类型,类型包括一般字段、record字段和relation字段
	 * @param composite
	 * @return
	 */
	private int checkFieldType(ClassPropertyComposite composite) {
		EntityRecord annoRecord = composite.getFieldAnno(EntityRecord.class);
		EntityRelation annoRelation = composite.getFieldAnno(EntityRelation.class);
		if(annoRecord != null && annoRelation != null){
			throw new RuntimeException("字段不能同时配置EntityRecord注解和EntityRelation注解");
		}else if(annoRecord != null){
			return TYPE_RECORD;
		}else if(annoRelation != null){
			return TYPE_RELATION;
		}else{
			return TYPE_ATTRIBUTE;
		}
	}
	private boolean checkIgnored(ClassPropertyComposite composite) {
		EntityElement anno = composite.getFieldAnno(EntityElement.class);
		return anno != null && anno.readIgnored();
	}
	
	
	
	
	
	/**
	 * 
	 * @param pojo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Entity bindData(Object pojo, Entity target){
		if(pojo != null){
			Entity existEntity = getExistEntity(pojo);
			if(existEntity != null) {
				return existEntity;
			}else {
				putExistsEntity(pojo, target);
			}
			getFieldRefrectUtils(pojo.getClass()).iterateField((propName, composite)->{
				if(!checkIgnored(composite)){
					try {
						//获得字段值
						Object value = composite.getValue(pojo);
						int fieldType = checkFieldType(composite);
						switch (fieldType) {
						case TYPE_ATTRIBUTE:
							//将普通字段值转换成entity可接受到的值
							Object entityValue = transferNormalEntityValue(value, composite);
							target.putValue(propName, entityValue);
							break;
						case TYPE_RELATION:
							EntityRelation annoRelation = composite.getFieldAnno(EntityRelation.class);
							//如果是集合的话，那么要对集合的元素进行转换
							if(value instanceof Collection) {
								for (Object ele : (Collection<Object>)value) {
									//构造集合内的一个元素对应的entity
									//将pojo的数据绑定到entity
									Entity rEntity = bindData(ele, new Entity(annoRelation.entityName()));
									target.putRelationEntity(propName, annoRelation.domainName(), rEntity);
								}
							}else {
								Entity rEntity = bindData(value, new Entity(annoRelation.entityName()));
								target.putRelationEntity(propName, annoRelation.domainName(), rEntity);
							}
							break;
						default:
							break;
						}
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});
			return target;
		}else {
			return null;
		}
	}

	
	/**
	 * 将普通字段的值转换成entity对象中可接受的字段值
	 * @param value
	 * @param composite
	 * @return
	 */
	private Object transferNormalEntityValue(Object value, ClassPropertyComposite composite) {
		if(value != null) {
			Class<?> clazz = value.getClass();
			EntityElement anno = composite.getFieldAnno(EntityElement.class);
			if(String.class.equals(clazz)) {
				return value;
			}else if(Date.class.isAssignableFrom(clazz)) {
				if(anno == null || DateType.NULL.equals(anno.dateType())) {
					return DateType.DATE.format((Date) value); 
				}else {
					return anno.dateType().format((Date) value);
				}
			}else {
				return FormatUtils.toString(value.toString());
			}
		}else {
			return null;
		}
	}



	Map<Thread, Map<Object, Entity>> entityPojoEntityMap = new HashMap<Thread, Map<Object, Entity>>();
	
	public void bind(Object pojo, Entity target) {
		bindData(pojo, target);
		entityPojoEntityMap.remove(Thread.currentThread());
	}
	
	private Entity getExistEntity(Object pojo) {
		Map<Object, Entity> peMap = entityPojoEntityMap.get(Thread.currentThread());
		if(peMap != null) {
			return peMap.get(pojo);
		}
		return null;
	}
	
	private void putExistsEntity(Object pojo, Entity target) {
		Map<Object, Entity> peMap = entityPojoEntityMap.get(Thread.currentThread());
		if(peMap == null) {
			peMap = new HashMap<Object, Entity>();
			entityPojoEntityMap.put(Thread.currentThread(), peMap);
		}
		peMap.put(pojo, target);
	}

}
