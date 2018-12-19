package com.domi.cache;


import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.domi.mapper.VersionMapper;
import com.domi.model.Version;
import com.domi.service.StateConstant.VersionType;


public class VersionCache {

	
	private Version andriodVersion;
	private Version iosVersion;
	
	private ReentrantReadWriteLock versionMap_rwLock = new ReentrantReadWriteLock();
	
	private static VersionCache m_instance = null;
	
   private VersionCache (VersionMapper versionMapper) {
    	
	   andriodVersion = versionMapper.queryNewVersionByType(VersionType.andriod);
	   iosVersion = versionMapper.queryNewVersionByType(VersionType.ios);
	   
    }
    //每次调用该工厂方法返回该实例
    public synchronized static VersionCache getInstance(VersionMapper versionMapper) {
    	if(m_instance == null){
    		m_instance = new VersionCache(versionMapper);
    	}
        return m_instance;  
    }
	
	
	
	public void addVersion(Version version){
		try {
			versionMap_rwLock.writeLock().lock();
			if(version.getType() == VersionType.andriod && Integer.valueOf(version.getCurrentVersion()) > andriodVersion.getCurrentVersion()){
				andriodVersion = version;
			}else if(version.getType() == VersionType.ios && version.getCurrentVersion() > iosVersion.getCurrentVersion()){
				iosVersion = version;
			}
			
		} finally {
			versionMap_rwLock.writeLock().unlock();
		}
	}
	
	
	public void updateVersion(Version version){
		try {
			versionMap_rwLock.writeLock().lock();
			
			if(version.getType() == VersionType.andriod && version.getCurrentVersion() == andriodVersion.getCurrentVersion()){
				andriodVersion.setDescription(version.getDescription());
				andriodVersion.setDownLoadUrl(version.getDownLoadUrl());
				andriodVersion.setIsForceUpdate(version.getIsForceUpdate());
				andriodVersion.setCurrentVersion(version.getCurrentVersion());
			}else if(version.getType() == VersionType.ios && version.getCurrentVersion() == iosVersion.getCurrentVersion()){
				iosVersion.setDescription(version.getDescription());
				iosVersion.setDownLoadUrl(version.getDownLoadUrl());
				iosVersion.setIsForceUpdate(version.getIsForceUpdate());
				iosVersion.setCurrentVersion(version.getCurrentVersion());
			}
		} finally {
			versionMap_rwLock.writeLock().unlock();
		}
	}
		
			
	
	public Version getVersion(int type){
		versionMap_rwLock.readLock().lock();
		try {
			if(VersionType.andriod == type){
				return andriodVersion;
			}else {
				return iosVersion;
			}
				
		} finally {
			versionMap_rwLock.readLock().unlock();
		}
	}	
	

	
}
