package com.domi.support.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Http {
	public static void main(String[] args) throws Exception {
		//System.out.println(getICKDInfo("", ""));
		System.out.println(getExpressInfo());
	}
	
	public static Map<String, Object> getExpressInfo() throws Exception {
		Map<String, Object> map = new HashMap<>();
		
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
		String url = "http://p.kuaidi100.com/mobile/mobileapi.do";  
		URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        String param = "json={\"num\":\"3100117687234\",\"com\":\"\",\"os_name\":\"GT-S7572\",\"versionCode\":432,\"appid\":\"com.Kingdee.Express\",\"longitude\":113.4075,\"latitude\":23.057389,\"os_version\":\"android4.1.2\"}&method=query";
        out.print(param);
        // flush输出流的缓冲
        out.flush();

        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += "\n" + line;
        }
        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        
        if (jsonObject.has("lastResult")) {
        	 JSONObject lastResult = (JSONObject)jsonObject.get("lastResult");
             JSONArray data = (JSONArray)lastResult.get("data");
             JSONObject indexTop = (JSONObject)data.get(0);
             String context = indexTop.getString("context");
             map.put("count", data.length());
             map.put("topRecord", context);
        } else {
        	return null;
        }
		
		return map;
	}
	
	
	public static Map<String, Object> getICKDInfo(String nu, String com) throws Exception{
		Map<String, Object> map = new HashMap<>();
		String id = "107534";
		String secret = "dcd2721fa259aa5dc67d6db29c68d87f";
		
        BufferedReader in = null;
        String result = "";
        
        switch (com) {
        case "youzhengguonei":
        	com = "gnxb";
        	break;
        case "huitongkuaidi":
        	com = "huitong";
        	break;
        case "debangwuliu":
        	com = "debang";
        	break;
        case "jd":
        	com = "jingdong";
        	break;
        case "rufengda":
        	com = "rufeng";
        	break;
        case "guotongkuaidi":
        	com = "guotong";
        	break;
        case "yuntongkuaidi":
        	com = "yuntong";
        	break;
        case "suer":
        	com = "sure";
        	break;
        case "youshuwuliu":
        	com = "yousu";
        	break;
        default:
        	break;
        }
        
       // nu = "3100117687234";
		String url = "http://api.ickd.cn/?id=" + id + "&secret=" + secret + "&com=" + com + "&nu=" + nu + "&encode=utf8";  
		System.out.println(url);
		URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += "\n" + line;
        }
		System.out.println(result);
		
		JSONObject jsonObject = new JSONObject(result);
	    int errCode = jsonObject.getInt("errCode");
	     if (errCode == 0) {
	        JSONArray data = (JSONArray)jsonObject.get("data");
	        JSONObject indexTop = (JSONObject)data.get(data.length() - 1);
	        String context = indexTop.getString("context");
	        map.put("count", data.length());
	        map.put("topRecord", context);
	    } else {
	        return null;
	     }
		return map;
	}
}
