package cn.sowell.datacenter.model.esbasic;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

import cn.sowell.copframe.common.property.PropertyPlaceholder;

@SuppressWarnings("unused")
public class SearchTransportClient {
	private TransportClient client;
	private static SearchTransportClient instance;
	private String name = PropertyPlaceholder.getProperty("es.name");
	private String host = PropertyPlaceholder.getProperty("es.host");
	private int port = Integer.parseInt(PropertyPlaceholder.getProperty("es.port"));

	private SearchTransportClient() {
		// 设置集群名称 5.x
		Settings settings = Settings.builder().put("cluster.name", name).build();
		// 创建client
		/*try {
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/
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
