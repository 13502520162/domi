
package com.domi.support.identification;

import java.util.HashMap;
import java.util.Random;


public class UserHelper {

	private static HashMap<String, String> email2Code = new HashMap<String, String>();
    private static HashMap<String, String> phone2Code = new HashMap<String, String>();

    /**
     * 随机生成5位验证码
     * 
     * @return
     */
    public static String genVerifyCode() {
    	// 5位长度的验证码
        int length = 5;
        Random random = new Random();
        StringBuffer strbuf = new StringBuffer();
        String temp = "";
        int itmp = 0;
        for(int i = 0; i < length; i++) {
            switch(random.nextInt(5)){
            case 1:// 生成A～Z的字
                itmp = random.nextInt(26) + 65;
                temp = String.valueOf((char)itmp);
                break;
            case 2:
                itmp = random.nextInt(26) + 97;
                temp = String.valueOf((char)itmp);
                break;
            default:
                itmp = random.nextInt(10) + 48;
                temp = String.valueOf((char)itmp);
                break;
            }

            strbuf.append(temp);
        }
        return strbuf.toString();
    }

    /**
     * 发送验证码
     * 
     * @param email
     * @param verifyCode
     */
    public static void emailVerifyCode(String email, String verifyCode) {
        if(StringUtil.isNotEmpty(email)) {
            String subject = "【天天免费】邮箱验证码";
            String content = "欢迎注册【天天免费】商家账号，您的邮箱验证码：" + verifyCode +"\n 注：此邮件为系统自动发送，请勿回复，如有问题请联系： 广州浩泰信息科技客服  020-39337854";
            EmailInfo info = new EmailInfo(subject, content);
            info.addTo(email);
            EmailUtil.sendEmailInThread(info);
        }
    }
    

    /**
     * 发送验证码
     * 
     * @param receiver
     * @param verifyCode
     */
    public static void smsVerifyCode(String receiver, String verifyCode) {
        if(StringUtil.isNotEmpty(receiver)) {
            String content = verifyCode;
            SMSInfo info = new SMSInfo(receiver, content);
            SMSUtil.sendSMSInThread(info);
        }
    }

    /**
     * 注册成功发送邮件
     * 
     * @param email
     */
    public static void email2Scuccess(String email) {
        if(StringUtil.isNotEmpty(email)) {
            String subject = "浩泰【天天免费】平台商家端账号注册成功";
            String content = "注册成功，欢迎加入浩泰【天天免费】！" + "\n 浩泰【天天免费】,让您的每一秒都能创造价值!" + 
            "\n 广州浩泰信息科技有限公司" +
            "   广州市番禺区广州大学城国家数字家庭产业基地东区B座239" +
            "   020-39337854";
            EmailInfo info = new EmailInfo(subject, content);
            info.addTo(email);
            EmailUtil.sendEmailInThread(info);
        }
    }

    /**
     * 存储验证码
     * 
     * @param email
     * @param verifyCode
     */
    public static void addEmail2Code(String email, String verifyCode) {
        if(email2Code.get(email) != null) {
            email2Code.remove(email);
        }
        email2Code.put(email, verifyCode);
    }

    /**
     * 存储验证码
     * 
     * @param phone
     * @param verifyCode
     */
    public static void addPhone2Code(String phone, String verifyCode) {
        if(phone2Code.get(phone) != null) {
            phone2Code.remove(phone);
        }
        phone2Code.put(phone, verifyCode);
    }

    /**
     * 获取验证码
     * 
     * @param email
     * @return
     */
    public static String getCodebyEmail(String email) {
        return email2Code.get(email);
    }
    
    /**
     * 验证该邮箱是否发送过注册码
     * 
     * @param email
     * @return
     */
    public static boolean isExist(String email){
    	return email2Code.containsKey(email);
    }
    
    /**
     * 清除该邮箱及其对应的注册码
     * 
     * @param email
     * @return
     */
    public static void clearEmail(String email){
    	email2Code.remove(email);
    }

    /**
     * 获取验证码
     * 
     * @param phone
     * @return
     */
    public static String getCodebyPhone(String phone) {
        return phone2Code.get(phone);
    }
}
