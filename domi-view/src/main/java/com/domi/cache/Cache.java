package com.domi.cache;


public class Cache {
	
	public Cache() {
		
		DetectCacheDeadlock detectCacheDeadlock = DetectCacheDeadlock.getInstance();
		detectCacheDeadlock.addCache(this);
	}
}
