package com.domi.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domi.cache.PhoneCheckCodeCache;
import com.domi.model.User;
import com.domi.service.AppService;
import com.domi.service.InviteDataService;
import com.domi.support.utils.ExceptionParamLog;
import com.domi.support.utils.LuckyThreadPool;
import com.domi.support.utils.MD5;
import com.domi.support.utils.MapUtils;

@Controller
@RequestMapping("web")
public class WebController {
	Logger log = Logger.getLogger(WebController.class);

	Map<String, Long> phoneCodeLimit = new HashMap<>();
	private final static Long minute = 60  * 1000L;

	@Autowired
	private AppService appService;
	@Autowired
	private InviteDataService inviteDataService;

	/***
	 * h5邀请码注册
	 * 
	 * @param request
	 * @param cellphone
	 * @param password
	 * @param code
	 * @param inviteCode
	 * @return
	 */
	@RequestMapping("register.do")
	@ResponseBody
	public Map<String, Object> register(HttpServletRequest request, String cellphone, String password, String code,
			Integer inviteCode) {
		Map<String, Object> data = new HashMap<>();
		try {
			if (StringUtils.isEmpty(cellphone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
				return MapUtils.generateReturnMap(1, "参数出错", "", data);
			}
			if (inviteCode == null || inviteCode <= 0)
				inviteCode = 0;

			String checkCodeTmp = PhoneCheckCodeCache.getInstance().getCheckCode(cellphone);
			if (!code.equalsIgnoreCase(checkCodeTmp)) {
				return MapUtils.generateReturnMap(1, "手机验证码错误", "", data);
			}
			PhoneCheckCodeCache.getInstance().removeCheckCode(cellphone);

			if (appService.isPhoneExist(cellphone)) {
				return appService.generateReturnMap(4, "手机号码已经被注册，请更换手机号码!", request.getRequestURI(), data);
			}
			
			final Date today = new Date();
			final int inviteUserId = inviteCode;
			User user = new User();
			user.setCellphone(cellphone);
			user.setPassword(MD5.MD5Encrypt(password));
			user.setInviteId(inviteCode);
			user.setRegisterTime(today);
			boolean result = appService.register(user);
			if (!result) {
				return MapUtils.generateReturnMap(3, "注册失败", "", data);
			}
			
			LuckyThreadPool.getInstance().getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						inviteDataService.updateInviteData(inviteUserId, today);
					} catch (Exception e) {
						log.error("更新邀请数据失败：", e);
					}
					
				}
			});

			return MapUtils.generateReturnMap(0, "成功", "", data);
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: "
					+ ExceptionParamLog.logExceptionParam(request), e);
			return MapUtils.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
		}
	}

	/***
	 * 发送验证码
	 * 
	 * @param request
	 * @param cellphone
	 * @return
	 */
	@RequestMapping("getPhoneCode.do")
	@ResponseBody
	public Map<String, Object> getPhoneCode(HttpServletRequest request, String cellphone) {
		Map<String, Object> data = new HashMap<>();
		try {
			
			if (StringUtils.isEmpty(cellphone)) {
				return MapUtils.generateReturnMap(1, "参数出错", "", data);
			}
			if (appService.isPhoneExist(cellphone)) {
				return appService.generateReturnMap(4, "手机号码已经被注册，请更换手机号码!", request.getRequestURI(), data);
			}
			if (phoneCodeLimit.containsKey(cellphone)) {
				long pre_ms = phoneCodeLimit.get(cellphone);
				long dur_ms = System.currentTimeMillis() - pre_ms;
				if (dur_ms < minute) {
					return appService.generateReturnMap(5, "发送过于频繁", request.getRequestURI(), data);
				}
			}
			phoneCodeLimit.put(cellphone, System.currentTimeMillis());
			PhoneCheckCodeCache.getInstance().pushToRegisteCheckCodeQueue(cellphone);
			return MapUtils.generateReturnMap(0, "成功", "", data);
		} catch (Exception e) {
			log.error("exception in:" + request.getRequestURI() + " requestParam: "
					+ ExceptionParamLog.logExceptionParam(request), e);
			return MapUtils.generateReturnMap(10, "出问题了，程序猿们等着今晚被捡肥皂了 @_@", request.getRequestURI(), data);
		}
	}
	
	@RequestMapping("invite_page.page")
	public String register_page(HttpServletRequest request, Integer inviteCode) {
		if (inviteCode == null || inviteCode <= 0) inviteCode = 0;
		request.setAttribute("inviteCode", inviteCode);
		return "h5/DaiDai";
	}
	
	@RequestMapping("register_success.page")
	public String register_success() {
		return "h5/Success";
	}
}
