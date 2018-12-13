package com.domi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domi.model.InviteUser;
import com.domi.service.InviteDataService;
import com.domi.service.WebAdminService;
import com.domi.support.utils.AesEncryptUtil;
import com.domi.support.utils.CookieInfo;
import com.domi.support.utils.CookieUtil;
import com.domi.support.utils.ExceptionParamLog;
import com.domi.support.utils.MD5;
import com.domi.support.utils.MapUtils;

@Controller
@RequestMapping("web/system")
public class WebAdminController {
	
	Logger log = Logger.getLogger(WebAdminController.class);
	public static final String LOGINTOKEN = "SYSTEMTOKEN";
	public static final int PAGESIZE = 10;
	
	@Autowired
	private WebAdminService webAdminService;
	@Autowired
	private InviteDataService inviteDataService;
	
	@RequestMapping("login.do")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response,  String account, String password) {
		Map<String, Object> data = new HashMap<>();
		try {
			if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
				return MapUtils.generateReturnMap(1, "参数出错", "", data);
			}
			
			InviteUser inviteUser = webAdminService.getInviteUser(account);
			if (inviteUser == null) {
				return MapUtils.generateReturnMap(3, "账户不存在", "", data);
			}
			if (!inviteUser.getPassword().equals(MD5.MD5Encrypt(password))) {
				return MapUtils.generateReturnMap(4, "密码不正确", "", data);
			}
			
			String token = AesEncryptUtil.encrypt(System.currentTimeMillis() + "`" + account);
			CookieInfo cookieInfo = new CookieInfo(LOGINTOKEN, token, 3600 * 24 * 365);
			CookieUtil.addCookie(response, cookieInfo);

			webAdminService.updateLoginToken(account, token);
			return MapUtils.generateReturnMap(0, "成功", "", data);
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: "
					+ ExceptionParamLog.logExceptionParam(request), e);
			return MapUtils.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
		}
	}
	
	@RequestMapping("islogin.do")
	@ResponseBody
	public Map<String, Object> islogin(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<>();
		try {
			InviteUser inviteUser = validlogin(request);
			if (inviteUser == null) {
				return MapUtils.generateReturnMap(2, "未登录", "", data);
			}
			data.put("account", inviteUser.getAccount());
			return MapUtils.generateReturnMap(0, "成功", "", data);
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: "
					+ ExceptionParamLog.logExceptionParam(request), e);
			return MapUtils.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
		}
	}
	
	@RequestMapping("logout.do")
	@ResponseBody
	public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<>();
		try {
			CookieUtil.deleteCookie(response, LOGINTOKEN);
			return MapUtils.generateReturnMap(0, "成功", "", data);
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: "
					+ ExceptionParamLog.logExceptionParam(request), e);
			return MapUtils.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
		}
	}
	
	@RequestMapping("getDataList.do")
	@ResponseBody
	public Map<String, Object> getDataList(HttpServletRequest request, HttpServletResponse response, Integer page, String beginDate, String endDate) {
		Map<String, Object> data = new HashMap<>();
		try {
			InviteUser inviteUser = validlogin(request);
			if (inviteUser == null) {
				return MapUtils.generateReturnMap(2, "未登录", "", data);
			}
			if (StringUtils.isEmpty(beginDate)) beginDate = null;
			if (StringUtils.isEmpty(endDate)) endDate = null;
			if (page == null || page <= 0) page = 1;
			int start = (page - 1) * PAGESIZE;
			data = inviteDataService.getDataListByUserId(inviteUser.getInviteId(), beginDate, endDate, start, PAGESIZE);
			return MapUtils.generateReturnMap(0, "成功", "", data);
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: "
					+ ExceptionParamLog.logExceptionParam(request), e);
			return MapUtils.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
		}
	}
	
	private InviteUser validlogin(HttpServletRequest request) {
		String token = CookieUtil.getString(request, LOGINTOKEN);
		if (StringUtils.isEmpty(token)) return null;
		String decryptText = AesEncryptUtil.decrypt(token);
		String account = decryptText.split("`")[1];
		InviteUser inviteUser = webAdminService.getInviteUser(account);
		if (inviteUser == null) return null;
		if (!inviteUser.getToken().equals(token)) return null;
		return inviteUser;
	}

	@RequestMapping("login.page")
	public String login_page() {
		return "h5_admin/login";
	}
	@RequestMapping("index.page")
	public String index_page() {
		return "h5_admin/index";
	}
}
