package cn.sowell.datacenter.model.modules.bean;

import java.util.Set;

import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;

public interface EntityPagingQueryProxy {
	int getTotalCount();
	int getPageSize();
	Set<ModuleEntityPropertyParser> load(int pageNo);
}
