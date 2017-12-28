package cn.sowell.datacenter.common.property.translator;

import cn.sowell.datacenter.common.property.GetPropertyValueComposite;
import cn.sowell.datacenter.common.property.SetPropertyValueComposite;

public class DefaultPropertyValueTranslator extends AbstractPropertyValueTranslator{

	@Override
	public boolean canGet(GetPropertyValueComposite composite) {
		return true;
	}

	@Override
	public boolean canSet(SetPropertyValueComposite composite) {
		return true;
	}

	@Override
	public Object getValue(GetPropertyValueComposite composite) {
		return composite.getPropertyValue();
	}

	@Override
	public void setValue(SetPropertyValueComposite composite) {
		composite.setValueByExpression(composite.getToSetValue());
	}

	
	
	

}
