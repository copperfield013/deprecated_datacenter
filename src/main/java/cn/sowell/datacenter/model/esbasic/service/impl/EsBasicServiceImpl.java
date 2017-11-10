package cn.sowell.datacenter.model.esbasic.service.impl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.sowell.datacenter.model.esbasic.SearchTransportClient;
import cn.sowell.datacenter.model.esbasic.service.EsBasicService;

@Service
public class EsBasicServiceImpl implements EsBasicService {
	private Logger logger = Logger.getLogger(EsBasicService.class);
	private TransportClient client = SearchTransportClient.getInstance().getTransportClient();
	private String index = "ydd";
	private String type = "peopleDemo";
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
	
	public Map<String, Object> eSearchGet(String id){
		GetResponse response = client
				.prepareGet(index,type, id)
				.execute().actionGet();
		System.out.println(response.getSource());
		return response.getSource();		
	}

}
