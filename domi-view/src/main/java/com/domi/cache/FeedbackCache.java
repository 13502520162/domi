package com.domi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.domi.mapper.FeedbackMapper;
import com.domi.model.Feedback;

public class FeedbackCache extends Cache {
	
	private static FeedbackCache m_instance = null;
	
	private FeedbackMapper feedbackMapper;
	
	private Map<Integer, Feedback> feedbackMap = new HashMap<>();
	private List<Feedback> feedbackList = new ArrayList<>();
	
	private ReentrantReadWriteLock feedbackMap_rwlock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock feedbackList_rwlock = new ReentrantReadWriteLock();
	
	private FeedbackCache(FeedbackMapper feedbackMapper) {
		List<Feedback> list = feedbackMapper.queryAll();
		for (Feedback feedback : list) {
			feedbackMap.put(feedback.getId(), feedback);
			feedbackList.add(feedback);
		}
		this.feedbackMapper = feedbackMapper;
	}
	
	public synchronized static FeedbackCache getInstance(FeedbackMapper feedbackMapper) {
		if (m_instance == null) {
			m_instance = new FeedbackCache(feedbackMapper);
		}
		return m_instance;
	}
	
	public boolean insertFeedback(Feedback feedback) {
		boolean result = false;
		feedbackList_rwlock.writeLock().lock();
		feedbackMap_rwlock.writeLock().lock();
		try {
			if (feedbackMapper.insert(feedback) == 1) {
				feedbackMap.put(feedback.getId(), feedback);
				feedbackList.add(feedback);
				result = true;
			}
		} finally {
			feedbackMap_rwlock.writeLock().unlock();
			feedbackList_rwlock.writeLock().unlock();
		}

		return result;
	}
	
	public List<Feedback> getFeedbackList(int pageIndex, int pageSize) {		
		List<Feedback> list = null;
		try {
			feedbackList_rwlock.readLock().lock();
			int len = this.feedbackList.size();
			if (len < pageSize) {
				list = this.feedbackList.subList(0, len);
			} else if ((len - pageIndex * pageSize) >= 0 && (len - (pageIndex + 1) * pageSize) < 0) {
				list = this.feedbackList.subList(0, (len - pageIndex * pageSize));
			} else if ((len - pageIndex * pageSize) < 0) {
				list = new ArrayList<>();
			} else {
				list = this.feedbackList.subList((len - (pageIndex + 1) * pageSize), (len - pageIndex * pageSize));
			}
		} finally {
			feedbackList_rwlock.readLock().unlock();
		}
		
		return list;
	}
	
	public int getFeedbackListSize() {
		int size = 0;
		try {
			feedbackList_rwlock.readLock().lock(); 
			size = this.feedbackList.size();
		} finally {
			feedbackList_rwlock.readLock().unlock();
		}
		
		return size;
	}
	
	public Feedback getFeedback(int id) {
		Feedback feedback = null;
		try {
			feedbackMap_rwlock.readLock().lock();
			feedback = feedbackMap.get(id);
		} finally {
			feedbackMap_rwlock.readLock().unlock();
		}
		
		return feedback;
	}
}
