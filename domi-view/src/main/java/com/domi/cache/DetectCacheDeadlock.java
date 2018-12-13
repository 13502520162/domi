package com.domi.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;


public class DetectCacheDeadlock {
	
	private List<Cache> cacheList = new ArrayList<Cache>();
	private Logger log = Logger.getLogger(DetectCacheDeadlock.class);
	private static DetectCacheDeadlock m_instance = null;
	
	public static synchronized DetectCacheDeadlock getInstance() {
		if (m_instance == null) {
			m_instance = new DetectCacheDeadlock();
			
		}
		return m_instance;
	}
	
	public void detect(){
		
		for(Cache cache : cacheList){
			Class classTmp = cache.getClass();
			Class<?> cacheClass = null;
	        try {
	        	cacheClass = Class.forName(classTmp.getName());
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.error("exception in detect",e);
	        }

	        Field[] field = cacheClass.getDeclaredFields();
	        for (int i = 0; i != field.length; i++) {
	        	try {
	        		// 属性类型
		            Class<?> type = field[i].getType();
		            
		            if(type.getName().contains("ReentrantReadWriteLock")){
		            	field[i].setAccessible(true);
		            	ReentrantReadWriteLock reentrantReadWriteLockTmp = (ReentrantReadWriteLock)field[i].get(cache);
		            	reentrantReadWriteLockTmp.writeLock().lock();
		                reentrantReadWriteLockTmp.writeLock().unlock();
		            }
	        	}
		        catch (Exception e) {
		            e.printStackTrace();
		            log.error("exception in detect",e);
		        }
	        }
		}
	}
	
	public void addCache(Cache cache){
		cacheList.add(cache);
	}
}
