package com.domi.service.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.domi.support.identification.StringUtil;

public class PhonePlaceUtil {
	private static final Logger logger = Logger.getLogger(PhonePlaceUtil.class);
	private static final String PHONE_PLACE_API_URL = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm";

	public static JSONObject getPhonePlace(String phone) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tel", phone);
		String result = HttpClientUtil.doGet(PHONE_PLACE_API_URL, map);
		result = result.substring(result.indexOf("{"), result.length());
		JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
		return jsonObject;
	}

	public static String getProvince(String phone) {
		String province = "";
		if (StringUtil.isEmpty(phone)) {
			return "";
		}
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", phone);
			String result = HttpClientUtil.doGet(PHONE_PLACE_API_URL, map);
			result = result.substring(result.indexOf("{"), result.length());
			JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
			province = jsonObject.getString("province");
		} catch (Exception e) {
			logger.error("获取手机归属地出错", e);
		}

		return province;
	}

	public static void main(String[] args) {
//		JSONObject phonePlace = getPhonePlace("15915775654");
//		System.out.println(phonePlace);
//		System.out.println(phonePlace.getString("province"));
		System.out.println(getProvince("17377771641"));
	}
}
