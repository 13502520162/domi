package com.domi.service.util;

import java.util.Calendar;
/**
 * 时间格式化工具类
 * @author chenhuanshuo
 *
 * @Date 2017年7月5日
 */
public class DateParseUtil {
	
	/**
	 * 根据身份证号获取年龄
	 * @param IdNO
	 * @return
	 * @author chenhuanshuo
	 * @Date 2017年7月5日
	 */
    public static int IdNOToAge(String IdNO){
	  Calendar calendar = Calendar.getInstance();
	  int curYear = calendar.get(Calendar.YEAR);//获取年
	  int curMonth =calendar.get(Calendar.MONTH) + 1;//获取月份，0表示1月份
	  int curDay = calendar.get(Calendar.DAY_OF_MONTH);//获取当前天数
	  int age=-1;
      if (IdNO!=null&&IdNO.length()==18) {
    	int idYear =Integer.parseInt(IdNO.substring(6, 10));
    	int idMonth =Integer.parseInt(IdNO.substring(10, 12));
    	int idDay=Integer.parseInt(IdNO.substring(12, 14));
    	 age=curYear-idYear-1;
        if(curMonth==idMonth){
        	if(curDay>=idDay){
        		age=age+1;
        	}
        }else if(curMonth>idMonth){
        	age=age+1;
        }
        return age;
     }
      return age;
    }
   /**
    * 
    * @param ms
    * @return
    * @author chenhuanshuo
    * @Date 2017年7月17日
    */
	public static String formatTime(Long ms) {  
	    Integer ss = 1000;  
	    Integer mi = ss * 60;  
	    Integer hh = mi * 60;  
	    Integer dd = hh * 24;  
	  
	    Long day = ms / dd;  
	    Long hour = (ms - day * dd) / hh;  
	    Long minute = (ms - day * dd - hour * hh) / mi;  
	    Long second = (ms - day * dd - hour * hh - minute * mi) / ss;  
	    StringBuffer sb = new StringBuffer();  
	    if(day > 0) {  
	        sb.append(day+"天");  
	    }  
	    if(hour > 0) {  
	        sb.append(hour+"小时");  
	    }  
	    if(minute > 0) {  
	        sb.append(minute+"分");  
	    }  
	    if(second > 0) {  
	        sb.append(second+"秒");  
	    }else{
	    	 sb.append("0秒");  
	    }  
	    return sb.toString();  
	} 
}
