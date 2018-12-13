package com.domi.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.domi.data.UserCacheObj;
import com.domi.mapper.UserMapper;
import com.domi.model.User;
public class UserCache extends Cache {
	
	private static UserCache m_instance = null;
	private UserMapper userMapper;
	private Map<String,Integer> cookieAndUserIdMap = new HashMap<String,Integer>();
	private Map<String,Integer> phoneAndUserIdMap = new HashMap<String, Integer>();
	private Map<String,String> phoneAndCookieMap = new HashMap<String, String>();
	
	private ReentrantReadWriteLock cookieAndUserIdMap_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock phoneAndCookieMap_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock phoneAndUserIdMap_rwlock = new ReentrantReadWriteLock();
	private UserCache(UserMapper userMapper) {
		
		this.userMapper = userMapper;
		List<UserCacheObj> list = userMapper.queryAll();
		for(UserCacheObj cacheObj:list) {
			phoneAndUserIdMap.put(cacheObj.getCellphone(), cacheObj.getId());
			cookieAndUserIdMap.put(cacheObj.getCookie(), cacheObj.getId());
		}
	}

	public static synchronized UserCache getInstance(UserMapper userMapper) {
		
		if (m_instance == null) {
			m_instance = new UserCache(userMapper);
		}
		return m_instance;
	}
	
	public boolean insert(User user) {
		boolean result = false;
		phoneAndUserIdMap_rwlock.writeLock().lock();
		try {
			if (userMapper.insert(user) == 1) {
				phoneAndUserIdMap.put(user.getCellphone(), user.getId());
				
				result = true;
			}
		} finally {
			phoneAndUserIdMap_rwlock.writeLock().unlock();
		}
		return result;
	}
	

	
	public int getUserIdByPhone(String phone) {
		Integer userId;
		try {
			phoneAndUserIdMap_rwlock.readLock().lock();
			userId = phoneAndUserIdMap.get(phone);
		} finally {
			phoneAndUserIdMap_rwlock.readLock().unlock();
		}
		
		if (userId == null) {
			return -1;
		}
		return userId;
	}
	
	public boolean addCookieAndUserId(String cookie, String phone ,int userId) {
		boolean result = false;
		try {
			cookieAndUserIdMap_rwlock.writeLock().lock();
			phoneAndCookieMap_rwlock.writeLock().lock();
			
			String tempCookie = phoneAndCookieMap.get(phone);
			if (tempCookie != null) {
				cookieAndUserIdMap.remove(tempCookie);
			}
			phoneAndCookieMap.put(phone, cookie);
			cookieAndUserIdMap.put(cookie, userId);
			
			User user = userMapper.queryByUserId(userId);
			
			if(user != null){
				user.setCookie(cookie);
				userMapper.updateCookie(user);
			}
			result = true;
		} finally {
			phoneAndCookieMap_rwlock.writeLock().unlock();
			cookieAndUserIdMap_rwlock.writeLock().unlock();
		}

		return result;
	}
	
	public int getUserIdByCookie(String cookie) {
		Integer userId;
		try {
			cookieAndUserIdMap_rwlock.readLock().lock();
			userId = cookieAndUserIdMap.get(cookie);
		} finally {
			cookieAndUserIdMap_rwlock.readLock().unlock();
		}
		
		if (userId == null) {
			return -1;
		}
		return userId;
	}

	
	
}
