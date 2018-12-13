package com.domi.support.utils;


public class PhoneEncode {

	public static String encodePhone(String phone){
		
		String head = phone.substring(0, 3);
		String tail = phone.substring(8, phone.length());
		
		String encodePhone = head + "*****" + tail;
		return encodePhone;
	}
}
