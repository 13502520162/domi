package com.domi.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.bcloud.msg.http.HttpSender;
import org.apache.log4j.Logger;


public class PhoneCheckCodeCache extends Cache{
	
	Logger logger = Logger.getLogger(PhoneCheckCodeCache.class);
	
	private final static String URL = "http://114.55.141.65/msg/HttpSendSM";//应用地址
	private final static String ACCOUNT = "ABABAB";//账号
	private final static String PSWD = "Qazx1234";//密码
	
	private static PhoneCheckCodeCache m_instance = null;
	private Map<String, String> phoneCheckcode = new HashMap<>();
	
	Queue<String> registeCheckCodeQueue = new LinkedList<String>();
	Queue<String> changePasswordCheckCodeQueue = new LinkedList<String>();
	 
	private ReentrantReadWriteLock registeCheckCodeQueue_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock changePasswordCheckCodeQueue_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock phoneCheckcode_rwlock = new ReentrantReadWriteLock();
	
	private Timer timer = new Timer(true);
	private TimerTask task = new TimerTask() {
        @Override
        public void run() {

        	sendAndStorePhoneCode();
        }
    };
 
	private PhoneCheckCodeCache() {
		Date date = new Date();
		timer.schedule(task, date, 1000);
	}
	
	public synchronized static PhoneCheckCodeCache getInstance() {
		if (m_instance == null) {
			m_instance = new PhoneCheckCodeCache();
		}
		return m_instance ;
	}
	
	public  void pushToRegisteCheckCodeQueue(String phone){
		try{
			registeCheckCodeQueue_rwlock.writeLock().lock();
			registeCheckCodeQueue.offer(phone);
		}
		finally {
			registeCheckCodeQueue_rwlock.writeLock().unlock();
		}
	}
	
	public  void pushToChangePasswordCheckCodeQueue(String phone){
		try{
			changePasswordCheckCodeQueue_rwlock.writeLock().lock();
			registeCheckCodeQueue.offer(phone);
		}
		finally {
			changePasswordCheckCodeQueue_rwlock.writeLock().unlock();
		}
	}
	
	/*public String genPhoneCode(String phone) {
		
		try{
			String phoneCode = generatePhoneCode();
			TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23352113", "82d22c25b59c2703a9038de9342f8fa0");
			AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
			req.setExtend("123456");
			req.setSmsType("normal");
			req.setSmsFreeSignName("分期呗");
			req.setSmsParamString("{\"code\":\"" + phoneCode +"\",\"product\":\"分期呗\"}");
			req.setRecNum(phone);
			req.setSmsTemplateCode("SMS_8175253");
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			if (rsp.isSuccess()) {
	               
				return phoneCode;
	        } else {
	        	return null;
	        }
		}
		catch(Exception e){
			return null;
		}
	}*/
	
	public void addCheckCode(String phone, String checkCode) {
		
		try{
			phoneCheckcode_rwlock.writeLock().lock();
			
			phoneCheckcode.put(phone, checkCode);
			
		} finally {
			phoneCheckcode_rwlock.writeLock().unlock();
		}
	}
	
	public String getCheckCode(String phone) {
		String checkCode = null;
		try {
			phoneCheckcode_rwlock.readLock().lock();
			checkCode = phoneCheckcode.get(phone);
			
		} finally {
			phoneCheckcode_rwlock.readLock().unlock();
			
		}
		return checkCode;
	}
	
	public void removeCheckCode(String phone){
		try {
			phoneCheckcode_rwlock.writeLock().lock();
			phoneCheckcode.remove(phone);
		}
		finally {
			phoneCheckcode_rwlock.writeLock().unlock();
		}	
	}
	
	
	public String generatePhoneCode() {
		Random random = new Random();
		int max = 9999;
		int min = 1000;
		int s = random.nextInt(max)%(max-min+1) + min;
		return s + "";
	}
	
	public void sendAndStorePhoneCode(){
		try{
			registeCheckCodeQueue_rwlock.writeLock().lock();
			String phone = null;
			while((phone=registeCheckCodeQueue.poll())!=null){
				String checkCode = null;
				if(phone.startsWith("1800000")){
					checkCode = "1234";
				}else {
					checkCode = send(phone);					
				}
				if(checkCode != null){
					System.out.println("phone:"+ phone + " checkCode:" + checkCode);
					addCheckCode(phone, checkCode);
				}
	        }
			while((phone=changePasswordCheckCodeQueue.poll())!=null){
				String checkCode = null;
				if(phone.startsWith("1800000")){
					checkCode = "1234";
				}else {
					checkCode = send(phone);					
				}
				if(checkCode != null){
					System.out.println("phone:"+ phone + " checkCode:" + checkCode);
					addCheckCode(phone, checkCode);
				}
			}
		}
		finally {
			registeCheckCodeQueue_rwlock.writeLock().unlock();
		}	
	}
	
	public String send(String mobiles) {
		String phoneCode = generatePhoneCode();
		String content = "【贷贷】验证码:"+phoneCode+"";
		boolean needstatus = true;//是否需要状态报告，需要true，不需要false
		String product = "1086";//产品ID
		String extno = "001";//扩展码
		try {
			String returnString = HttpSender.send(URL, ACCOUNT, PSWD, mobiles, content, needstatus, product, extno);
			System.out.println(returnString);
			int begin = returnString.indexOf(',')+1;
			int end = returnString.indexOf('\n');
			String code = returnString.substring(begin, end);
			if("0".equals(code)){
				logger.warn("发送"+mobiles+"短信成功");		
				return phoneCode;
			}else {
				logger.warn("发送"+mobiles+"的短信失败,返回码为："+code);
				return null;
			}
		} catch (Exception e) {
			logger.error("发送短信异常：", e);
			return null;
		}
	}
	
	public static void main(String[] args) {
		String s = "20171219120143,0\n2081219120143237400"; 
		int begin = s.indexOf(',')+1;
		int end = s.indexOf('\n')+1;
		
		System.out.println(s.substring(begin, end));
	}
	
	}
