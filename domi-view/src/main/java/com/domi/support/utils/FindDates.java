package com.domi.support.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FindDates {
	
	public static void main(String[] args) {
		System.out.println(findDates("2015-05-16", "2015-05-24"));
	}

	public static List<String> findDates(String startTime, String endTime) {
		List<String> lDate = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			lDate.add(startTime);
			Date begin = sdf.parse(startTime);
			Date end = sdf.parse(endTime);
			Calendar calBegin = Calendar.getInstance();
			// 使用给定的 Date 设置此 Calendar 的时间
			calBegin.setTime(begin);
			Calendar calEnd = Calendar.getInstance();
			// 使用给定的 Date 设置此 Calendar 的时间
			calEnd.setTime(end);
			// 测试此日期是否在指定日期之后
			while (end.after(calBegin.getTime())) {
				// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
				calBegin.add(Calendar.DAY_OF_MONTH, 1);
				lDate.add(sdf.format(calBegin.getTime()));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lDate;
	}
}