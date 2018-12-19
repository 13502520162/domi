package com.domi.support.utils;

public class AddressMatch {
	private static String[] shortAddress = {"广工", "广大", "广中医", "广药", "华工", "中大", "星海", "华师", "广美", "广外", "国家数字"};
	private static String[] longAddress = {"广东工业大学", "广州大学", "广州中医药大学", "广东药学院", "华南理工大学", "中山大学", "星海音乐学校", "华南师范大学", "广州美术学院", "广东外语外贸大学"};
	
	public static String getAddress(String address) {
		
		String a;
		for (int i = 0; i < shortAddress.length; i++) {
			a = shortAddress[i];
			if (address.contains(a)) {
				return shortAddress[i];
			}
		}
		
		for (int i = 0; i < longAddress.length; i++) {
			a = longAddress[i];
			if (address.contains(a)) {
				return shortAddress[i];
			}
		}
		
		return "其他";
	}
}
