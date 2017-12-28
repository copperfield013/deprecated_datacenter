package cn.sowell.datacenter.common.property;

import org.springframework.expression.Expression;

public class DefaultSetPropertyValueComposite extends AbstractPropertyValueComposite implements
		SetPropertyValueComposite {
	private Object toSetValue;

	public DefaultSetPropertyValueComposite(Object target, Expression expression, Object toSetValue) {
		super(target, expression);
		this.toSetValue = toSetValue;
	}

	@Override
	public Object getToSetValue() {
		return toSetValue;
	}

	@Override
	public Class<?> getToSetValueType() {
		return toSetValue == null? null: toSetValue.getClass();
	}

	@Override
	public boolean toSetValueIsNull() {
		return toSetValue == null;
	}

	@Override
	public void setValueByExpression(Object value) {
		Expression expression = getPropertyExpression();
		if(expression != null){
			expression.setValue(this.getTarget(), value);
		}
	}

	
	
}
