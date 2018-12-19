package com.domi.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.domi.mapper.ManagerMapper;
import com.domi.mapper.UserMapper;
import com.domi.mapper.loanmarket.LoanPlatformMapper;
import com.domi.model.loanmarket.LoanPlatform;
import com.domi.service.AdminService;
@Controller
public class AutoController {
	
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ManagerMapper managerMapper; 
	@Autowired
	private AdminService adminService;
	@Autowired
	private LoanPlatformMapper loanPlatformMapper;
	
	Logger log = Logger.getLogger(AutoController.class);
	
	private Timer onlineLoanPlatform = new Timer(true);//预约上线借款平台 by shexiao 2017-10-30
	public  AutoController(){
		System.out.println("autoController init...");
		
	Calendar calendar6 = Calendar.getInstance();
	calendar6.setTime(new Date());
	calendar6.add(Calendar.MILLISECOND, 500);
	onlineLoanPlatform.schedule(onlineLoanPlatformTask, calendar6.getTime(), 1000*60*10);	
}

	private TimerTask onlineLoanPlatformTask = new TimerTask() {
        @Override
        public void run() {
        	try {
        		
        		List<LoanPlatform> list = loanPlatformMapper.queryToBeOnlineList();
        		if (list != null && !list.isEmpty()) {
        			Date now = new Date();
        			for (LoanPlatform item : list) {
        				if (item.getSubscribeTime().before(now)) {
        					item.setState(1);
        					item.setHasSubscribe(1);
        					loanPlatformMapper.updateState(item);
        				}
        			}
        		}
        	} catch (Exception e) {
        		log.error("上线预约平台任务失败", e);
        	}
        }
    };
}
