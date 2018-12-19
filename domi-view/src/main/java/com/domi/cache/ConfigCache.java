package com.domi.cache;

import com.domi.mapper.ConfigMapper;
import com.domi.model.Config;

public class ConfigCache {
	
	private static ConfigCache m_instance = null;
	
	private Config config;
	
	
	private ConfigCache(ConfigMapper configMapper) {
		this.config = configMapper.query();
	}
	
	
	public static synchronized ConfigCache getInstance(ConfigMapper configMapper, boolean needReLoad) {
		if (m_instance == null || needReLoad) {
			m_instance = new ConfigCache(configMapper);
		}
		return m_instance;
	}
	
	
	public Config getConfig(){
		return config;
	}
	
	
}
