
package com.domi.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.util.JSONUtils;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.domi.cache.ConfigCache;
import com.domi.cache.UserCache;
import com.domi.mapper.BannerMapper;
import com.domi.mapper.ConfigMapper;
import com.domi.mapper.FeedbackMapper;
import com.domi.mapper.LoginPositionMapper;
import com.domi.mapper.UserMapper;
import com.domi.mapper.VersionMapper;
import com.domi.mapper.loanmarket.LoanPlatformMapper;
import com.domi.model.Config;
import com.domi.model.Feedback;
import com.domi.model.LoginPosition;
import com.domi.model.SafeCall;
import com.domi.model.User;
import com.domi.support.utils.CustomCookie;
import com.domi.support.utils.HttpUtil;
import com.domi.support.utils.MD5;
import com.domi.support.utils.OurMD5;
import com.qiniu.util.StringMap;

@Service
public class AppService {

		
	Logger log = Logger.getLogger(AppService.class);
	
	@Autowired
	private ApplicationContext ctx;
	
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private FeedbackMapper feedbackMapper;
	@Autowired
	private VersionMapper versionMapper;
	@Autowired
	private BannerMapper bannerMapper;
	@Autowired
	private ConfigMapper configMapper;
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private LoginPositionMapper positionMapper;
	@Autowired
	private LoanPlatformMapper loanPlatformMapper;
	
	
	public boolean isPhoneExist(String phone) {
		boolean result = true;
		UserCache userCache = UserCache.getInstance(userMapper);
		int userId = userCache.getUserIdByPhone(phone);
		if (userId == -1 ) {
			result = false;
		}
		return result;
	}
	
	public boolean validateSercretKey(String phone, String password, String secretKey) throws UnsupportedEncodingException {
		boolean result = false;
		String md5 = OurMD5.encode(phone, password);
		if (md5.equals(secretKey)) {
			result = true;
		}
		return result;
	}
	
	public Map<String, Object> generateReturnMap(int errcode, String info, String method, Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("errcode", errcode);
		map.put("info", info);
		map.put("method", method);
		map.put("data", data);
		return map;
	}


    public boolean validateUser(User user) {
        boolean result = true;
        String phone = user.getCellphone();
        if (phone.length() <= 0) {
            result = false;
        }
        return result;
    }

    public boolean addUser(User user) throws UnsupportedEncodingException {
        UserCache userCache = UserCache.getInstance(userMapper);        
        return userCache.insert(user);
    }

    public int validateLogin(String phone, String password) throws UnsupportedEncodingException {

        UserCache userCache = UserCache.getInstance(userMapper);
        int userId = userCache.getUserIdByPhone(phone);

        if (userId == -1) {
            return 1;
        }
        User user = userMapper.queryByUserId(userId);
        if (password.equals(user.getPassword())) {
            return 0;
        } else {
            return 2;
        }
    }

    public boolean changePassword(User user) throws UnsupportedEncodingException {
        boolean result = false;
        String password = user.getPassword();
        UserCache userCache = UserCache.getInstance(userMapper);
        int userId = userCache.getUserIdByPhone(user.getCellphone());
        if (userId == -1) {
            return result;
        }

        user = userMapper.queryByUserId(userId);
        String oldPassword = user.getPassword();
        user.setPassword(password);
        if (userMapper.updatePassword(user) == 1) {
            result = true;
        } else {
            user.setPassword(oldPassword);
        }
        return result;

    }


    public boolean validateCookie(String cookie) {
        boolean result = false;
        if (cookie == null) {
            return result;
        }
        UserCache userCache = UserCache.getInstance(userMapper);
        int userId = userCache.getUserIdByCookie(cookie);
        if (userId != -1) {
            result = true;
        }
        return result;
    }

    public String addCookieAndUserId(String phone) throws UnsupportedEncodingException {
        UserCache userCache = UserCache.getInstance(userMapper);
        int userId = userCache.getUserIdByPhone(phone);
        String cookie = CustomCookie.generateCookie(phone, userId);
        if (userCache.addCookieAndUserId(cookie, phone, userId)) {
            return cookie;
        }
        return null;
    }

    public boolean validateSercretKey(User user, String secretKey) throws UnsupportedEncodingException {
        boolean result = false;
        String md5 = OurMD5.encode(user.getCellphone(), user.getPassword());
        if (md5.equals(secretKey)) {
            result = true;
        }
        return result;
    }

    public int getUserIdByCookie(String cookie) {
        UserCache userCache = UserCache.getInstance(userMapper);
        return userCache.getUserIdByCookie(cookie);
    }

   
  
    public Map<String, Object> getServiceInfo() {
        Map<String, Object> data = new HashMap<String, Object>();
        Config config = ConfigCache.getInstance(configMapper, false).getConfig();
        data.put("servicePhone", config.getServicePhone());
        data.put("serviceQQ", config.getServiceQQ());
        data.put("serviceWx", config.getServiceWx());
        return data;
    }
    

    /**
     * 注册
     * @param user
     * @return
     * @author chenhuanshuo
     * @throws Exception 
     * @Date 2017年7月27日
     */
	public boolean register(User user) throws Exception {
		boolean result=false;
		if(addUser(user)){
			result=true;
		}
		return result;
	}
    
	public void uploadFeedback(int userId, String content) {
		User user = userMapper.queryByUserId(userId);
		Feedback feedback = new Feedback();
		feedback.setPhone(user.getCellphone());
		feedback.setContent(content);
		feedbackMapper.insert(feedback);
	}
	
	public boolean submitLoanInfo(int userId, User user, String ip) {
		boolean result = false;		 
		user.setId(userId);		
		if(userMapper.updateLoanInfo(user)>0){
			pushToSafe(userId, user.getCellphone(), user.getIdNumber(), ip);
			result = true;
		}
		return result;		
	}

	public void positionForLogin(int userId, String address) {
		LoginPosition position = new LoginPosition(userId, address);
		if(positionMapper.insert(position)==1){
			userMapper.updateAddress(userId, address);
		}
	}
	
	public void pushToSafe(int userId, String cellphone, String idcard, String ip){
		try {
			String sign = MD5.MD5Encrypt(cellphone+"ljinsywl4qpc7p9Ywx");
			String url = "http://i.95baoxian.com/api/yiwaixian/reg?c=ljin&v="+sign;
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", "陈洋洋");
			/*map.put("birthday", "1991-10-02");
			map.put("sex", "1");*/
			map.put("idcard", idcard);
			map.put("phone", cellphone);
			map.put("ua", "");
			map.put("ip", ip);
			String request = JSONUtils.valueToString(map);
			String response = HttpUtil.doPost(url, map);
			JSONObject jsonObject = new JSONObject(response);
			String errCode = jsonObject.getString("errCode");
			SafeCall safeCall = new SafeCall(userId, request, response, errCode);
			loanPlatformMapper.insertSafeCall(safeCall);
		} catch (Exception e) {
			log.error("推送给保险失败", e);
		}
		
	}	
	
	
}




