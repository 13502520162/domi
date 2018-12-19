package com.domi.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.domi.cache.ConfigCache;
import com.domi.cache.PhoneCheckCodeCache;
import com.domi.cache.VersionCache;
import com.domi.mapper.ConfigMapper;
import com.domi.mapper.VersionMapper;
import com.domi.model.Config;
import com.domi.model.User;
import com.domi.model.Version;
import com.domi.service.AdminBasicService;
import com.domi.service.AdminService;
import com.domi.service.AppService;
import com.domi.service.util.randomCode.RandomNum;
import com.domi.support.utils.ExceptionParamLog;
import com.domi.support.utils.MD5;
import com.domi.support.utils.MapUtils;
@Controller
@RequestMapping("app")
public class AppController {
	@Autowired
	private com.domi.controller.AutoController autoController;
	@Autowired
	private AppService appService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private VersionMapper versionMapper;
	@Autowired
	private AdminBasicService adminBasicService;
	@Autowired
	private ConfigMapper configMapper;
	
	
	Map<String, String> codeMap = new HashMap<String, String>();
	Map<String, String> randomMap = new HashMap<String, String>();
	Logger log = Logger.getLogger(AppController.class);
	
	
	
	@RequestMapping("isRegiste")
	@ResponseBody
	public Map<String, Object> isRegiste(HttpServletRequest request,String cellphone) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {
			if(cellphone==null||cellphone==""){
				map = appService.generateReturnMap(1, "手机号不能为空", request.getRequestURI(), data);
				return map;
			}else{
				if(!appService.isPhoneExist(cellphone)){
					data.put("isRegiste", 0);					
					String randomNum = RandomNum.getRandomNum();
					data.put("randomNum",randomNum);
					randomMap.put(cellphone, randomNum+","+new Date().getTime());
					System.out.println("手机号为:"+cellphone+">>>加密后的signKey为:"+MD5.MD5Encrypt("daidai"+cellphone+randomNum));
				}else {
					data.put("isRegiste", 1);					
				}
			}
			map = appService.generateReturnMap(0, "成功！", request.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
			return map;
		}
	}
	
	
	
	@RequestMapping("getRandomNum")
	@ResponseBody
	public Map<String, Object> getRandomNum(HttpServletRequest request,String cellphone,Integer type) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {
			if(cellphone==null||cellphone==""){
				map = appService.generateReturnMap(1, "手机号不能为空", request.getRequestURI(), data);
				return map;
			}else{
				if(type==1){
					if(!appService.isPhoneExist(cellphone)){
						map = appService.generateReturnMap(3, "手机号码不存在!", request.getRequestURI(), data);
						return map;
					}
				}else {
		            if(appService.isPhoneExist(cellphone)){
						map = appService.generateReturnMap(4, "手机号码已经被注册，请更换手机号码!", request.getRequestURI(), data);
						return map;
					}
				}
				if(randomMap.get(cellphone)!=null){
					if(randomMap.get(cellphone).split(",").length>=2){
						if((long)new Date().getTime()-Long.parseLong(randomMap.get(cellphone).split(",")[1])<=60000){//一分钟内不重发
							map = appService.generateReturnMap(5, "不要频繁请求", request.getRequestURI(), data);
							return map;
						}
					}
				}
			}
			String randomNum = RandomNum.getRandomNum();
			data.put("randomNum",randomNum);
			randomMap.put(cellphone, randomNum+","+new Date().getTime());
			System.out.println("手机号为:"+cellphone+">>>加密后的signKey为:"+MD5.MD5Encrypt("cxxYH"+cellphone+randomNum));
			map = appService.generateReturnMap(0, "成功！", request.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
			return map;
		}
	}
	
	
	
		@RequestMapping("register")
		@ResponseBody
		public Map<String, Object> register(HttpServletRequest request, User user, String secretKey) {
			Map<String, Object> map = null;
			Map<String, Object> data = new HashMap<>();
			try{
				if (!appService.validateUser(user)) {
					map = appService.generateReturnMap(1, "手机或者密码不符合长度要求", "register", data);
					return map;
				}
				
				boolean isSecretKeyRight = appService.validateSercretKey(user, secretKey);
				if (!isSecretKeyRight) {
					map = appService.generateReturnMap(2, "密钥不正确", "register", data);
					return map;
				}
				
				boolean isExist = appService.isPhoneExist(user.getCellphone());
				if (isExist) {
					map = appService.generateReturnMap(3, "此用户已存在", "register", data);
					return map;
				}
				
				
				boolean result = appService.register(user);
				if (!result) {
					map = appService.generateReturnMap(4, "注册失败", "register", data);
					return map;
				}
				else{
					/*String ip = IpUtils.getClientIP(request);
					data.put("startPingang",ServerConfig.getInstance().getStartPingang());*/
					String cookie =  appService.addCookieAndUserId(user.getCellphone());
					int startInsurance = ConfigCache.getInstance(configMapper, false).getConfig().getStartPingang();
					data.put("startInsurance", startInsurance);
					data.put("cookie", cookie);
					
					map = appService.generateReturnMap(0, "注册成功", "register", data);
					return map;
				}
				
			} catch (Exception e) {
				log.error("exception in register requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
				map = appService.generateReturnMap(10, "注册出问题啦，等我们今晚回去撸死程序猿！", "register", data);
				return map;
			}
		}
		
	
	//登录
	@RequestMapping("login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request, User user,String secretKey) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try{		
			if (!appService.validateUser(user)) {
				map = appService.generateReturnMap(4, "手机或者密码不符合长度要求", "login", data);
				return map;
			}
			
			int result = appService.validateLogin(user.getCellphone(), user.getPassword());
			if (result == 1) {
				map = appService.generateReturnMap(3, "手机号不存在，请先注册", "login", data);
				return map;
			}
			if (result == 2) {
				map = appService.generateReturnMap(5, "密码错误", "login", data);
				return map;
			}
			
			boolean isSecretKeyRight = appService.validateSercretKey(user, secretKey);
			if (!isSecretKeyRight) {
				map = appService.generateReturnMap(2, "密钥不正确", "login", data);
				return map;
			}
			
			String cookie =  appService.addCookieAndUserId(user.getCellphone());
			data = new HashMap<>();
			data.put("cookie", cookie);

			map = appService.generateReturnMap(0, "登录成功", "login", data);
			return map;
		} catch (Exception e) {
			log.error("exception in login requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "登陆出问题啦，等我们今晚回去撸死程序猿！", "login", data);
			return map;
		}
	}
	
	
	//修改密码
	@RequestMapping("changePassword")
	@ResponseBody
	public Map<String, Object> changePassword(HttpServletRequest request, User user,String secretKey) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try{		
			if (!appService.validateUser(user)) {
				map = appService.generateReturnMap(1, "手机或者密码不符合长度要求", "changePassword", data);
				return map;
			}
			
			boolean isSecretKeyRight = appService.validateSercretKey(user, secretKey);
			if (!isSecretKeyRight) {
				map = appService.generateReturnMap(2, "密钥不正确", "changePassword", data);
				return map;
			}
			
			
			if (!appService.changePassword(user)) {
				map = appService.generateReturnMap(3, "此帐号不存在，请先注册", "changePassword", data);
				return map;
			}
			
			map = appService.generateReturnMap(0, "修改成功", "changePassword", data);
			return map;
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
			return map;
		}
	}
	
	
	/**
	 * Md5验证方式获取手机验证码
	 * 加密规则 cxxYH+cellphone+随机数
	 * @param request
	 * @param cellphone
	 * @param signKey 加密后的key
	 * @param type
	 * @return
	 * @author chenhuanshuo
	 * @Date 2017年8月21日
	 */
	@RequestMapping("getCellPhoneCode")   
	@ResponseBody
	public Map<String, Object> getCellPhoneCode(HttpServletRequest request, String cellphone,String signKey,Integer type) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {
			if(cellphone==null||cellphone==""||signKey==null||signKey==""){
				map = appService.generateReturnMap(1,"手机号和signKey不能为空" , request.getRequestURI(), data);
				return map;
			}else{
				if(type==1){
					if(!appService.isPhoneExist(cellphone)){
						map = appService.generateReturnMap(3, "手机号码不存在!", request.getRequestURI(), data);
						return map;
					}
				}else{
		            if(appService.isPhoneExist(cellphone)){
						map = appService.generateReturnMap(4, "手机号码已经被注册，请更换手机号码!", request.getRequestURI(), data);
						return map;
					}
				}
				if(randomMap.get(cellphone)!=null){
					String randomNum = (randomMap.get(cellphone).split(","))[0];
					if(!signKey.equalsIgnoreCase(MD5.MD5Encrypt("daidai"+cellphone+randomNum))){
						map = appService.generateReturnMap(5, "signKey不正确", request.getRequestURI(), data);
						return map;
					}else{
						if(randomMap.get(cellphone+type)!=null){
							String[] randomArr = randomMap.get(cellphone+type).split(",");
							if(randomArr.length>=2){
								if((long)new Date().getTime()-Long.parseLong(randomArr[1])<=55000){
									map = appService.generateReturnMap(6, "不要频繁请求发送验证码", request.getRequestURI(), data);
									return map;
								}else{
									randomMap.remove(cellphone+type);
									randomMap.remove(cellphone);
									if(signKey.equalsIgnoreCase(randomArr[0])){
										map = appService.generateReturnMap(7, "请重新获取验证码", request.getRequestURI(), data);
										return map;
									}
								}
							}else{
								map = appService.generateReturnMap(8, "signKey不正确", request.getRequestURI(), data);
								return map;
							}
						}else{
							randomMap.put(cellphone+type,signKey+","+new Date().getTime());
						}
					}		
				}else{
					map = appService.generateReturnMap(9, "signKey失效", request.getRequestURI(), data);
					return map;
				}
			}
			if(type == 0){
				PhoneCheckCodeCache.getInstance().pushToRegisteCheckCodeQueue(cellphone);
			}else {
				PhoneCheckCodeCache.getInstance().pushToChangePasswordCheckCodeQueue(cellphone);
			}
			data = appService.getServiceInfo();
			map = appService.generateReturnMap(0, "手机验证码已成功发送！", request.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in "+ request.getRequestURI()+" requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
			return map;
		}
	}
	
	
	
	@RequestMapping("checkCellPhoneCheckCode")
	@ResponseBody
	public Map<String, Object> checkCellPhoneCheckCode(HttpServletRequest request, String cellphone, String checkCode) {

		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {
	
			String checkCodeTmp = PhoneCheckCodeCache.getInstance().getCheckCode(cellphone);
			
			if(checkCodeTmp != null){
				if(checkCodeTmp.equals(checkCode)){
					
					map = appService.generateReturnMap(0, "校验成功", "checkCellPhoneCheckCode", data);
					PhoneCheckCodeCache.getInstance().removeCheckCode(cellphone);
					return map;
				}
				else{
					map = appService.generateReturnMap(1, "验证码错误", "checkCellPhoneCheckCode", data);
					return map;
				}
			}
			else{
				map = appService.generateReturnMap(1, "验证码错误", "checkCellPhoneCheckCode", data);
				return map;
			}
				
		} catch (Exception e) {
			log.error("exception in checkCellPhoneCheckCode requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", "checkCellPhoneCheckCode", data);
			return map;
		}
	}

	
 
	//获取反馈页面
	@RequestMapping("feedbackFTL")
	@ResponseBody
	public ModelAndView feedbackFTL(HttpServletRequest request, String cookie) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{		
			if(cookie == null){
				map.put("cookie", "");
			}
			else{
				map.put("cookie", cookie);
			}
			return new ModelAndView("app/feedback", map);
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			return new ModelAndView("app/feedback", map);
		}
	}
	
	//上传反馈与建议
	@RequestMapping("uploadFeedback")
	@ResponseBody
	public Map<String, Object> uploadFeedback(HttpServletRequest request, String cookie, String content) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try{		
			if(content == null || content.length() > 200){
				map = appService.generateReturnMap(1, "反馈内容不能超过200字符，请缩短！", request.getRequestURI(), data);
				return map;
			}
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			appService.uploadFeedback(userId, content);
			map = appService.generateReturnMap(0, "提交反馈成功！", request.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "提交反馈内容失败！", request.getRequestURI(), data);
			return map;
		}
	}
	
	/**
	 * 用户提交贷款相关信息
	 * @param request
	 * @param cookie
	 * @param user  
	 * @return
	 */
	@RequestMapping("submitLoanInfo")
	@ResponseBody
	public Map<String, Object> submitLoanInfo(HttpServletRequest request, String cookie, User user) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try{		
			
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			String ip = request.getRemoteHost();
			if(appService.submitLoanInfo(userId, user, ip)){
				map = appService.generateReturnMap(0, "信息提交成功", request.getRequestURI(), data);
			}else {
				map = appService.generateReturnMap(3, "信息提交失败", request.getRequestURI(), data);
			}
			return map;
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "错误异常", request.getRequestURI(), data);
			return map;
		}
	}
	
	@RequestMapping("getServiceInfo")
	@ResponseBody
	public Map<String, Object> getServiceInfo(HttpServletRequest request) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try{
			
			data = appService.getServiceInfo();
			map = appService.generateReturnMap(0, "获取客服信息成功！", request.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "操作异常，等我们今晚回去撸死程序猿！", request.getRequestURI(), data);
			return map;
		}
	}
		
	
	/** 获取版本信息 */
	@RequestMapping("getVersionInfo")
	@ResponseBody
	public Map<String, Object> getVersionInfo(HttpServletRequest req, Integer type) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (type == null) {
				map = MapUtils.generateReturnMap(1, "参数错误", req.getRequestURI(), data);
				return map;
			}
			VersionCache cache = VersionCache.getInstance(versionMapper);
			Version localVersion = cache.getVersion(type);
			Config config = ConfigCache.getInstance(configMapper, false).getConfig();
			int alipay_mode = config.getAlipay_mode();
			data.put("mode", alipay_mode);// 1：隐藏功能
			data.put("newVersion", config.getCheck_version());
			data.put("andriodMode", config.getAndroid_mode());// 1：隐藏功能
			data.put("andriodNewVersion", config.getAndroid_check_version());
			if (localVersion == null) {
				map = MapUtils.generateReturnMap(0, "成功", req.getRequestURI(), data);
				return map;
			}

			data.put("currentVersion", localVersion.getCurrentVersion());
			data.put("downLoadUrl", localVersion.getDownLoadUrl());
			data.put("description", localVersion.getDescription());
			data.put("isForceUpdate", localVersion.getIsForceUpdate());

			map = MapUtils.generateReturnMap(0, "成功", req.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in " + req.getRequestURI() + " requestParam: "
					+ ExceptionParamLog.logExceptionParam(req), e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, req.getRequestURI(), data);
			return map;
		}
	}
	
	
	//上传反馈与建议
	@RequestMapping("positionForLogin")
	@ResponseBody
	public Map<String, Object> positionForLogin(HttpServletRequest request, String cookie, String address) {
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try{		
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			appService.positionForLogin(userId, address);
			map = appService.generateReturnMap(0, "提交地址信息成功！", request.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = appService.generateReturnMap(10, "提交反馈内容失败！", request.getRequestURI(), data);
			return map;
		}
	}
	
	
	@RequestMapping("registerAgreementFTL")
	public String registerAgreementFTL() {
		return "web/register_agreement";
	}
	
	//静态页面
	@RequestMapping("loginFTL")
	public String loginFTL() {
		return "web/login";
	}
	
	@RequestMapping("securityCenterFTL")
	public String securityCenterFTL() {
		return "web/security_center";
	}
}
