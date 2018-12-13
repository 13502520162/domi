package com.domi.support.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static Long countDown(){
		
		
		Date currentDate = new Date();
		
	    SimpleDateFormat dateFormatTmp = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式      
		String  dayStringTmp  = dateFormatTmp.format(currentDate);      
		 
		SimpleDateFormat sdfTmp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDateTmp = dayStringTmp + " 15:05:00";
		try{
			Date dateTmp=sdfTmp.parse(strDateTmp);
			
			if(currentDate.after(dateTmp)){
				
				Date endDate=null;
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(currentDate);
			    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
					  
			    	endDate = DateUtil.caculateNewDate(currentDate, 3);
				}
			    else if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
				  
			    	endDate = DateUtil.caculateNewDate(currentDate, 2);
				}
			    else if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			    	
			    	endDate = DateUtil.caculateNewDate(currentDate, 1);
			    }
			    else{
			    	endDate = DateUtil.caculateNewDate(new Date(), 1);      
			    }
			    
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式      
				String  dayString  = dateFormat.format(endDate);      
				 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strDate = dayString + " 15:05:00";
				try{
					Date date=sdf.parse(strDate);
					
					return date.getTime();
				}
				catch(Exception exception){
					return null;
				}
			}
			else{
				Date endDate=null;
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(currentDate);
			    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
					  
			    	endDate = DateUtil.caculateNewDate(currentDate, 3);
				}
			    else if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
				  
			    	endDate = DateUtil.caculateNewDate(currentDate, 2);
				}
			    else if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			    	
			    	endDate = DateUtil.caculateNewDate(currentDate, 1);
			    }
			    else{
			    	endDate = new Date();      
			    }
			    
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式      
				String  dayString  = dateFormat.format(endDate);      
				 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strDate = dayString + " 15:05:00";
				try{
					Date date=sdf.parse(strDate);
					
					return  date.getTime();
				}
				catch(Exception exception){
					return null;
				}
			}
		}
		catch(Exception exception){
			return null;
		}
	}
}
