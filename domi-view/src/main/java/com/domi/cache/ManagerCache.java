package com.domi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.domi.mapper.ManagerMapper;
import com.domi.model.Manager;

public class ManagerCache extends Cache {
	
	private static ManagerCache m_instance = null;
	
	private Map<Integer, Manager> managerMap = new HashMap<Integer, Manager>();
	private Map<String, Integer> nameAndIdMap = new HashMap<String, Integer>();
	private Map<String, Integer> loginKeyAndIdMap = new HashMap<String, Integer>();
	private List<Manager> managers = new ArrayList<Manager>();
	
	private ReentrantReadWriteLock managerMap_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock nameAndIdMap_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock loginKeyAndIdMap_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock managers_rwlock = new ReentrantReadWriteLock();
	
	
	private ManagerCache(ManagerMapper managerMapper) {
		managers = managerMapper.queryAll();
		for (Manager manager : managers) {
			managerMap.put(manager.getId(), manager);
			nameAndIdMap.put(manager.getName(), manager.getId());
			if (manager.getLoginKey().equals("")) {
				manager.setLoginKey(manager.getName());
			}
			loginKeyAndIdMap.put(manager.getLoginKey(), manager.getId());
		}
	}
	
	public synchronized static ManagerCache getInstance(ManagerMapper managerMapper) {
		if (m_instance == null) {
			m_instance = new ManagerCache(managerMapper);
		}
		return m_instance;
	}
	
	public int getIdByName(String managerName) {
		Integer id ;
		try {
			nameAndIdMap_rwlock.readLock().lock();
			id = nameAndIdMap.get(managerName);
			if (id == null) {
				id = -1;
			}
		} finally {
			nameAndIdMap_rwlock.readLock().unlock();
		}
		
		return id;
	}
	
	public int getIdByLoginKey(String loginKey) {
		Integer id ;
		try {
			loginKeyAndIdMap_rwlock.readLock().lock();
			id = loginKeyAndIdMap.get(loginKey);
			if (id == null) {
				id = -1;
			}
		} finally {
			loginKeyAndIdMap_rwlock.readLock().unlock();
		}
		return id;
	}
	
	public Manager getManager(int id) {
		Manager manager;
		try {
			managerMap_rwlock.readLock().lock();
			manager = managerMap.get(id);
		} finally {
			managerMap_rwlock.readLock().unlock();
		}
		
		return manager;
	}
	
	public boolean updateLoginKeyAndId(String oldLoginKey, String newLoginKey) {
		boolean result = false;
		Integer id ;
		try {
			loginKeyAndIdMap_rwlock.writeLock().lock();
			id = loginKeyAndIdMap.get(oldLoginKey);
			if (id != null) {
				loginKeyAndIdMap.put(newLoginKey, id);
				loginKeyAndIdMap.remove(oldLoginKey);
				result = true;
			}
		} finally {
			loginKeyAndIdMap_rwlock.writeLock().unlock();
		}
		
		return result;
	}
	
	
	public void addManager(Manager manager){
		managerMap_rwlock.writeLock().lock();
		nameAndIdMap_rwlock.writeLock().lock();
		loginKeyAndIdMap_rwlock.writeLock().lock();
		managers_rwlock.writeLock().lock();
		try {
			managerMap.put(manager.getId(), manager);
			nameAndIdMap.put(manager.getName(), manager.getId());
			if (manager.getLoginKey().equals("")) {
				manager.setLoginKey(manager.getName());
			}
			loginKeyAndIdMap.put(manager.getLoginKey(), manager.getId());			
			managers.add(manager);
		} finally{
			managerMap_rwlock.writeLock().unlock();
			nameAndIdMap_rwlock.writeLock().unlock();
			loginKeyAndIdMap_rwlock.writeLock().unlock();
			managers_rwlock.writeLock().unlock();
		}
		
	}
	
	public void delManager(Manager manager){
		managerMap_rwlock.writeLock().lock();
		nameAndIdMap_rwlock.writeLock().lock();
		loginKeyAndIdMap_rwlock.writeLock().lock();
		managers_rwlock.writeLock().lock();
		try {
			managerMap.remove(manager.getId());
			nameAndIdMap.remove(manager.getName());
			loginKeyAndIdMap.remove(manager.getLoginKey());
			managers.remove(manager);			
		} finally{
			managerMap_rwlock.writeLock().unlock();
			nameAndIdMap_rwlock.writeLock().unlock();
			loginKeyAndIdMap_rwlock.writeLock().unlock();
			managers_rwlock.writeLock().unlock();
		}
	}
	
	
	
	public List<Manager> getManagers(){
		List<Manager> list = new ArrayList<Manager>();
		managers_rwlock.readLock().lock();
		try {
			list = new ArrayList<Manager>(managers);			
		} finally{
			managers_rwlock.readLock().unlock();
		}
		return list;
		
	}
	
	
}
