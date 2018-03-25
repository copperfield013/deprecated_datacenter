package cn.sowell.datacenter.model.modules.bean;

import java.util.Set;

import cn.sowell.datacenter.model.modules.EntityPropertyParser;

public interface EntityPagingQueryProxy {
	int getTotalCount();
	int getPageSize();
	Set<EntityPropertyParser> load(int pageNo);
}
