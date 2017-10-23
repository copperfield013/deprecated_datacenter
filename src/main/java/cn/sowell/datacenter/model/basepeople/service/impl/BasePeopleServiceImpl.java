package cn.sowell.datacenter.model.basepeople.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.dao.BasePeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;



@Service
public class BasePeopleServiceImpl implements BasePeopleService{


	private Logger logger = Logger.getLogger(BasePeopleService.class);
	@Resource
	BasePeopleDao basePeopleDao;
	
	private TransportClient client;
	
	@Override
	public List<BasePeople> queryList(BasePeopleCriteria criteria, PageInfo pageInfo) {
		return basePeopleDao.queryList(criteria, pageInfo);
	}
	
	@Override
	public void delete(Long id){
		BasePeople p = new BasePeople();
		p.setId(id);
		basePeopleDao.delete(p);
	}

	@Override
	public void create(BasePeople people) {
		basePeopleDao.insert(people);
		
	}

	@Override
	public BasePeople getPeople(Long id) {
		// TODO Auto-generated method stub
		return basePeopleDao.get(BasePeople.class, id);
	}

	@Override
	public void update(BasePeople people) {
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
			SearchRequestBuilder builder =
					client.prepareSearch("ydd")
							.setTypes("demo")
							.setSearchType(SearchType.DEFAULT)
							.setFrom(0)
							.setSize(9);
			BoolQueryBuilder qb = QueryBuilders.boolQuery();
			String titleList[] = title.split(" ");//将多个条件断开
			for(int i=0;i<titleList.length;i++){
				qb = qb.should(QueryBuilders.matchPhraseQuery("title", titleList[i]));//进行短语匹配
			}
			//QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", title);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("naifeitian creat failed");
			return null;
		}

	}

}
