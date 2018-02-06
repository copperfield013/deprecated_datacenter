package cn.sowell.datacenter.model.basepeople;

import java.util.Set;

import com.abc.mapping.entity.Entity;

public interface EntityPagingQueryProxy {
	int getTotalCount();
	int getPageSize();
	Set<Entity> load(int pageNo);
}
