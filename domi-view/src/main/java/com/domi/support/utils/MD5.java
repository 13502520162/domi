package com.domi.support.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;


public class MD5 {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(MD5Encrypt("123456"));
		System.out.println(MD5Encrypt("jyhg&yhn1t6rfvd@DM141748913800138000"));
		System.out.println(mD5Base64Encrypt("123456"));
	}
	//该方法将你输入的字符串，通过md5加密，返回一个加密後的字符串
	public static String MD5Encrypt(String inStr) throws UnsupportedEncodingException {  
	  
	    MessageDigest md = null;  
	    String outStr = null;  
	    try {   

	     md = MessageDigest.getInstance("MD5");         //可以选中其他的算法如SHA   
	     byte[] digest = md.digest(inStr.getBytes("utf-8"));       
	     //返回的是byet[]，要转化为String存储比较方便  
	     outStr = bytetoString(digest);  
	    } 
	    catch (NoSuchAlgorithmException nsae) {   
	     nsae.printStackTrace();  
	    }  
	    return outStr; 
	} 
	
	public static String bytetoString(byte[] digest) {  

	    String str = "";  
	    String tempStr = "";  
	    for (int i = 0; i < digest.length; i++) {   
	     tempStr = (Integer.toHexString(digest[i] & 0xff));   
	     if (tempStr.length() == 1) {    
	      str = str + "0" + tempStr;   
	     } 
	     else {    
	      str = str + tempStr;   
	     }  
	    }  
	    return str;

	}
	/**
	 * md5+base64双重加密
	 * @param inputStrin
	 * @return
	 * @author chenhuanshuo
	 * @throws UnsupportedEncodingException 
	 * @Date 2017年8月17日
	 */
	public static String mD5Base64Encrypt(String inStr) throws UnsupportedEncodingException{

	    MessageDigest md = null;  
	    String outStr = null;  
	    try {   
	     md = MessageDigest.getInstance("MD5"); 
	     byte[] digest = md.digest(inStr.getBytes("utf-8"));  
	     outStr=new  BASE64Encoder().encodeBuffer(digest);
	    } 
	    catch (NoSuchAlgorithmException nsae) {   
	       nsae.printStackTrace();  
	    }  
	    return outStr; 
	}
}
