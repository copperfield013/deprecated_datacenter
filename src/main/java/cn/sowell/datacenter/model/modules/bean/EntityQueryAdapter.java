package cn.sowell.datacenter.model.modules.bean;

import java.util.List;
import java.util.Set;

import com.abc.mapping.entity.Entity;
import com.abc.query.entity.impl.EntitySortedPagedQuery;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.datacenter.model.abc.resolver.EntityPropertyParser;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfigResolver;

public class EntityQueryAdapter implements EntityPagingQueryProxy{

	private EntitySortedPagedQuery sortedPagedQuery;
	private int pageSize;
	private FusionContextConfigResolver resolver;
	
	
	
	public EntityQueryAdapter(EntitySortedPagedQuery sortedPagedQuery, 
			FusionContextConfigResolver fusionContextConfigResolver, Integer pageSize) {
		Assert.notNull(fusionContextConfigResolver);
		sortedPagedQuery.setPageSize(pageSize);
		this.sortedPagedQuery = sortedPagedQuery;
		this.pageSize = pageSize;
		this.resolver = fusionContextConfigResolver;
	}

	@Override
	public int getTotalCount() {
		return sortedPagedQuery.getAllCount();
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public Set<EntityPropertyParser> load(int pageNo) {
		List<Entity> entities = sortedPagedQuery.visit(pageNo);
		return CollectionUtils.toSet(entities, entity->resolver.createParser(entity));
	}

}
