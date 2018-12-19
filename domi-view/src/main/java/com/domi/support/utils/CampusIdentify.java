package com.domi.support.utils;


public class CampusIdentify {

	public static int campusIdentify(String address){
		int index = -1;
		
		index = address.indexOf("广中医");
		if(index != -1){
			return 1;
		}
		
		index = address.indexOf("广药");
		if(index != -1){
			return 2;
		}
		
		index = address.indexOf("广外");
		if(index != -1){
			return 3;
		}
		
		index = address.indexOf("广工");
		if(index != -1){
			return 4;
		}
		
		index = address.indexOf("广大");
		if(index != -1){
			return 5;
		}
		
		index = address.indexOf("华师");
		if(index != -1){
			return 6;
		}
		
		index = address.indexOf("星海");
		if(index != -1){
			return 7;
		}
		
		index = address.indexOf("中大");
		if(index != -1){
			return 8;
		}
		
		index = address.indexOf("广美");
		if(index != -1){
			return 9;
		}
		
		index = address.indexOf("华工");
		if(index != -1){
			return 10;
		}
		
		else{
			return 0;
		}
	}
}
