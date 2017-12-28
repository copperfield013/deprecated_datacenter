package cn.sowell.datacenter.common.property;

import org.springframework.expression.Expression;
import org.springframework.util.Assert;

public abstract class AbstractPropertyValueComposite implements PropertyValueComposite {

	private Object target;
	private Expression expression;
	
	
	public AbstractPropertyValueComposite(Object target, Expression expression) {
		super();
		Assert.notNull(target);
		Assert.notNull(expression);
		this.target = target;
		this.expression = expression;
	}

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public String getPropertyName() {
		return expression.getExpressionString();
	}

	@Override
	public Expression getPropertyExpression() {
		return expression;
	}

	@Override
	public Object getPropertyValue() {
		return expression.getValue(this.getTarget());
	}

	@Override
	public Class<?> getPropertyType() {
		return expression.getValueType(this.getTarget());
	}
	
	@Override
	public boolean isPropertyNull() {
		return getPropertyValue() == null;
	}

}
