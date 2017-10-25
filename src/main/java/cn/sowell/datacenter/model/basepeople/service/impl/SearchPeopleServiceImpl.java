package cn.sowell.datacenter.model.basepeople.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
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
	private TransportClient client = SearchTransportClient.getInstance().getTransportClient();
	private String index = "ydd";
	private String type = "peopleDemo";
	
	public JSONArray peopleSearch(String name,String idCode,String address,String content, PageInfo pageInfo){
		try {
			SearchRequestBuilder builder =
					client.prepareSearch(index)
							.setTypes(type)
							.setSearchType(SearchType.DEFAULT)
							.setFrom(0)
							.setSize(9);
			
			
			HighlightBuilder hiBuilder=new HighlightBuilder();
	        hiBuilder.preTags(" <span style=\"color:red\">");
	        hiBuilder.postTags("</span>");
	        hiBuilder.field("name")
	        	.field("address")
	        	.field("content");
	        
			BoolQueryBuilder qb = QueryBuilders.boolQuery();
			idCode = "*"+idCode+"*";
			qb = qb.should(QueryBuilders.matchPhraseQuery("name", name));//进行短语匹配
			qb = qb.must(new QueryStringQueryBuilder(idCode).field("idCode"));
			String addressList[] = address.split(" ");//将多个条件断开
			for(int i=0;i<addressList.length;i++){
				qb = qb.should(QueryBuilders.matchPhraseQuery("address", addressList[i]));
			}
			String contentList[] = content.split(" ");
			for(int i=0;i<contentList.length;i++){
				if(isNumeric(contentList[i])){//判断是否为数字
					String con = "*"+contentList[i]+"*";
					//qb = qb.must(new QueryStringQueryBuilder(con).field("content"));
				}				
				else
					qb = qb.should(QueryBuilders.matchPhraseQuery("content", contentList[i]));
			}
			builder.setQuery(qb);
			SearchResponse response ;
			response = builder.highlighter(hiBuilder).execute().actionGet();
			JSONArray  jsonThree = new JSONArray ();
			for(SearchHit hit:response.getHits()){
				jsonThree.add(hit.getSource());
				highLight(hit,"name");
				highLight(hit,"address");
				highLight(hit,"content");
				
				System.out.println(hit.getSource());
			}
			
			System.out.println("总数量："+response.getHits().getTotalHits()+" 耗时："+response.getTookInMillis());
			return jsonThree;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("Search failed");
			return null;
		}
		
	}
	
	public void createIndex(String index) {
	    try {
	    	client.admin().indices().prepareCreate(index).execute().actionGet();
	        
	    } catch (ElasticsearchException e) {
	        e.printStackTrace();
	    }
	}
	
	public void addMapping(String index, String type){
	    try {
	        // 使用XContentBuilder创建Mapping
	    	XContentBuilder builder=XContentFactory.jsonBuilder()
	                .startObject()
	                	.startObject("_all").field("type", "text").field("analyzer", "ik_smart").endObject()
	                	.startObject("properties").endObject()
	                .endObject();
	        PutMappingRequest mapping = Requests.putMappingRequest(index).type(type).source(builder);
	        client.admin().indices().putMapping(mapping).actionGet();
	        
	    } catch (ElasticsearchException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void closeIndex(){
		client.close();
		System.out.println("--索引关闭--");
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
	
	public void highLight(SearchHit hit,String key){//关键字高亮
		String con = "";
		if(hit.getHighlightFields().get(key)!=null){
			Text[] tContent= hit.getHighlightFields().get(key).fragments();
			hit.getSource().put(key, StringUtils.join(tContent));
		}
	}
	
	public boolean isNumeric(String str){ //判断是否为数字
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
	
}
