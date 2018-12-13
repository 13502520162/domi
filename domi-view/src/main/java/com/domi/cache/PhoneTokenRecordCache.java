package com.domi.cache;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;

import com.domi.support.utils.DateUtil;

/**
 * 记录1天内统一设备出现次数
 * @author shexiao
 *
 */
@Component
public class PhoneTokenRecordCache {
	
	private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> dateTokenCount = new ConcurrentHashMap<>();
	
	private ReentrantReadWriteLock dateTokenCountLock = new ReentrantReadWriteLock();
	

	public void addOne(String token) {
		dateTokenCountLock.writeLock().lock();
		try {
			String todayString = getTodayString();
			ConcurrentHashMap<String, Integer> tokenCount = dateTokenCount.get(todayString);
			if (tokenCount == null) {
				tokenCount = new ConcurrentHashMap<>();
				dateTokenCount.put(todayString, tokenCount);
			}
			
			Integer count = tokenCount.get(token);
			if (count == null) {
				tokenCount.put(token, 1);
			} else {
				tokenCount.put(token, count + 1);
			}
		} finally {
			dateTokenCountLock.writeLock().unlock();
		}
	}
	
	public int getCount(String token) {
		dateTokenCountLock.readLock().lock();
		try {
			String todayString = getTodayString();
			ConcurrentHashMap<String, Integer> tokenCount = dateTokenCount.get(todayString);
			if (tokenCount == null) {
				return 0;
			}
			
			Integer count = tokenCount.get(token);
			if (count == null) {
				return 0;
			} 
			return count;
		} finally {
			dateTokenCountLock.readLock().unlock();
		}
	}
	
	private String getTodayString() {
		return DateUtil.getDateString(new Date(), "yyyyMMdd");
	}
	
	public static void main(String[] args) {
		PhoneTokenRecordCache cache = new PhoneTokenRecordCache();
		System.out.println(cache.getCount("123"));
		cache.addOne("123");
		cache.addOne("345");
		cache.addOne("123");
		System.out.println(cache.getCount("345"));
		cache.addOne("123");
		System.out.println(cache.getCount("123"));
	}
}
