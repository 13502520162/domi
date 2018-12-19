package com.domi.support.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LuckyThreadPool {

	private static LuckyThreadPool luckyThreadPool;
	private ExecutorService threadPool = null; 
	
	private LuckyThreadPool(){
		threadPool = Executors.newCachedThreadPool();
	}
	
	public static synchronized LuckyThreadPool getInstance(){
		if(luckyThreadPool==null){
			luckyThreadPool = new LuckyThreadPool();
		}
		return luckyThreadPool;
	} 
	
	public synchronized ExecutorService getThreadPool(){
		return threadPool;
	}
	
	
}
