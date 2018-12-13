package com.domi.support.utils;

import java.util.LinkedList;
import java.util.List;

public class LuckyCodeGenerator {

	public static int genTodayGoodsLuckyCode(float shanghaiZ, float shenzhenZ, int totalJoinerCount){
		int codeTmp = (int)(shanghaiZ * shenzhenZ * 10000);
		String codeString = String.valueOf(codeTmp);
		
		String reserveString = new StringBuffer(codeString).reverse().toString();
		
		int lastNum = Integer.valueOf(reserveString)%totalJoinerCount;
		
		int luckyCode = lastNum + 10000001;
		
		return luckyCode;
	}
	
	public static List<Integer> genGoodsLuckyCode(int startLuckyCodeIndex, int count){
		List<Integer> luckyCodeList = new LinkedList<Integer>();
		
		for(int i = 0; i != count; ++i){
			luckyCodeList.add(startLuckyCodeIndex + i + 10000001);
		}
		
		return luckyCodeList;
	}
	
	public static boolean isValidLuckyCode(String luckyCode, int totalJoinerCount){
		
		String totalJoinerCountString = String.valueOf(totalJoinerCount);
		String count = luckyCode.substring(luckyCode.length() - totalJoinerCountString.length(), luckyCode.length());
		int countTmp = Integer.valueOf(count);
		
		if (countTmp>totalJoinerCount){
			return false;
		}
		else{
			return true;
		}
	}
}
