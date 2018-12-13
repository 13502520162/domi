package com.domi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.domi.mapper.InviteDataMapper;
import com.domi.mapper.InviteUserMapper;
import com.domi.model.InviteData;
import com.domi.model.InviteUser;
import com.domi.support.utils.DateUtil;

public class InviteDataTask {
	Logger log = Logger.getLogger(InviteDataTask.class);
	
	@Autowired
	private InviteUserMapper inviteUserMapper;
	@Autowired
	private InviteDataMapper inviteDataMapper;
	
    public void initTomorrowInviteData() {
        try {
        	System.out.println("初始化所有用户明天的邀请数据...");
        	List<InviteUser> userList = inviteUserMapper.getAll();
        	Date tomorrow = DateUtil.caculateEndDate(new Date(), 1);
        	String inviteDate = DateUtil.getDateString(tomorrow, "yyyy-MM-dd");
        	if (!userList.isEmpty()) {
        		List<InviteData> dataList = new ArrayList<>();
        		for (InviteUser inviteUser : userList) {
        			InviteData inviteData = new InviteData();
        			inviteData.setInviteCount(0);
        			inviteData.setInviteDate(inviteDate);
        			inviteData.setInviteId(inviteUser.getInviteId());
        			dataList.add(inviteData);
        		}
        		inviteDataMapper.insertMany(dataList);
        	}
        } catch (Exception e) {
        	log.error("初始化明天邀请数据出错", e);
        }
    }  
}
