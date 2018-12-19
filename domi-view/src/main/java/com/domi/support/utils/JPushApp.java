package com.domi.support.utils;

import java.util.Map;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JPushApp {
	private static final String MASTER_SECRET = "0dc8682ca5c35335a3880213";
	private static final String APPKEY = "0e117baa117060763ddbf312";
	
	public static void pushToAlias(String phone, String data) {
		try {
			JPushClient jpushClient = new JPushClient(MASTER_SECRET, APPKEY, true,0);
			PushPayload payload = buildPushObject_all_alias_alert(phone, data);
			PushResult result = jpushClient.sendPush(payload);
		} catch (Exception e) {
			System.out.println("jpush failed");
		}
	}
	
	public static void pushExtrasToAlias(String phone, String data, String platform, Map<String, String> extras) {
		try {
			JPushClient jpushClient = new JPushClient(MASTER_SECRET, APPKEY, true,0);
			PushPayload payload = null;
			if(platform.equals("IOS")){
				payload = iosExtrasPush(phone, data, extras);
			}
			else{
				payload = androidExtrasPush(phone, data, extras);
			}
			PushResult result = jpushClient.sendPush(payload);
		} catch (Exception e) {
			System.out.println("jpush failed");
		}
	}
	
	 private static PushPayload buildPushObject_all_alias_alert(String phone, String data) {
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.all())
	                .setAudience(Audience.alias(phone))
	                .setNotification(Notification.alert(data))
	                .build();
		 
	 }
	 
	 private static PushPayload iosExtrasPush(String phone, String alert, Map<String, String> extras) {
		 
		 	Notification notification = Notification.ios(alert, extras);
	        
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.all())
	                .setAudience(Audience.alias(phone))
	                .setNotification(notification)
	                .build();
	 }
	 
	 private static PushPayload androidExtrasPush(String phone, String alert, Map<String, String> extras) {

		 	Notification notification = Notification.android(alert, "订单提示", extras);
	        
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.all())
	                .setAudience(Audience.alias(phone))
	                .setNotification(notification)
	                .build();
	 }
	 
	 public static void main(String[] args) {
		 pushToAlias("15622102740","fddfdfds");
	 }
}
