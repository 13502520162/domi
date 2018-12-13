package com.domi.support.utils;

import java.io.UnsupportedEncodingException;

public class Encode {
	public static String UTF8Encode(String iso) throws UnsupportedEncodingException {
		if (iso == null) {
			return "";
		}
		String utf8;
		byte bb[];
        bb = iso.getBytes("ISO-8859-1"); 
        utf8 = new String(bb, "utf-8");
        return utf8;
	}
	
}
