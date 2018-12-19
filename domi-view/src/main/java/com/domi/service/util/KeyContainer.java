package com.domi.service.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class KeyContainer {
	
	public class ocr{
	    public static final String api_id = "fb49b5a7442041ce8b6d07141d704461"; 
	    public static final String api_secret = "3bb2599d05974ab2a2adc2c5e4b11cd8";
	    public static final String POST_URL = "https://v1-auth-api.visioncloudapi.com/police/idnumber_verification";
	}
         
	public static void verifyNameAndIDNumber(String username, String IDNumber) throws Exception {
    	  HttpClient httpclient = HttpClients.createDefault();
    	  HttpPost httppost = new HttpPost(ocr.POST_URL);

    	  List<NameValuePair> params = new ArrayList<NameValuePair>(4);
    	  params.add(new BasicNameValuePair("api_id", ocr.api_id));
    	  params.add(new BasicNameValuePair("api_secret", ocr.api_secret));
    	  params.add(new BasicNameValuePair("name", username));
    	  params.add(new BasicNameValuePair("id_number", IDNumber));
    	  httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    	  HttpResponse response = httpclient.execute(httppost);
    	  if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entitys = response.getEntity();
                BufferedReader reader = new BufferedReader(
                new InputStreamReader(entitys.getContent()));
                String line = reader.readLine();
                System.out.println(line);
          }else{
                HttpEntity r_entity = response.getEntity();
                String responseString = EntityUtils.toString(r_entity);
                JSONObject jsonObject = new JSONObject(responseString);
System.out.println("status:"+jsonObject.getString("status"));                
System.out.println("request_id:"+jsonObject.getString("request_id"));                
                System.out.println("错误码是："+response.getStatusLine().getStatusCode()+"  "+response.getStatusLine().getReasonPhrase());
                System.out.println("出错原因是："+responseString);
                //你需要根据出错的原因判断错误信息，并修改
          }
	}
	
	public static void main(String[] args) throws Exception {
		verifyNameAndIDNumber("陈洋洋", "421126199110026033");
	}
      
   
}