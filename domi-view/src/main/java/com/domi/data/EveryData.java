package com.domi.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EveryData {

	private String date;
	private int registerCount = 0;  		
	private int identiCount = 0; 		
	private int creditOrderCount = 0;	
	private int itemOrderCount = 0;		
	private float creditMoney = 0; 		
	private float itemMoney = 0; 			
	private float allMoney = 0;
	
	private float guangdongPerformance = 0;
	private float hunanPerformance = 0;
	private float hebeiPerformance = 0;
	
	public synchronized void addRegisterCount(){
		registerCount += 1;
	}
	
	public synchronized void addIdentiCount(){
		identiCount += 1;
	}
	
	public synchronized void addPerformance(int orderType, float money, String schoolPrivince){
		if(orderType == 1){
			creditMoney += money;
			creditOrderCount += 1;
		}else if(orderType == 0){
			itemMoney += money;
			itemOrderCount += 1;
		}
		if("广东".equals(schoolPrivince)){
			guangdongPerformance += money;
		}else if("湖南".equals(schoolPrivince)){
			hunanPerformance += money;
		}else if("河北".equals(schoolPrivince)){
			hebeiPerformance += money;
		}
		allMoney += money;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}
	public int getIdentiCount() {
		return identiCount;
	}
	public void setIdentiCount(int identiCount) {
		this.identiCount = identiCount;
	}
	public int getCreditOrderCount() {
		return creditOrderCount;
	}
	public void setCreditOrderCount(int creditOrderCount) {
		this.creditOrderCount = creditOrderCount;
	}
	public int getItemOrderCount() {
		return itemOrderCount;
	}
	public void setItemOrderCount(int itemOrderCount) {
		this.itemOrderCount = itemOrderCount;
	}
	public float getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(float creditMoney) {
		this.creditMoney = creditMoney;
	}
	public float getItemMoney() {
		return itemMoney;
	}
	public void setItemMoney(float itemMoney) {
		this.itemMoney = itemMoney;
	}
	public float getAllMoney() {
		return creditMoney+itemMoney;
	}
	public void setAllMoney(float allMoney) {
		this.allMoney = allMoney;
	}
	public float getGuangdongPerformance() {
		return guangdongPerformance;
	}
	public void setGuangdongPerformance(float guangdongPerformance) {
		this.guangdongPerformance = guangdongPerformance;
	}
	public float getHunanPerformance() {
		return hunanPerformance;
	}
	public void setHunanPerformance(float hunanPerformance) {
		this.hunanPerformance = hunanPerformance;
	}
	public float getHebeiPerformance() {
		return hebeiPerformance;
	}
	public void setHebeiPerformance(float hebeiPerformance) {
		this.hebeiPerformance = hebeiPerformance;
	} 			
	
	public void reSet() throws ParseException {		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = sdf.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, 1);		
		date = sdf.format(calendar.getTime());
		registerCount = 0;  		
		identiCount = 0; 		
		creditOrderCount = 0;	
		itemOrderCount = 0;		
		creditMoney = 0; 		
		itemMoney = 0; 			
		allMoney = 0;		
		guangdongPerformance = 0;
		hunanPerformance = 0;
		hebeiPerformance = 0;
	}
}
