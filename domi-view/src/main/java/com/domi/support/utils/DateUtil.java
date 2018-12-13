package com.domi.support.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {
	private static final Logger logger = Logger.getLogger(DateUtil.class);

	public static Date getCurrentDate(){
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date getDate(Date date){
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date getDateTime(Date date, int duration){
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(Calendar.DATE, duration);
		return calendar.getTime();
	}
	
	public static Date caculateEndDate(Date startDate, int duration){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(calendar.DAY_OF_MONTH, +duration);
		return calendar.getTime();
	}

	public static Date caculateNewDate(Date startDate, int OffsetDays){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(calendar.DAY_OF_MONTH, OffsetDays);
		return calendar.getTime();
	}
	
	public static Date timeToDateCorrectToDay(Long time){
		try{
			 SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
			 String d = format.format(time);
			 return format.parse(d);
		}
		catch(Exception e){
			return null;
		}
	}
	public static Date timeToDateCorrectToSecond(Long time){
		try{
			 SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			 String d = format.format(time);
			 return format.parse(d);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public static Date formatDate(String dateString){
		
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			date = sdf.parse(dateString);
			return date;
		}
		catch(Exception err){
			return date;
		}
	}
	
	public static Date formatDate(String dateString, String format){
		
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			date = sdf.parse(dateString);
			return date;
		}
		catch(Exception err){
			return date;
		}
	}
	
	public static Date formatDateCorrectToSecond(String dateString){
		
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			date = sdf.parse(dateString);
			return date;
		}
		catch(Exception err){
			return date;
		}
	}
	
	public static String getDateString(Date date){
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		return sdf.format(date);  
	}
	
	public static String getDateString(Date date, String format){
		
		SimpleDateFormat sdf=new SimpleDateFormat(format);  
		return sdf.format(date);  
	}
	
	public static String getDatetimeString(Date date) {
		if(date==null){
			return "";
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return sdf.format(date);  
	}
	
	public static int getTodayDayOfMonth(){
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(new Date());
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		return today;
	}
	
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date getMonday(int week) { //0为本周
	    Calendar cal = Calendar.getInstance();
	    cal.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
	    cal.add(Calendar.DATE, week * 7);
	    cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	
	public static Date getSunday(int week) { //0为本周
	    Calendar cal = Calendar.getInstance();
	    cal.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
	    cal.add(Calendar.DATE, week * 7);
	    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	
		
	public static int getDateDiff(Date date1, Date date2){
		int betweenDays = (int) (Math.abs((date1.getTime()-date2.getTime()))/(1000*60*60*24));
		return betweenDays;
	} 	
	/**根据身份证号获取年份*/
    public static Date getYearDate(String idNumber, int year) {
    	try {
    		Date oldDate = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        Date birthDate = sdf.parse(idNumber.substring(6, 14));
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(birthDate);
	        calendar.add(Calendar.YEAR, year);
	        oldDate = calendar.getTime();
	        return oldDate;
    	} catch (Exception e) {
    		logger.error("根据身份证号获取年份异常", e);
    		return null;
    	}
    }


	public static void main(String[] args) {
//		System.out.println(getMonday(0));
//		System.out.println(getSunday(-2));
//		System.out.println(getDateString(new Date(), "MM月dd日 HH:mm"));
		Date tmp = getDateTime(new Date(), -7);
		System.out.println(tmp);
		Date d = formatDate("2017-10-11", "yyyy-MM-dd");
		System.out.println(getDateDiff(d, new Date()));
	}
}
