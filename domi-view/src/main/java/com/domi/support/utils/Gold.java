package com.domi.support.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Gold {
	private static final String SHIYONG_URL = "http://www.shi-yong.net";
	//private static final String SHIYONG_URL = "http://192.168.1.120:8080";
	
	public static boolean decrease(String openid, Integer gold)  {
		String url = SHIYONG_URL + "/thirdparty/decreaseUserPoint?openID=" + openid + "&point=" + gold;
		try {
			String json = getResponse(url);
			JSONObject jsonObject = new JSONObject(json);
			Integer errcode = jsonObject.getInt("errcode");
			if (errcode == 0) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}
	
	public static boolean increase(String openid, Integer gold) {
		String url = SHIYONG_URL + "/thirdparty/increaseUserPoint?openID=" + openid + "&point=" + gold;
		try {
			String json = getResponse(url);
			JSONObject jsonObject = new JSONObject(json);
			Integer errcode = jsonObject.getInt("errcode");
			if (errcode == 0) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}
	
	private static String getResponse(String url) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		String result = "";
		if(httpResponse.getStatusLine().getStatusCode()==200){
			result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
		}
		return result;
	}
}
