package com.domi.support.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.domi.support.utils.EncodingConstants;

/**
 * url��صĹ�����
 * 
 * @author tgf(Mar 5, 2011)
 * 
 */
public class UrlUtil {
    /**
     * ��url���Ϸ����ַ�ת����utf8�����ʽ Translates a string into
     * application/x-www-form-urlencoded format
     * 
     * @param value
     * @return
     */
    public static String encode(String value) {
        try{
            return URLEncoder.encode(value, EncodingConstants.UTF8);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * ��url���ַ����
     * 
     * @param value
     * @return
     */
    public static String decode(String value) {
        try{
            return URLDecoder.decode(value, EncodingConstants.UTF8);
        }catch (Exception e){
            return null;
        }
    }
}
