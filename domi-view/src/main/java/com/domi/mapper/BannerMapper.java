package com.domi.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.domi.model.Banner;

@Component
public interface BannerMapper {
	public int insert(Banner banner);
	public int update(Banner banner);
	public List<Banner> queryAll();
	public Banner query(int id);
	public int delete(int id);
	
	public List<Banner> queryListByParam(int type);

	public List<Banner> getNewBanner();
}
