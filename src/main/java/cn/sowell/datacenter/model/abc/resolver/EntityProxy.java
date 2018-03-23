package cn.sowell.datacenter.model.abc.resolver;

import com.abc.mapping.entity.SimpleEntity;

public interface EntityProxy {

	void putValue(String propName, Object val);

	SimpleEntity getEntity();

	EntityProxy createEmptyEntity();

	default boolean preprocessValue(String propName, Object propValue) {
		return true;
	}
}
