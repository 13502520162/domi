package com.domi.service.loanmarket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domi.cache.ConfigCache;
import com.domi.support.identification.StringUtil;
import com.domi.mapper.BannerMapper;
import com.domi.mapper.ConfigMapper;
import com.domi.mapper.UserMapper;
import com.domi.mapper.loanmarket.LoanPlatformMapper;
import com.domi.mapper.loanmarket.PlatformInfoMapper;
import com.domi.model.Banner;
import com.domi.model.Config;
import com.domi.model.User;
import com.domi.model.loanmarket.AppLoanPlatformDto;
import com.domi.model.loanmarket.LoanPlatform;
import com.domi.model.loanmarket.LoanPlatformData;
import com.domi.model.loanmarket.PlatformInfo;
import com.domi.model.loanmarket.VisitHistory;
import com.domi.model.loanmarket.VisitHistoryInfo;

@Service
public class LoanMarketService {
	private static final Logger logger = Logger.getLogger(LoanMarketService.class);
	private static final int PAGESIZE = 100;
	
	@Autowired
	private LoanPlatformMapper loanPlatformMapper;
	@Autowired
	private PlatformInfoMapper platformInfoMapper;
	@Autowired
	private com.domi.service.loanmarket.RandomMessageService randomMessageService;
	@Autowired
	private BannerMapper bannerMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private VisitHistoryMapper visitHistoryMapper;
	@Autowired
	private ConfigMapper configMapper;
	
	
	public int updateLoanPlatform(LoanPlatform loanPlatform) {
		loanPlatform.setAddTime(new Date());
		int result = 0;
		if (loanPlatform.getBeOnline() == 1) {//如果是立即
			loanPlatform.setState(1);
			loanPlatform.setHasSubscribe(1);
		} else if (loanPlatform.getBeOnline() == 2) {
			loanPlatform.setState(2);
			loanPlatform.setHasSubscribe(1);
			loanPlatform.setOfflineTime(new Date());
		}
		if (loanPlatform.getId() > 0) {
			result = loanPlatformMapper.update(loanPlatform);
		} else {
			result = loanPlatformMapper.insert(loanPlatform);
			
			PlatformInfo platformInfo = new PlatformInfo();
			platformInfo.setLoanPlatformId(loanPlatform.getId());
			platformInfoMapper.insert(platformInfo);
		}
		return loanPlatform.getId();
	}
	
	public LoanPlatform getLoanPlatform(int id) {
		LoanPlatform loanPlatform = loanPlatformMapper.queryById(id);
		loanPlatform.setRange(getRange(loanPlatform.getDayRange(), loanPlatform.getMonthRange()));
		return loanPlatform;
	}
	
	public Map<String, Object> getLoanPlatformList(int state, int page) {
		Map<String, Object> param = new HashMap<>();
		param.put("state", state);
		param.put("start", (page - 1) * PAGESIZE);
		param.put("length", PAGESIZE);
		
		List<LoanPlatform> list = loanPlatformMapper.queryListByParam(param);
		int size = loanPlatformMapper.querySizeByParam(param);
		
		if (list != null && !list.isEmpty()) {
			for (LoanPlatform item : list) {
				item.setRange(getRange(item.getDayRange(), item.getMonthRange()));
			}
		}
		
		Map<String, Object> data = new HashMap<>();
		data.put("list", list);
		data.put("size", size);
		return data;
	}
	
	public boolean updatePlatformInfo(PlatformInfo platformInfo) {
		return platformInfoMapper.update(platformInfo) > 0;
	}
	
	public Map<String, Object> getPlatformInfo(int loanPlatformId) {
		Map<String, Object> data = new HashMap<String, Object>(); 
		AppLoanPlatformDto dto = platformInfoMapper.queryByLoanPlatformIId(loanPlatformId);
		data.put("platformInfo", dto);
		return data;
	}
	
	/**app*/
	public Map<String, Object> getFirstPageInfo(int userId) {
		Map<String, Object> data = new HashMap<>();
		
		Map<String, Object> param = new HashMap<>();
		
		int hasSubmitInfo = 0;
		if(userId != -1){
			User user = userMapper.queryByUserId(userId);
			hasSubmitInfo = user.getHasSubmitInfo();
			if(hasSubmitInfo == 1){
				data.put("loanMoney", 10000);
				data.put("loanCount", 5);
			}
		}
		
		param.put("platformState", 1);
		List<AppLoanPlatformDto> newList = loanPlatformMapper.queryListByPlatformStateForTwo(param);
		
		for(AppLoanPlatformDto platform : newList){
			String range = getRange(platform.getDayRange(), platform.getMonthRange());
			platform.setRange(range);
		}
		
		List<Banner> bannerList = bannerMapper.queryListByParam(2);
		data.put("bannerList", bannerList);
		
		Config config = ConfigCache.getInstance(configMapper, false).getConfig();
		
		data.put("bank1ImgUrl", config.getBank1ImgUrl());
		data.put("bank1GotoUrl", config.getBank1GotoUrl());
		data.put("bank2ImgUrl", config.getBank2ImgUrl());
		data.put("bank2GotoUrl", config.getBank2GotoUrl());
		
		data.put("moreBankUrl", config.getMoreBankUrl());
		
		data.put("hasSubmitInfo", hasSubmitInfo);
		data.put("list", newList);
		data.put("slideList", randomMessageService.getRandomList());
		return data;
	}
	
	
	/**app*/
	public Map<String, Object> getLoanPlatforms(int userId, int page) {
		Map<String, Object> data = new HashMap<>();
		
		Map<String, Object> param = new HashMap<>();
		
		int hasSubmitInfo = 0;
		if(userId != -1){
			User user = userMapper.queryByUserId(userId);
			hasSubmitInfo = user.getHasSubmitInfo();
			if(hasSubmitInfo == 1){
				data.put("loanMoney", 10000);
				data.put("loanCount", 5);
			}
		}
		param.put("pageIndex", (page-1)*PAGESIZE);
		param.put("pageSize", PAGESIZE);
//		param.put("platformState", 1);
		List<AppLoanPlatformDto> newList = loanPlatformMapper.queryListByPlatformState(param);
		
		for(AppLoanPlatformDto platform : newList){
			String range = getRange(platform.getDayRange(), platform.getMonthRange());
			platform.setRange(range);
		}
		
//		param.put("platformState", 2);
//		List<AppLoanPlatformDto> highList = loanPlatformMapper.queryListByPlatformState(param);

		
		data.put("hasSubmitInfo", hasSubmitInfo);
		data.put("list", newList);
//		data.put("list", highList);
//		data.put("creditCardUrl", ServerConfig.getInstance().getCreditCardUrl());
		data.put("slideList", randomMessageService.getRandomList());
		return data;
	}
	
	
	public Map<String, Object> getLoanPlatformListByApp(int page, Integer minMoney, Integer maxMoney, Integer platformState, String ratioRate,
			String moneyRate, String userRole, String[] userMaterials, String[] userUsages) {
		Map<String, Object> param = new HashMap<>();
		param.put("start", (page - 1) * PAGESIZE);
		param.put("length", PAGESIZE);
		param.put("minMoney", minMoney);
		param.put("maxMoney", maxMoney);
		param.put("platformState", platformState);
		param.put("ratioRate", ratioRate);
		param.put("moneyRate", moneyRate);
		param.put("userRole", userRole);
		param.put("userMaterials", userMaterials);
		param.put("userUsages", userUsages);
		
		List<LoanPlatform> list = loanPlatformMapper.queryListByAppParam(param);
		Map<String, Object> data = new HashMap<>();
		data.put("list", list);
		return data;
	}
	
	public List<PlatformInfo> getAllPlatformInfo() {
		return platformInfoMapper.queryAll();
	}
	
	public void visitPlatform(int userId, int platformId, int type){
		VisitHistory db_visitHistory = visitHistoryMapper.queryByUserIdAndPlatformId(userId, platformId);
		LoanPlatform loanPlatform = loanPlatformMapper.queryById(platformId);
		if(loanPlatform == null) 
			return;
		VisitHistoryInfo visitHistoryInfo = new VisitHistoryInfo(userId, platformId, loanPlatform.getName(), type);
		visitHistoryMapper.insertVisitHistoryInfo(visitHistoryInfo);
		if(db_visitHistory==null){
			VisitHistory visitHistory = new VisitHistory();
			visitHistory.setPlatformId(platformId);
			visitHistory.setPlatformName(loanPlatform.getName());
			visitHistory.setIntro(loanPlatform.getPlatformDesc());
			visitHistory.setUserId(userId);
			visitHistory.setPlatformLogoUrl(loanPlatform.getLogo());
			visitHistoryMapper.insert(visitHistory);
		}else {
			db_visitHistory.setUpdateTime(new Date());
			if(type==0){
				db_visitHistory.setVisitCount(db_visitHistory.getVisitCount()+1);
			}else {
				db_visitHistory.setRegisteCount(db_visitHistory.getRegisteCount()+1);
				db_visitHistory.setHasRegiste(1);
			}
			visitHistoryMapper.update(db_visitHistory);
		}
	}
	
	
	public Map<String, Object> getVisitHistories(int userId, int page){
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("pageIndex", PAGESIZE*(page-1));
		paramMap.put("pageSize", PAGESIZE);
		List<VisitHistory> list = visitHistoryMapper.queryByUserId(paramMap);
		data.put("list", list);
		return data;
	}

	
	
	private String getRange(String dayRange, String monthRange) {
		String pre = null;
		String next = null;
		String range = "";
		String[] monthRanges = null;
		String[] dayRanges = null;
		if (!StringUtil.isEmpty(dayRange)) {
			dayRanges = dayRange.split(",");
			pre = dayRanges[0];
		}
		if (!StringUtil.isEmpty(monthRange)) {
			monthRanges = monthRange.split(",");
			next = monthRanges[monthRanges.length - 1];
		}
		if (pre != null && next != null) {
			range = pre + "天-" + next+"个月";
		} else if (pre != null) {
			if (dayRanges != null && dayRanges.length > 1) {
				range = pre + "-" + dayRanges[dayRanges.length - 1] + "天";
			} else {
				range = pre+"天";
			}
		} else {
			if (monthRanges != null && monthRanges.length > 1) {
				range = monthRanges[0] + "-" + next + "个月";
			} else {
				range = next;
			}
		}
		return range;
	}
	
		
	public Map<String, Object> getPlatformData(Integer state, String beginDate,	String endDate) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("state", state);
		param.put("beginDate", beginDate);
		param.put("endDate", endDate);
		List<LoanPlatformData> datas = loanPlatformMapper.queryPlatformData(param);
		data.put("list", datas);
		return data;
	}
	
	
	
	
	
	
	
	
	
	
	
}
