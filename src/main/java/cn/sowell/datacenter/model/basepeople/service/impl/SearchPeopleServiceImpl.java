package cn.sowell.datacenter.model.basepeople.service.impl;

import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.SearchTransportClient;
import cn.sowell.datacenter.model.basepeople.service.SearchPeopleService;

@Service
public class SearchPeopleServiceImpl implements SearchPeopleService{	
	private Logger logger = Logger.getLogger(SearchPeopleService.class);
	private TransportClient client;
	private String index = "ydd";
	private String type = "peopleDemo";
	
	public JSONArray peopleSearch(String name,String idCode,String address,String content, PageInfo pageInfo){
		//设置集群名称 5.x
	    Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
	    //创建client
	    try {
			client = new PreBuiltTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
			SearchRequestBuilder builder =
					client.prepareSearch(index)
							.setTypes(type)
							.setSearchType(SearchType.DEFAULT)
							.setFrom(0)
							.setSize(9);
			
			
			HighlightBuilder hiBuilder=new HighlightBuilder();
	        hiBuilder.preTags(" <span style=\"color:red\">");
	        hiBuilder.postTags("</span>");
	        hiBuilder.field("name");
	        hiBuilder.field("address");
	        
			BoolQueryBuilder qb = QueryBuilders.boolQuery();
			idCode = "*"+idCode+"*";
			qb = qb.should(QueryBuilders.matchPhraseQuery("name", name));//进行短语匹配
			qb = qb.must(new QueryStringQueryBuilder(idCode.trim()).field("idcode"));
			qb = qb.should(QueryBuilders.matchPhraseQuery("address", address.trim()));
			qb = qb.should(QueryBuilders.matchPhraseQuery("content", content.trim()));
			builder.setQuery(qb);
			
			
	        
			SearchResponse response ;
			response = builder.highlighter(hiBuilder).execute().actionGet();
			JSONArray  jsonThree = new JSONArray ();
			for(SearchHit hit:response.getHits()){
				//String source=hit.getSourceAsString();
				jsonThree.add(hit.getSource());
				System.out.println(hit.getHighlightFields());
				System.out.println(hit.getHighlightFields().get("address"));				
				String sName = "";
				String sAddress = "";
				if(hit.getHighlightFields().get("name")!=null){
					Text[] tName= hit.getHighlightFields().get("name").fragments();
					for (Text str : tName) {
						sName+=str.string();
		            }
					hit.getSource().put("name", sName);
				}
				if(hit.getHighlightFields().get("address")!=null){
					Text[] tAddress= hit.getHighlightFields().get("address").fragments();
					for (Text str : tAddress) {
						sAddress+=str.string();
		            }
					hit.getSource().put("address", sAddress);
				}
				

			}
			System.out.println(jsonThree);
			System.out.println("总数量："+response.getHits().getTotalHits()+" 耗时："+response.getTookInMillis());
			return jsonThree;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("Search failed");
			return null;
		}
		
	}
	
	public JSONArray eSearchTitle(String title) {

	    try {
			SearchRequestBuilder builder =
					client.prepareSearch(index)
							.setTypes(type)
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
				String source=hit.getSourceAsString();
				jsonThree.add((hit.getSource()));

			}
			System.out.println(jsonThree);
			System.out.println("总数量："+response.getHits().getTotalHits()+" 耗时："+response.getTookInMillis());
			return jsonThree;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("Search failed");
			return null;
		}

	}

	public void eSearchUpdate(JSONObject json,String id){		 
	    IndexResponse response = client
	    		.prepareIndex(index,type,id)
	    		.setSource(json).execute().actionGet();
	    System.out.println("update success");
	}
	
	
	public void eSearchDelete(String id){
		DeleteResponse response = client
				.prepareDelete(index,type,id) 
	    		.execute().actionGet();
		System.out.println("delete success");
		
	}
	
	public void eSearchGet(String id){
		GetResponse response = client
				.prepareGet(index,type, id)
				.execute().actionGet();
		System.out.println("get success");
	}
	
	
}
