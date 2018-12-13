package com.domi.support.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static String doPost(String url,Map<String,String> map){  
		String result = "";
		 // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);  
        // 创建参数队列    
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
        for(Map.Entry<String, String> entry : map.entrySet()){
        	formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
        }
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    result = EntityUtils.toString(entity, "UTF-8");
System.out.println(result);                    
                }  
            } finally {  
                response.close();  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return result;
    }  
	
	
	 /**
     * 发送HttpGet请求
     * @param url
     * @return
     */
    public static String doGet(String url, Map<String, String> param) {
    	if (param!=null && param.size()!=0) {
    		url += "?";
			for(Map.Entry<String, String> entry : param.entrySet()){
				url += entry.getKey()+"="+entry.getValue()+"&";
			}
		}
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.生成一个get请求
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            //3.执行get请求并返回结果
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;
        try {
            //4.处理结果，这里将结果返回为字符串
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public static String postJson(String url, String json) {
    	String result = "";
		 // 创建默认的httpClient实例.    
       CloseableHttpClient httpclient = HttpClients.createDefault();  
       // 创建httppost    
       HttpPost httppost = new HttpPost(url);  
       try {  
    	   
    	   StringEntity param = new StringEntity(json, "UTF-8");
//    	   StringEntity param = new StringEntity(json);
    	   param.setContentEncoding("UTF-8");  
    	   httppost.setHeader("Content-Type", "application/json;charset=utf-8");
           httppost.setEntity(param);  
           CloseableHttpResponse response = httpclient.execute(httppost);  
           try {  
               HttpEntity entity = response.getEntity();  
               if (entity != null) {  
                   result = EntityUtils.toString(entity, "UTF-8");
               }  
           } finally {  
               response.close();  
           }  
       } catch (IOException e) {  
           e.printStackTrace();  
       } finally {  
           // 关闭连接,释放资源    
           try {  
               httpclient.close();  
           } catch (IOException e) {  
               e.printStackTrace();  
           }  
       }  
       return result;
    }
    
    public static void main(String[] args) throws Exception {
		String result = doGet("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=59.41.64.125&co=&resource_id=6006&t=1506654589991&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110207357968372253119_1506653762076&_=1506653762078", null);
		System.out.println(result);
    }
}
