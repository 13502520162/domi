package com.domi.support.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParamCheck {

	public static boolean check(String param, int supportLength){
		
		if(param == null){
			return false;
		}
		else{
			if(param.length() > supportLength){
				return false;
			}
			else{
				
				String regEx = "'|\"|=|>|<|!"; //表示a或F  
				Pattern pat = Pattern.compile(regEx);  
				Matcher mat = pat.matcher(param);  
				boolean ret = mat.find();   
				if(ret){
					return false;
				}
				else{
					return true;
				}
			}
		}
		
	}
}
