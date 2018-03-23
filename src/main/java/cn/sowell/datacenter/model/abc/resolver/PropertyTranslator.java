package cn.sowell.datacenter.model.abc.resolver;

public interface PropertyTranslator<T, V> {
	boolean check(T propValue);
	V transfer(T propValue);

}
