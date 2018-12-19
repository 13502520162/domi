package com.domi.support.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class CustomCookie {
	public static String generateCookie(String phone, int userId) throws UnsupportedEncodingException {
		String userIdStr = userId + "";
		Date now = new Date();
		String sig = phone + now.getTime();
		StringBuffer sigs = new StringBuffer();
		for (int i = 0; i < userIdStr.length(); i++) {
			sigs.append(sig.substring(i, 2 * i)).append(userIdStr.charAt(i));
			if (i == userIdStr.length() - 1) {
				sigs.append(sig.substring(2 * i));
			}
		}
		return OurMD5.MD5Encrypt(sigs.toString());
	}
}
