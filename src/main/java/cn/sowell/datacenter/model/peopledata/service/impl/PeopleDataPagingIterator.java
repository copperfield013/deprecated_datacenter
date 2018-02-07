package cn.sowell.datacenter.model.peopledata.service.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;

public class PeopleDataPagingIterator implements Iterator<PeopleData>{
	//对象静态属性
	
	//所有数据的条数
	private final int totalCount;
	//当前迭代器可以查出的最多的数据条数
	private final int dataCount;
	//获取对应页码的所有数据的方法
	private final Function<Integer, Set<PeopleData>> dataGetter;
	//起始页码
	private final int startPageNo;
	//起始需要忽略的数据条数
	private final int ignoreDataCount;
	
	//对象可变属性
	private Iterator<PeopleData> cacheItr;
	private int current = 0;
	private int pageNoInc = 0;
	private int hasIgnored = 0;
	
	
	public PeopleDataPagingIterator(
			int totalCount,
			int dataCount,
			int ignoreDataCount,
			int startPageNo,
			Function<Integer, Set<PeopleData>> dataGetter) {
		super();
		this.totalCount = totalCount;
		this.dataCount = dataCount;
		this.ignoreDataCount = ignoreDataCount;
		this.startPageNo = startPageNo;
		this.dataGetter = dataGetter;
	}

	@Override
	public boolean hasNext() {
		return current < dataCount;
	}

	
	private long lastCacheDataTime = System.currentTimeMillis();
	private float speed = 0;
	@Override
	public synchronized PeopleData next() {
		if(hasNext() && (cacheItr == null || !cacheItr.hasNext())){
			Set<PeopleData> set = dataGetter.apply(startPageNo + (pageNoInc++));
			if(set != null){
				cacheItr = set.iterator();
				long currentTime = System.currentTimeMillis();
				speed = set.size() / ((float)(currentTime - lastCacheDataTime) / 1000);
				lastCacheDataTime = currentTime;;
			}
		}
		if(cacheItr != null && cacheItr.hasNext()){
			if(ignoreDataCount > hasIgnored){
				hasIgnored++;
				cacheItr.next();
				return this.next();
			}
			current++;
			return cacheItr.next();
		}else{
			return null;
		}
	}
	
	public int getTotalCount(){
		return totalCount;
	}
	
	public int getCurrent(){
		return current;
	}

	public int getDataCount() {
		return dataCount;
	}

	/**
	 * 调用next方法的速度，单位条/秒
	 * @return
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * 根据速度计算获得剩余的条数所需要的时间
	 * @return
	 */
	public float getRemainSecond() {
		if(speed > 0){
			return (dataCount - current) / speed;
		}
		return 0;
	}
	
	

}
