package com.domi.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.domi.model.Feedback;


@Component
public interface FeedbackMapper {
	public int insert(Feedback feedback);
	public int update(Feedback feedback);
	public List<Feedback> queryAll();
	
	public List<Feedback> queryForPage(Map<String, Object> map);
	
	public int querySize();
}
