package com.domi.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.domi.cache.ConfigCache;
import com.domi.cache.FeedbackCache;
import com.domi.cache.ManagerCache;
import com.domi.cache.PhoneTokenRecordCache;
import com.domi.cache.UserCache;
import com.domi.cache.VersionCache;
import com.domi.data.AppData;
import com.domi.data.UserData;
import com.domi.mapper.BannerMapper;
import com.domi.mapper.ConfigMapper;
import com.domi.mapper.FeedbackMapper;
import com.domi.mapper.InviteUserMapper;
import com.domi.mapper.ManagerMapper;
import com.domi.mapper.UserMapper;
import com.domi.mapper.VersionMapper;
import com.domi.model.Banner;
import com.domi.model.Config;
import com.domi.model.Feedback;
import com.domi.model.InviteUser;
import com.domi.model.Manager;
import com.domi.model.Version;
import com.domi.support.serverConfig.ServerParam;
import com.domi.service.util.Results;
import com.domi.support.utils.CustomCookie;
import com.domi.support.utils.MD5;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

@Service
public class AdminService {
    Logger log = Logger.getLogger(AdminService.class);

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private VersionMapper versionMapper;
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private PhoneTokenRecordCache phoneTokenRecordCache;
    @Autowired
    private InviteUserMapper inviteUserMapper;

    public boolean isPhoneExist(String phone) {
        boolean result = true;
        UserCache userCache = UserCache.getInstance(userMapper);
        int userId = userCache.getUserIdByPhone(phone);
        if (userId == -1) {
            result = false;
        }
        return result;
    }

    public Map<String, Object> generateReturnMap(int errcode, String info,
                                                 String method, Map<String, Object> data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errcode", errcode);
        map.put("info", info);
        map.put("method", method);
        map.put("data", data);
        return map;
    }

    public boolean validateManager(String managerName, String password)
            throws UnsupportedEncodingException {
        boolean result = false;
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        int id = managerCache.getIdByName(managerName);
        if (id == -1) {
            return result;
        }

        Manager manager = managerCache.getManager(id);
        if (manager.getPassword().equals(MD5.MD5Encrypt(password))) {
            result = true;
        }
        return result;
    }

    public Manager getManager(String managerName) {
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        int id = managerCache.getIdByName(managerName);
        Manager manager = managerCache.getManager(id);
        return manager;
    }

    public String updateLoginKey(String managerName)
            throws UnsupportedEncodingException {
        String loginKey = null;
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        int id = managerCache.getIdByName(managerName);
        if (id == -1) {
            return null;
        }
        Manager manager = managerCache.getManager(id);
        String oldKey = manager.getLoginKey();
        loginKey = CustomCookie.generateCookie(managerName, id);
        manager.setLoginKey(loginKey);
        if (managerMapper.update(manager) != 1) {
            manager.setLoginKey(oldKey);
        } else {
            managerCache.updateLoginKeyAndId(oldKey, loginKey);
        }
        return loginKey;
    }

    public boolean validateLoginKey(String loginKey)
            throws UnsupportedEncodingException {
        boolean result = false;
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        int id = managerCache.getIdByLoginKey(loginKey);
        if (id != -1) {
            result = true;
        }
        return result;
    }

    public String getNameByLoginKey(String loginKey) {
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        int id = managerCache.getIdByLoginKey(loginKey);
        Manager manager = managerCache.getManager(id);
        return manager.getName();
    }

 

    public boolean isVersionTypeExist(int type) {
        VersionCache versionCache = VersionCache.getInstance(versionMapper);
        if (versionCache.getVersion(type) == null) {
            return false;
        }
        return true;
    }

    


    public boolean updatePassWord(Manager manager)
            throws UnsupportedEncodingException {
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        Manager oldManager = managerCache.getManager(manager.getId());
        int oldType = oldManager.getType();
        String oldPassword = oldManager.getPassword();
        if (manager.getType() != -1) {
            oldManager.setType(manager.getType());
        }
        if (!"".equals(manager.getPassword())) {
            oldManager.setPassword(MD5.MD5Encrypt(manager.getPassword()));
        }
        if (managerMapper.updatePassword(oldManager) != 1) {
            oldManager.setType(oldType);
            oldManager.setPassword(oldPassword);
            return false;
        }
        return true;
    }

    public Map<String, Object> getManagers() {
        Map<String, Object> data = new HashMap<String, Object>();
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        List<Manager> list = managerCache.getManagers();
        data.put("list", list);
        return data;
    }

    public boolean delManager(int managerId) {
        boolean result = false;
        ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
        Manager manager = managerCache.getManager(managerId);
        if (null == manager) {
            return false;
        } else {
            if (managerMapper.delManager(managerId) == 1) {
                managerCache.delManager(manager);
                result = true;
            }
        }
        return result;
    }

    public List<Feedback> getFeedbackList(int pageIndex, int pageSize) {
        FeedbackCache feedbackCache = FeedbackCache.getInstance(feedbackMapper);
        return feedbackCache.getFeedbackList(pageIndex, pageSize);
    }

   

    public boolean addManager(Manager manager)
            throws UnsupportedEncodingException {
        boolean result = false;
        manager.setLoginKey(manager.getName());
        manager.setPassword(MD5.MD5Encrypt(manager.getPassword()));
        if (managerMapper.insert(manager) == 1) {
            ManagerCache managerCache = ManagerCache.getInstance(managerMapper);
            managerCache.addManager(manager);
            result = true;
        }
        return result;
    }

   
    public Map<String, Object> getBanners() {
//      BannerCache bannerCache = BannerCache.getInstance(bannerMapper);
      List<Banner> list = bannerMapper.queryAll();
      return Results.of()
              .put("list", list).toMap();
  }
   
   
    public int getFeedbackListSize() {
        FeedbackCache feedbackCache = FeedbackCache.getInstance(feedbackMapper);
        return feedbackCache.getFeedbackListSize();
    }
    
    
    
    public Map<String, Object> getAppData(String beginDate, String endDate){
    	if(endDate==null){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(new Date());
    		endDate = sdf.format(calendar.getTime());
    		calendar.add(Calendar.DATE, -6);
    		beginDate = sdf.format(calendar.getTime());
    	}
    	Map<String, Object> data = new HashMap<String, Object>();    	
    	List<AppData> appDatas = userMapper.queryAppData(beginDate, endDate);
    	data.put("list", appDatas);
    	return data;
    }

	public Map<String, Object> getUserData(String beginDate, String endDate,Integer hasSubmitInfo, String info) {
		Map<String, Object> data = new HashMap<String, Object>();    	
		if(endDate==null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			endDate = sdf.format(calendar.getTime());
			calendar.add(Calendar.DATE, -6);
			beginDate = sdf.format(calendar.getTime());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("info", info);
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		paramMap.put("hasSubmitInfo", hasSubmitInfo);		
		List<UserData> appDatas = userMapper.queryUserData(paramMap);
		Integer size = userMapper.queryUserCount(paramMap);
		data.put("list", appDatas);
		data.put("size", size);
		return data;
	}
	
	/**生成邀请帐号密码 by shexiao 2018-08-15*/
//	public boolean updateInviteUser(int userId, String account, String password) throws Exception {
//		InviteUser inviteUser = inviteUserMapper.getByUserId(userId);
//		if (inviteUser != null) {
//			return inviteUserMapper.updatePassword(userId, MD5.MD5Encrypt(password)) == 1;
//		} else {
//			userMapper.updateHadGenInviteLink(userId);
//			inviteUser = new InviteUser();
//			inviteUser.setUserId(userId);
//			inviteUser.setAccount(account);
//			inviteUser.setAddtime(new Date());
//			inviteUser.setPassword(MD5.MD5Encrypt(password));
//			return inviteUserMapper.insert(inviteUser) == 1;
//		}
//	}

	public Map<String, Object> getFeedBacks(int page, int pageSize, String phone, String beginDate, String endDate) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pageIndex", (page-1)*pageSize);
		paramMap.put("pageSize", pageSize);
		paramMap.put("phone", phone);
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		List<Feedback> feedbacks = feedbackMapper.queryForPage(paramMap);		
		int size = feedbackMapper.querySize();
		data.put("feedbackList", feedbacks);
		data.put("size", size);
		return data;
	}
    
    
    
	public String getUptoken() throws UnsupportedEncodingException {
		ServerParam serverParam = ServerParam.getInstance(false);
		Auth auth = Auth.create(serverParam.getAccessKey(), serverParam.getSecretKey());
		String uptoken = auth.uploadToken("ljdata", null, 3600, new StringMap().put("insertOnly", 0));
		return uptoken;
	
	}    
    
    
    
    
	public boolean updateConfig(Config config) {
        if (configMapper.update(config) == 1) {
        	Config newConfig = ConfigCache.getInstance(configMapper, true).getConfig();
System.out.println(newConfig);        	
        }
        return true;
    }

	
	public Map<String, Object> getConfig(){
		Map<String, Object> data = new HashMap<String, Object>();
		Config config = ConfigCache.getInstance(configMapper, false).getConfig();
		data.put("config", config);
		return data;
	}
    
	   public boolean addVersion(Version version) {
	        VersionCache versionCache = VersionCache.getInstance(versionMapper);
	        if (versionMapper.insert(version) == 1) {
	            versionCache.addVersion(version);
	            return true;
	        }
	        return false;
	    }
    
    public Map<String, Object> getInviteUserList(int start, int length) {
    	List<InviteUser> list = inviteUserMapper.getList(start, length);
    	int size = inviteUserMapper.getSize();
    	Map<String, Object> data = new HashMap<>();
    	data.put("list", list);
    	data.put("size", size);
    	return data;
    }
    public boolean addInviteUser(InviteUser inviteUser) {
    	return inviteUserMapper.insert(inviteUser) == 1;
    }
    public InviteUser getInviteUser(String account) {
    	return inviteUserMapper.getByAccount(account);
    }
}

