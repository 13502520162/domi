package com.domi.support.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

public class RiskServicePreloan {
	
		private static final Log log = LogFactory.getLog(RiskServicePreloan.class);
		private static final String apiUrl = "https://api.tongdun.cn/preloan/apply/v3";
		private static final String apiUrl2 = "https://api.tongdun.cn/preloan/report/v6";

		private static final String PARTNER_CODE = "amengfenqi";
		private static final String PARTNER_KEY = "fbda06047bc7432ab644ae77fb8a885e";
		private static final String APP_NAME = " fenqibei_web";
		
		private HttpURLConnection conn;

		public String invoke(Map<String, Object> params) {
		try {
			URL url = new URL(apiUrl);
			// 组织请求参数
			StringBuilder postBody = new StringBuilder();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() == null) continue;
				postBody.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),
				"utf-8")).append("&");
			}
			if (!params.isEmpty()) {
				postBody.deleteCharAt(postBody.length() - 1);
			}
			conn = (HttpURLConnection) url.openConnection();
			// 设置长链接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置连接超时
			conn.setConnectTimeout(1000);
			// 设置读取超时
			conn.setReadTimeout(500);
			// 提交参数
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.getOutputStream().write(postBody.toString().getBytes());
			conn.getOutputStream().flush();
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				log.warn("RiskServicePreloan] invoke failed, response status:" + responseCode);
				return null;
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line).append("\n");
			}
			return result.toString();
	} catch (Exception e) {
		log.error("[RiskServicePreloan] invoke throw exception, details: " + e);
	}
		return null;
	}
		
		public String invoke2(Map<String, Object> params) {
			try {
			StringBuilder getBody = new StringBuilder();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() == null) continue;
				getBody.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),
				"utf-8")).append("&");
			}
			String path = apiUrl2 + "?" + getBody.toString();
			System.out.println(path);
			  HttpURLConnection httpConn=null;
			  BufferedReader in=null;
			  try {
			   URL url=new URL(path);
			   httpConn=(HttpURLConnection)url.openConnection();

			   //读取响应
			   if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
			    StringBuffer content=new StringBuffer();
			    String tempStr="";
			    in=new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			    while((tempStr=in.readLine())!=null){
			     content.append(tempStr);
			    }
			    return content.toString();
			   }else{
			    throw new Exception("请求出现了问题!");
			   }
			  } catch (IOException e) {
			   e.printStackTrace();
			  }finally{
			   in.close();
			   httpConn.disconnect();
			  }
			  return null;
			} catch (Exception e) {
				log.error("[RiskServicePreloan] invoke throw exception, details: " + e);
				return null;
			}
	}



		
		public static void main(String[] args) throws Exception {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("partner_code", PARTNER_CODE);
			params.put("partner_key", PARTNER_KEY);
			params.put("app_name", APP_NAME);
			params.put("name", "陈设晓");
			params.put("id_number", "440781199101206796");
			params.put("mobile", "15622102740");
			
			

			String retString = new RiskServicePreloan().invoke(params);
			System.out.println(retString);
			String reportId = "";
			if (retString != "" && retString != null) {
				org.json.JSONObject retJsonObject = new org.json.JSONObject(retString);
				if (retJsonObject.getString("success").equals("true")) {
					reportId = retJsonObject.getString("report_id");
					System.out.println(reportId);
					Map<String, Object> params2 = new HashMap<String, Object>();
					params2.put("partner_code", PARTNER_CODE);
					params2.put("partner_key", PARTNER_KEY);
					params2.put("app_name", APP_NAME);
					params2.put("report_id", reportId);
//					for (int i = 0; i < 50; i++) {
						Thread.sleep(20);
						String response2 = new RiskServicePreloan().invoke2(params2);
						System.out.println(response2);
						if (response2 != "" && response2 != null) {
							org.json.JSONObject retJsonObject2 = new org.json.JSONObject(response2);
							if (retJsonObject2.getString("success").equals("true")) {
								int finalScore = retJsonObject2.getInt("final_score");
								System.out.println(finalScore);
//								break;
							}
						}
					}
//				}

			}

		}
}
            