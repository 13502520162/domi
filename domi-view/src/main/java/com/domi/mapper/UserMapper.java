package com.domi.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.domi.data.AppData;
import com.domi.data.UserCacheObj;
import com.domi.data.UserData;
import com.domi.model.User;

@Component
public interface UserMapper {
	
	public int insert(User user);
	public List<UserCacheObj> queryAll();
		
	public User queryByUserId(int id);
	
	public int updateCookie(User user);
	public int updatePassword(User user);
	
	public int updateLoanInfo(User user);
	public List<AppData> queryAppData(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
	
	public List<UserData> queryUserData(Map<String, Object> paramMap);
	
	public Integer queryUserCount(Map<String, Object> paramMap);
	
	public User queryByInfo(String info);
	
	public void updateAddress(@Param("id") int userId, @Param("address")String address);
	
	int updateHadGenInviteLink(@Param("userId") int userId);
	int getTodayInviteCount(@Param("inviteId") int inviteId);
	
}
