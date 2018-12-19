package com.domi.support.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ExceptionParamLog {
	
	public static String logExceptionParam(HttpServletRequest request){
		
		String paramString = new String();
		
		Map<String, String[]> params = request.getParameterMap();  

        for (String key : params.keySet()) {  
            String[] values = params.get(key);  
            for (int i = 0; i < values.length; i++) {  
                String value = values[i];  
                paramString += key + "=" + value + "&";  
            }  
        }  
		
		return paramString;
	}
}
