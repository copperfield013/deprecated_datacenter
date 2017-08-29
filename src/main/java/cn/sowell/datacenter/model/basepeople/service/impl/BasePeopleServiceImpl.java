package cn.sowell.datacenter.model.basepeople.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.dao.BasePeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.People;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;

@Service
public class BasePeopleServiceImpl implements BasePeopleService{

	@Resource
	BasePeopleDao basePeopleDao;
	
	private TransportClient client;
	
	@Override
	public List<People> queryList(BasePeopleCriteria criteria, PageInfo pageInfo) {
		return basePeopleDao.queryList(criteria, pageInfo);
	}
	
	@Override
	public void delete(Long id){
		People p = new People();
		p.setId(id);
		basePeopleDao.delete(p);
	}

	@Override
	public void create(People people) {
		basePeopleDao.insert(people);
		
	}

	@Override
	public People getPeople(Long id) {
		// TODO Auto-generated method stub
		return basePeopleDao.get(People.class, id);
	}

	@Override
	public void update(People people) {
		basePeopleDao.update(people);
		
	}

	@Override
	public JSONArray titleSearchByEs(String title) {
		
		//设置集群名称 5.x
	    Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
	    //创建client
	    try {
			client = new PreBuiltTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SearchRequestBuilder builder = 
				client.prepareSearch("ydd")
				.setTypes("demo")
				.setSearchType(SearchType.DEFAULT)
				.setFrom(0)
				.setSize(9);  
	    BoolQueryBuilder qb ;
	    qb = QueryBuilders.boolQuery()
	    		.should(new QueryStringQueryBuilder(title).field("title"));
	    builder.setQuery(qb);  
	    SearchResponse response ;
	    response = builder.execute().actionGet();
	    JSONArray  jsonThree = new JSONArray ();
	    for(SearchHit hit:response.getHits()){
	        String source=hit.getSourceAsString();
		    jsonThree.add((hit.getSource()));
	        
	    }
	    
	    System.out.println(jsonThree);
	    System.out.println("总数量："+response.getHits().getTotalHits()+" 耗时："+response.getTookInMillis());
		return jsonThree;
	}

}
