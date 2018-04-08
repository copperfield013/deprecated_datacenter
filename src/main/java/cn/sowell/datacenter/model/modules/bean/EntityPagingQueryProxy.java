package cn.sowell.datacenter.model.modules.bean;

import java.util.Set;

import cn.sowell.datacenter.model.abc.resolver.ModuleEntityPropertyParser;

public interface EntityPagingQueryProxy {
	int getTotalCount();
	int getPageSize();
	Set<ModuleEntityPropertyParser> load(int pageNo);
}
