package cn.sowell.datacenter.model.basepeople.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;


import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.model.basepeople.service.SearchPeopleService;
import cn.sowell.datacenter.model.esbasic.SearchTransportClient;

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
							.setFrom(pageInfo.getFirstIndex())//从第几条开始查， 默认0
							.setSize(pageInfo.getPageSize());//每页显示数量 ，默认10
			
			//高亮配置
			HighlightBuilder hiBuilder=new HighlightBuilder();
	        hiBuilder.preTags(" <span style=\"color:red\">");
	        hiBuilder.postTags("</span>");
	        hiBuilder.field("name")
	        	.field("idCode")
	        	.field("address")
	        	.field("content");
	        
	        //查询详情
			BoolQueryBuilder qb = QueryBuilders.boolQuery();
			if(!name.equals(""))
				qb = qb.filter(QueryBuilders.matchPhraseQuery("name", name));//进行短语匹配过滤
			if(!idCode.equals("")){
				String id = "*"+idCode+"*";
				qb = qb.must(new QueryStringQueryBuilder(id).field("idCode"));
			}
			if(!address.equals("")){
				BoolQueryBuilder addressQuery = QueryBuilders.boolQuery();
				String addressList[] = address.split(" ");//将多个条件断开
				for(int i=0;i<addressList.length;i++){
					addressQuery = addressQuery.should(QueryBuilders.matchPhraseQuery("address", addressList[i]));
				}
				qb = qb.should(addressQuery);
			}
			if(!content.equals("")){
				BoolQueryBuilder contentQuery = QueryBuilders.boolQuery();
				String contentList[] = content.split(" ");
				for(int i=0;i<contentList.length;i++){
					if(isNumeric(contentList[i])){//判断是否为数字
						String con = "*"+contentList[i]+"*";
						contentQuery = contentQuery.must(new QueryStringQueryBuilder(con).field("content"));
					}				
					else
						contentQuery = contentQuery.should(QueryBuilders.matchPhraseQuery("content", contentList[i]));
				}
				qb.should(contentQuery);
			}
			
			builder.setQuery(qb);			
			//实现查询
			SearchResponse response = builder.highlighter(hiBuilder).execute().actionGet();			
			//总数量
			int count = (int) response.getHits().getTotalHits();			
			//将内容遍历到jsonThree
			JSONArray  jsonThree = new JSONArray ();
			for(SearchHit hit:response.getHits()){
				highLight(hit,"name");//将高亮的关键字字符串覆盖到hit.getSource()
				highLight(hit,"address");
				highLight(hit,"content");
				jsonThree.add(hit.getSource());
				logger.debug("权值："+hit.getScore()+"  内容："+hit.getSource());
			}
			logger.debug("总数量："+count+" 耗时："+response.getTookInMillis());
			pageInfo.setCount(count);
			return jsonThree;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("Search failed");
			return null;
		}
		
	}
	
	
	
	public void highLight(SearchHit hit,String key){//关键字高亮
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
