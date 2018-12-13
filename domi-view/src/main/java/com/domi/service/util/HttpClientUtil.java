package com.domi.service.util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.domi.support.utils.MD5;

public class HttpClientUtil {

	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	} 

	public static String doGet(String url) {
		return doGet(url, null);
	}

	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity =new UrlEncodedFormEntity(paramList, "utf-8");//设置请求数据编码
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}
	
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}
	
	 public static void main(String[] args) throws UnsupportedEncodingException {
		 boolean result=false;
		 Map<String,String> param = new HashMap<String,String>();
		 param.put("channelId", "1417");
		 param.put("adid", "489");
		 param.put("sign",MD5.MD5Encrypt("jyhg&yhn1t6rfvd@DM141748913800138021"));
		/* param.put("adid", "507");
		 param.put("sign",MD5.MD5Encrypt("jyhg&yhn1t6rfvd@DM141750713800138000"));*/
		 param.put("name", "安安");
		 param.put("sex", "M");
		 param.put("ip", "192.168.0.118");
		 param.put("birthday", "1992-7-12");
		 param.put("isMobile", "1");
		 param.put("mobile", "13800138021");
		// System.out.println(doPost("https://testapi.viplex.cn/myapi/?do=pabaoxian", param));
		 String pabxReturn = HttpClientUtil.doPost("https://testapi.viplex.cn/myapi/?do=pabaoxian", param);
		// String pabxReturn = HttpClientUtil.doPost("https://rdcapi.viplex.cn/myapi/?do=pabaoxian", param);
		 net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(pabxReturn);
		 Object object = fromObject.get("status");
			if(object instanceof Integer &&0==(int)object){//查询成功
				result=true;
			}
			System.out.println(pabxReturn);
			System.out.println(result);
	} 
}
