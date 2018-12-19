package com.domi.support.utils;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.domi.support.identification.StringUtil;

/**
 * ip工具类
 * @author shexiao
 *
 */
public class IpUtils {
	private static final Logger logger = Logger.getLogger(IpUtils.class);
	
	public static String[] cityType1 = {"北京", "上海", "深圳", "广州"};
	public static String[] cityType2 = {"成都", "杭州", "武汉", "天津", "南京", "重庆", "西安", "长沙", "青岛", "沈阳", "大连", "厦门", "苏州", "宁波", "无锡"};
	public static String[] cityType3 = {"福州", "合肥", "郑州", "哈尔滨", "佛山", "济南", "东莞", "昆明", "太原", "南昌", "南宁", "温州", "石家庄", "长春", "泉州", "贵阳", "常州", "珠海", "金华", "烟台", "海口", "惠州", "徐州", "乌鲁木齐", "扬州", "汕头", "嘉兴", "洛阳", "南通"};
	
	
	public static String getClientIP(HttpServletRequest request) {
		try {
			if (request == null) {
				return "";
			}
		    String ip = request.getHeader("X-Real-IP");
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		        ip = request.getHeader("X-Forwarded-For");
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		        ip = request.getHeader("Proxy-Client-IP");
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		        ip = request.getHeader("WL-Proxy-Client-IP");
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		        ip = request.getRemoteAddr();
		    return ip;
		} catch (Exception e) {
			logger.error("获取用户访问ip地址出错" + e);
		}
		return "";
	}
	
	public static String getIpCity(String ip) {
		try {
			if (StringUtil.isEmpty(ip)) {
				return "";
			}
			String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?resource_id=6006&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110207357968372253119_1506653762076&query=" + ip;
			String result = HttpUtil.doGet(url, null);
			return parseJson(result);
		} catch (Exception e) {
			logger.error((ip + " : 请求出错"), e);
		}
		return "";
	}
	
	private static String parseJson(String result) {
		try {
			if (StringUtil.isEmpty(result)) {
				return "";
			}
			int firstIndex = result.indexOf("(");
			int lastIndex = result.lastIndexOf(")");
			if (firstIndex == -1 || lastIndex == -1) {
				return "";
			}
			
			String resultJSON = result.substring(firstIndex + 1, lastIndex);
			JSONObject json = new JSONObject(resultJSON);
			JSONArray data = json.getJSONArray("data");
			if (data != null && data.length() > 0) {
				JSONObject obj1 = data.getJSONObject(0);
				String location = obj1.getString("location");
				return location;
			}
			
		} catch (Exception e) {
			logger.error((result + " : 解析json错误"), e);
		}
		
		return "";
	}
	
	public static void main(String[] args) throws Exception {
		Random random = new Random();
		int maxNum = 255;
		for (int i = 0; i < 200; i++) {
//			System.out.println(i);
			String ip = random.nextInt(maxNum) + "." + random.nextInt(maxNum) + "." + random.nextInt(maxNum) + "." + random.nextInt(maxNum);
			IpCityQueryThread t1 = new IpCityQueryThread(ip);
			LuckyThreadPool.getInstance().getThreadPool().execute(t1);
			Thread.sleep(100);
		}
//		System.out.println(getIpCity("223.104.9.193"));
	}
	
	static class IpCityQueryThread extends Thread {
		private String ip;
		
		public IpCityQueryThread(String ip) {
			this.ip = ip;
		}
		
		@Override
		public void run() {
			System.out.println(ip  + " : " + getIpCity(ip));
		}
	}
}
