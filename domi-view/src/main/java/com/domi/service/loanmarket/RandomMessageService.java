package com.domi.service.loanmarket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domi.mapper.loanmarket.LoanPlatformMapper;


@Service
public class RandomMessageService {
	private static final Logger logger = Logger.getLogger(RandomMessageService.class);
	
	@Autowired
	private LoanPlatformMapper loanPlatformMapper;
	
	private Random random = new Random();
	private List<Map<String, String>> messageList = null;
	private Date refreshTime = null;
	private static final long tMin = 30 * 60 * 1000;
	
	public synchronized List<Map<String, String>> getRandomList() {
		if (messageList == null) {
			generateList();
			refreshTime = new Date();
		}
		if (refreshTime != null) {
			Date now = new Date();
			long duration = refreshTime.getTime() - now.getTime();
			if (duration > tMin) {
				generateList();
				refreshTime = now;
			}
		}
		
		return messageList;
	}
	
	private synchronized void generateList() {
		messageList = new ArrayList<>();
		List<String> names = loanPlatformMapper.queryAllNames();
		if (names == null || names.isEmpty()) {
			names = new ArrayList<>();
			names.add("分期呗");
		}
		int nameLen = names.size();
		
		for (int i = 0; i < 20; i++) {
			String fourNumber = getFourNumber();
			String name = names.get(random.nextInt(nameLen));
			String money = String.valueOf(((1 + random.nextInt(9)) * 1000));
			Map<String, String> map = new HashMap<>();
			map.put("phone", fourNumber);
			map.put("name", name);
			map.put("money", money);
			messageList.add(map);
		}
	}
	
	private String getFourNumber() {
		String[] phoneStart = {"131","132","133", "134", "135", "136", "137", "138", "139",
				"181","182", "183","184", "185", "186", "187", "188","189",
				"155","137", "159"
		}; 
		return phoneStart[new Random().nextInt(21)]+"****"+String.valueOf(1000 + random.nextInt(9000));
	}
	
	
	
}
