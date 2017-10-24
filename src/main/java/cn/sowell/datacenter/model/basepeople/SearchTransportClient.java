package cn.sowell.datacenter.model.basepeople;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

public class SearchTransportClient {
	private TransportClient client;
	private static SearchTransportClient instance;
	private String key="cluster.name";
	private String name="elasticsearch";
	private String host="127.0.0.1";
	private int port=9300;
	

	private SearchTransportClient() {
//		BeanDefinitionRegistry reg = new DefaultListableBeanFactory();
//		PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(reg);   
//		reader.loadBeanDefinitions(new ClassPathResource("elasticSearch.properties"));   
//		BeanFactory factory = (BeanFactory)reg;
//		key = (String) factory.getBean("key");
//		name = (String) factory.getBean("name");
//		host = (String) factory.getBean("InetAddress");
//		port = (int) factory.getBean("transport");
		
		// 设置集群名称 5.x
		Settings settings = Settings.builder().put(key,name).build();
		
		// 创建client
		try {
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 单例
	 */
	public static SearchTransportClient getInstance() {
		if (instance == null) {
			instance = new SearchTransportClient();
		}
		return instance;
	}

	public TransportClient getTransportClient() {
		return client;
	}

}
