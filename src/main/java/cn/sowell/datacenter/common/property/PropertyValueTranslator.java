package cn.sowell.datacenter.common.property;


/**
 * 
 * <p>Title: PropertyValueTranslator</p>
 * <p>Description: </p><p>
 * 字段转换器接口。
 * 该对象可以将符合条件的字段用特定的方法转换成另一个字段值，用于该字段值的获取和设置。
 * </p>
 * @author Copperfield Zhang
 * @date 2017年12月28日 下午4:07:58
 */
public interface PropertyValueTranslator{
	/**
	 * 读取字段时是否允许被转换。
	 * 返回true时，parser将会调用{@link #getValue(GetPropertyValueComposite)}方法将其转换成指定对象
	 * 返回false时，该转换器不会处理该字段
	 * @param composite
	 * @return
	 */
	boolean canGet(GetPropertyValueComposite composite);
	/**
	 * 设置字段值时是否允许被转换。
	 * 返回true是，parser将会调用{@link #setValue(SetPropertyValueComposite)}方法处理该字段值
	 * @param composite
	 * @return
	 */
	boolean canSet(SetPropertyValueComposite composite);
	/**
	 * 转换字段值。该方法不会改变原对象中的字段值，只会在parser调用时，转换原字段值并返回
	 * @param composite
	 * @return
	 */
	Object getValue(GetPropertyValueComposite composite);
	/**
	 * 转换要设置的字段值，并且在该方法中手动将转换后的值设置到字段中
	 * @param composite
	 */
	void setValue(SetPropertyValueComposite composite);
	
}
