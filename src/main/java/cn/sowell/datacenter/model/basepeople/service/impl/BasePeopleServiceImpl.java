package cn.sowell.datacenter.model.basepeople.service.impl;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.sowell.datacenter.model.basepeople.BasePeopleDictionaryCriteria;
import cn.sowell.datacenter.model.basepeople.dto.FieldDataDto;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleItemEntity;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.BasePeopleCriteria;
import cn.sowell.datacenter.model.basepeople.SearchTransportClient;
import cn.sowell.datacenter.model.basepeople.dao.BasePeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.BasePeople;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;



@Service
public class BasePeopleServiceImpl implements BasePeopleService{


	private Logger logger = Logger.getLogger(BasePeopleService.class);
	@Resource
	BasePeopleDao basePeopleDao;
	
	private TransportClient client = SearchTransportClient.getInstance().getTransportClient();
	
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
	public BasePeople getPeopleById(Long id) {
		// TODO Auto-generated method stub
		return basePeopleDao.get(BasePeople.class, id);
	}

	@Override
	public void update(BasePeople people) {
		basePeopleDao.update(people);
		
	}

	@Override
	public JSONArray titleSearchByEs(String title) {

	    try {
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
			
			builder.setQuery(qb);
			SearchResponse response ;
			response = builder.execute().actionGet();
			JSONArray  jsonThree = new JSONArray ();
			for(SearchHit hit:response.getHits()){
				jsonThree.add(hit.getSource());
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

	@Override
	public List<FieldDataDto> queryFieldList(PageInfo pageInfo) {
		SearchRequestBuilder builder =
				client.prepareSearch("ydd")
						.setTypes("demo")
						.setSearchType(SearchType.DEFAULT)
						.setFrom(0)
						.setSize(50)
				;
		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
		builder.setQuery(queryBuilder);
		SearchResponse response  = builder.addSort("id",SortOrder.ASC).execute().actionGet();

		List<FieldDataDto> list = new ArrayList<>();
		for(SearchHit hit:response.getHits()){
			list.add(new FieldDataDto(hit.getSource().get("id").toString(),
					hit.getSource().get("title").toString(),
					hit.getSource().get("title_en").toString(),
					hit.getSource().get("type").toString(),
					hit.getSource().get("check_rule").toString()));

		//	System.out.println("总数量："+response.getHits().getTotalHits()+" 耗时："+response.getTookInMillis());
		}
		return  list;
	}

	@Override
	public void addField(FieldDataDto field) {
		try {
           long stime = System.currentTimeMillis();
           boolean flag = false;//设置检查标记

           Map<String, Object> json = new LinkedHashMap<String, Object>();
           ObjectMapper ob = new ObjectMapper();
           json.put("id", field.getId());
           json.put("title", field.getTitle());
           json.put("title_en", field.getTitle_en());
           json.put("type", field.getType());
           json.put("check_rule",field.getCheck_rule());

			String json1 = ob.writeValueAsString(json);
			IndexResponse response = client
					.prepareIndex("ydd", "demo", field.getId())
					.setSource(json1).execute().actionGet();
                long etime = System.currentTimeMillis();
                System.out.println(etime - stime);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());

		}
	}

	@Override
	public List<TBasePeopleItemEntity> FieldList(String field) {
		return basePeopleDao.fieldList(field);
	}

	@Override
	public FieldDataDto queryFieldById(String FieldId) {
		GetResponse response = client
				.prepareGet("ydd", "demo",FieldId)
				.execute().actionGet();
		return new FieldDataDto(response.getSource().get("id").toString(),
				response.getSource().get("title").toString(),
				response.getSource().get("title_en").toString(),
				response.getSource().get("type").toString(),
				response.getSource().get("check_rule").toString());
	}

	@Override
	public List<TBasePeopleDictionaryEntity> querydicList(BasePeopleDictionaryCriteria criteria, PageInfo pageInfo) {
		return basePeopleDao.querydicList(criteria, pageInfo);
	}

	@Override
	public void updateBasePeople(Map<String, String> map, String id) {
		basePeopleDao.updateBasePeople(map,id);
	}


}