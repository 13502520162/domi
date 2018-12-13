package com.domi.service.util;

public class ConvertUtil {
	/**
	 * 取小数点后两位
	 * @param d
	 * @return
	 * @author chenhuanshuo
	 * @Date 2017年6月15日
	 */
	public static float convert(double d){
		long l1 = Math.round(d*100); //四舍五入
	float result = (float) (l1/100.00); //注意:使用 100.0 而不是 100
	return result;
	}
}
