package com.domi.support.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
	public static String dateToString(Date date) {
		SimpleDateFormat formatter; 
		formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
	    String ctime = formatter.format(date);
	    return ctime;
	}
}
