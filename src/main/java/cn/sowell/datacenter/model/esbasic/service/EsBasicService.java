package cn.sowell.datacenter.model.esbasic.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface EsBasicService {
	/**
	 * 添加更新一条索引内容
	 * @param json 添加更新内容；{"code":"xxx","name":"xxx","idCode":"xxx","address":"xxx","content":"xxx"}
	 * @param id 索引ID
    */
	public void eSearchUpdate(JSONObject json,String id);
	
	/**
	 * 删除一条索引内容
	 * @param id 索引ID
	 */
	public void eSearchDelete(String id);
	
	/**
	 * 获得一条索引内容
	 * @param id 索引ID
	 * @return 
	 */
	public Map<String, Object> eSearchGet(String id);
	
	/**
	 * 创建一个索引
	 * @param index 索引名
	 */
	public void createIndex(String index);
	
	/**
	 * 添加索引配置，默认ik_smart分词
	 * @param index 索引名称；
	 * @param type 索引类型
    */
	public void addMapping(String index, String type);
	
	/**
	 * 关闭索引
    */
	public void closeIndex();
}
