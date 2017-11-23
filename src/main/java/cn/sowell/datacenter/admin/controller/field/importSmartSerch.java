package cn.sowell.datacenter.admin.controller.field;

import cn.sowell.datacenter.model.basepeople.dto.FieldDataDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.aop.scope.ScopedProxyUtils;

import java.io.*;
import java.net.InetAddress;
import java.util.*;


/**
 * 修改当前实体测试类 不读取excel 采用测试数据 LinkedHashMap类；
 */

public class importSmartSerch {
    public static TransportClient client;

    public static void main(String args[]) throws IOException {
        String pathname = "D:\\field.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
        File filename = new File(pathname); // 要读取以上路径的input。txt文件
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename),"gbk"); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";
        line = br.readLine();
        int x=1;
        while (line != null) {
            String [] arr = line.split("\\s+");
            addFieldData(x,arr);
            x++;
            line = br.readLine(); // 一次读入一行数据
        }

    }


    public static void addFieldData (int id ,String arr[]){

        try {
            //设置集群名称
            Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
            //创建client
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

            long stime = System.currentTimeMillis();
            boolean flag = false;//设置检查标记
//
            Map<String, Object> json = new LinkedHashMap<String, Object>();
            ObjectMapper ob = new ObjectMapper();
            json.put("id", id);
            json.put("title", arr[0]);
            json.put("title_en",arr[1] );
            json.put("type", arr[2]);
            json.put("check_rule","0");
            String json1 = ob.writeValueAsString(json);
            System.out.println(json1);
            addEs(json1.toString(), String.valueOf(id));
            long etime = System.currentTimeMillis();
            System.out.println(etime - stime);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }


    private static void closeClient() {
        // TODO Auto-generated method stub
        client.close();
    }


    public static void addEs(String jsonValue, String esId) {
        IndexResponse response = client
                .prepareIndex("ydd", "demo", esId)
                .setSource()
                .setSource(jsonValue).execute().actionGet();

    }

    public static void eSearchGet(String id){
        GetResponse response = client
               .prepareGet("ydd", "demo","2")
                .execute().actionGet();
        System.out.println(response.getSource());

    }



     
}