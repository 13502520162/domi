package com.domi.service.util;

import java.util.Calendar;
import java.util.Date;

public class UserPaymentDay {

	/*
	 * 算用户距离下个还款日的时间
	 */
	public static int caculateUserMonthlyPaymentDay(int paymentDay){
		  int lastDay = 0;	
		  if(paymentDay == 0){
			  return 0;
		  }
		  Calendar calendar = Calendar.getInstance();
		  int monthlyMaxDay = calendar.getActualMaximum(Calendar.DATE);
		  calendar.setTime(new Date());
		  int today = calendar.get(Calendar.DAY_OF_MONTH);
		  if(today <= paymentDay){
			  lastDay = Math.min(paymentDay, monthlyMaxDay) - today;
		  }
		  else{
			  calendar.add(Calendar.MONTH, 1);
			  int nextMonthlyMaxDay = calendar.getActualMaximum(Calendar.DATE);
			  lastDay = monthlyMaxDay - today +Math.min(paymentDay, nextMonthlyMaxDay);
		  }
		  return lastDay;
	}
	
	/**
	 * 计算当前的还款日 新增加的接口,与旧版不同
	 * @param paymentDay
	 * @param type  0:当期划款日   1：下期还款日
	 * @return
	 */
	public static Date caculateMonthPaymentDate(int paymentDay, int type){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		int today = calendar.get(Calendar.DAY_OF_MONTH);		
		if(today > paymentDay){
			calendar.add(Calendar.MONTH, 1);
			int maxDay = calendar.getActualMaximum(Calendar.DATE);
			calendar.set(Calendar.DAY_OF_MONTH, Math.min(paymentDay, maxDay));
		}else {
			int maxDay = calendar.getActualMaximum(Calendar.DATE);
			calendar.set(Calendar.DAY_OF_MONTH, Math.min(paymentDay, maxDay));
		}
		if(type == 1){
			calendar.add(Calendar.MONTH, 1);
		}
		return calendar.getTime();
	}

	
	/**
	 * 计算账单
	 * @param paymentDay	用户还款日
	 * @param billDay		用户账单日
	 * @param addMonth		所在期数
	 * @return
	 */
	public static Date caculateUserMonthlyPaymentDate(Date date, int paymentDay, int billDay, int addMonth){
		  Calendar calendar = Calendar.getInstance();  
		  calendar.setTime(date);
		  calendar.set(Calendar.HOUR_OF_DAY,0);
		  calendar.set(Calendar.MINUTE,0);
		  calendar.set(Calendar.SECOND, 0);
		  calendar.set(Calendar.MILLISECOND, 0);
		  int today = calendar.get(Calendar.DAY_OF_MONTH);
		  
		  int monthlyMaxDay = calendar.getActualMaximum(Calendar.DATE);
		  if(billDay < paymentDay){
			  if(today < billDay){
				  calendar.add(Calendar.MONTH, addMonth);
				  monthlyMaxDay = calendar.getActualMaximum(Calendar.DATE);
			  }else{
				  calendar.add(Calendar.MONTH, addMonth+1);
				  monthlyMaxDay = calendar.getActualMaximum(Calendar.DATE);
			  }
			  calendar.set(Calendar.DAY_OF_MONTH, Math.min(paymentDay, monthlyMaxDay));
			  return calendar.getTime();
		  }else{
			  if(today < billDay){
				  calendar.add(Calendar.MONTH, addMonth+1);
				  monthlyMaxDay = calendar.getActualMaximum(Calendar.DATE);
			  }else{
				  calendar.add(Calendar.MONTH, addMonth+2);
				  monthlyMaxDay = calendar.getActualMaximum(Calendar.DATE);
			  }
			  calendar.set(Calendar.DAY_OF_MONTH, Math.min(paymentDay, monthlyMaxDay));
			  return calendar.getTime();
		  }
	}
	
	public static int caculateUserPaymentDay(){	
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(new Date());
		int today = calendar.get(Calendar.DATE);
		return today;
	}
	
	public static int caculateUserBillDay(){	
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(new Date());
		int today = calendar.get(Calendar.DATE);
		if(today - 15 > 0){
			return today - 15;
		}else{
			calendar.add(Calendar.DATE, -15);
			return calendar.get(Calendar.DATE);
		}
	}
	
		
}
