package com.domi.support.utils;

public class UserIdEncode {

	public static String encodeUserId(int userId){
		
		String userIdStr = String.valueOf(userId);
		String encodeUserId = "*****" + userIdStr;

		return encodeUserId;
	}
}
