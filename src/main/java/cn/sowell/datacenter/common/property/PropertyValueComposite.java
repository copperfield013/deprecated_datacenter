package cn.sowell.datacenter.common.property;

import org.springframework.expression.Expression;

/**
 * 
 * <p>Title: PropertyValueComposite</p>
 * <p>Description: </p><p>
 * 对象属性接口。实现该接口的对象将可以获得要处理的字段的相关信息
 * </p>
 * @author Copperfield Zhang
 * @date 2017年12月28日 下午4:04:06
 */
public interface PropertyValueComposite {

	/**
	 * 字段根对象
	 * @return
	 */
	Object getTarget();
	
	/**
	 * 获得字段的表达式
	 * @return
	 */
	String getPropertyName();
	
	/**
	 * 获得字段获取的表达式对象
	 * @return
	 */
	Expression getPropertyExpression();
	
	
	/**
	 * 获得原始字段值
	 * @return
	 */
	Object getPropertyValue();
	
	/**
	 * 获得字段的类型
	 * @return
	 */
	Class<?> getPropertyType();
	
	/**
	 * 当前字段值是否为空
	 * @return
	 */
	boolean isPropertyNull();
}
