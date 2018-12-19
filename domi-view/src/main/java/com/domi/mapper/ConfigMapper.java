package com.domi.mapper;

import org.springframework.stereotype.Component;

import com.domi.model.Config;

@Component
public interface ConfigMapper {
	public int update(Config config);
	public Config query();
	
}
