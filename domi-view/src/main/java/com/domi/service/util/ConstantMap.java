package com.domi.service.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ConstantMap {
	public final static Map<String,String> expressMap = new HashMap<String,String> ();  
	public final static List<String> expressList = new ArrayList<String> ();  
    static {  
    	expressMap.put("顺丰速运", "shunfeng");  
    	expressMap.put("跨越速运", "kuayue");  
    	expressMap.put("圆通快递", "yuantong");  
    	expressMap.put("申通快递", "shentong");  
    	expressMap.put("韵达快运", "yunda");  
    	expressMap.put("中通快递", "zhongtong");  
    	expressMap.put("汇通快递", "httx56");  
    	expressMap.put("天天快递", "tiantian");  
    	expressMap.put("宅急送", "zhaijisong");  
    	expressMap.put("邮政EMS", "ems");  
    	expressMap.put("优速", "yousu");  
    	expressMap.put("如风达", "rufengda");  
    	expressMap.put("快捷快递", "kuaijie");  
    	expressMap.put("京东", "jingdong");  
    	expressMap.put("邮政小包", "gnxb");  
    	expressMap.put("德邦", "debang");  
    	expressMap.put("国通", "guotong");  
    	expressMap.put("全峰快递", "quanfeng");  
    	expressMap.put("苏宁物流", "suning");  
    	expressMap.put("百世汇通", "bsky");  
    	
    	expressList.add("申通快递");
    	expressList.add("圆通快递");
    	expressList.add("韵达快运");
    	expressList.add("中通快递");
    	expressList.add("汇通快递");
    	expressList.add("顺丰速运");
    	expressList.add("京东");
    	expressList.add("天天快递");
    	expressList.add("跨越速运");
    	expressList.add("宅急送");
    	expressList.add("邮政EMS");
    	expressList.add("优速");
    	expressList.add("如风达");
    	expressList.add("快捷快递");
    	expressList.add("邮政小包");
    	expressList.add("德邦");
    	expressList.add("国通");
    	expressList.add("全峰快递");
    	expressList.add("苏宁物流");
    	expressList.add("百世汇通");
    }
}
