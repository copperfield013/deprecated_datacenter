package cn.sowell.datacenter.common.property.translator;


import java.util.Date;

import javax.annotation.Resource;

import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.common.property.GetPropertyValueComposite;
import cn.sowell.datacenter.common.property.SetPropertyValueComposite;

/**
 * 
 * <p>Title: DatePropertyValueTranlator</p>
 * <p>Description: </p><p>
 * 日期字段转换器
 * </p>
 * @author Copperfield Zhang
 * @date 2017年12月28日 下午4:41:35
 */
public class DatePropertyValueTranlator extends AbstractPropertyValueTranslator{

	@Resource
	FrameDateFormat dateFormat;
	
	@Override
	public boolean canGet(GetPropertyValueComposite composite) {
		return Date.class.isAssignableFrom(composite.getPropertyType());
	}

	@Override
	public boolean canSet(SetPropertyValueComposite composite) {
		return Date.class.isAssignableFrom(composite.getPropertyType()) 
				&& !composite.isPropertyNull() 
				&& composite.getToSetValue() instanceof String;
	}
	
	@Override
	public Object getValue(GetPropertyValueComposite composite) {
		return dateFormat.formatDate((Date) composite.getPropertyValue());
	}

	@Override
	public void setValue(SetPropertyValueComposite composite) {
		String value = (String) composite.getToSetValue();
		Date date = dateFormat.parse(value);
		composite.setValueByExpression(date);
	}

	protected void setDateFormat(FrameDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

}
