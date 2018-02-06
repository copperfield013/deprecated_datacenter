package cn.sowell.datacenter.model.basepeople;

import java.util.LinkedHashSet;
import java.util.Set;

import com.abc.mapping.entity.Entity;
import com.abc.query.entity.impl.EntitySortedPagedQuery;

public class EntityQueryAdapter implements EntityPagingQueryProxy{

	private EntitySortedPagedQuery sortedPagedQuery;
	private int pageSize;
	
	
	public EntityQueryAdapter(EntitySortedPagedQuery sortedPagedQuery, Integer pageSize) {
		sortedPagedQuery.setPageSize(pageSize);
		this.sortedPagedQuery = sortedPagedQuery;
		this.pageSize = pageSize;
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
	public Set<Entity> load(int pageNo) {
		return new LinkedHashSet<Entity>(sortedPagedQuery.visit(pageNo));
	}

}
