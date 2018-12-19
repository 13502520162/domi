package com.domi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domi.mapper.InviteDataMapper;
import com.domi.mapper.UserMapper;
import com.domi.model.InviteData;
import com.domi.support.utils.DateUtil;

@Service
public class InviteDataService {

	@Autowired
	private InviteDataMapper inviteDataMapper;
	@Autowired
	private UserMapper userMapper;
	
	public Map<String, Object> getDataListByUserId(int inviteId, String beginDate, String endDate, int start, int length) {
		Map<String, Object> data = new HashMap<>();
		List<InviteData> list =  inviteDataMapper.getList(inviteId, beginDate, endDate, start, length);
		int size = inviteDataMapper.getSize(inviteId, beginDate, endDate);
		data.put("list", list);
		data.put("size", size);
		return data;
	}
	
	public void updateInviteData(int inviteId, Date today) {
		int count = userMapper.getTodayInviteCount(inviteId);
		String inviteDate = DateUtil.getDateString(today, "yyyy-MM-dd");
		InviteData inviteData = new InviteData();
		inviteData.setInviteCount(count);
		inviteData.setInviteId(inviteId);
		inviteData.setInviteDate(inviteDate);
		inviteDataMapper.updateInviteData(inviteData);
		
	}
}
