package com.domi.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.domi.model.loanmarket.VisitHistory;
import com.domi.model.loanmarket.VisitHistoryInfo;

@Component
public interface VisitHistoryMapper {
	
	public int insert(VisitHistory visitHistory);
	public int insertVisitHistoryInfo(VisitHistoryInfo visitHistoryInfo);
	
	public int update(VisitHistory visitHistory);
	public List<VisitHistory> queryByUserId(Map<String, Object> map);
	
	public VisitHistory queryByUserIdAndPlatformId(@Param("userId") int userId,@Param("platformId")  int platformId);
	
}
