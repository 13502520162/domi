package com.domi.support.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
	public static final String ERROR_INFO = "出问题了，程序猿们等着今晚被捡肥皂了 @_@";
	public static final int ERROR_NUM = 10;
	
	public static Map<String, Object> generateReturnMap(int errcode, String info, String method, Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("errcode", errcode);
		map.put("info", info);
		map.put("method", method);
		map.put("data", data);
		return map;
	}
	
	public static Map<String, Object> hyReturnMap(String RespCode, String RespMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("RespCode", RespCode);
		map.put("RespMsg", RespMsg);
		return map;
	}
}
