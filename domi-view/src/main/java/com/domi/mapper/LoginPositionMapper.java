package com.domi.mapper;

import org.springframework.stereotype.Component;

import com.domi.model.LoginPosition;

@Component
public interface LoginPositionMapper {
	public int insert(LoginPosition loginPosition);
	
}
