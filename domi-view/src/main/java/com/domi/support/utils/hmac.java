package com.domi.support.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class hmac {

	/**
	 * hmac-sha1 加密模式
	 * @param data
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String HMACSHA1(String data, String key) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
		}
try {
	System.out.println(MD5.MD5Encrypt(new String(byteHMAC)));
} catch (UnsupportedEncodingException e) {
	e.printStackTrace();
}		
		String oauth = new BASE64Encoder().encode(byteHMAC);
		return oauth;
	}

}
