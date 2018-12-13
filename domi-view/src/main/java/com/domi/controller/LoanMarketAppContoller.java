package com.domi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domi.support.identification.StringUtil;
import com.domi.model.loanmarket.LoanPlatform;
import com.domi.service.AppService;
import com.domi.service.loanmarket.LoanMarketService;
import com.domi.support.utils.ExceptionParamLog;
import com.domi.support.utils.MapUtils;

/**
 * 借款超市app
 * @author shexiao
 *
 */
@Controller
@RequestMapping("app/loanmarket")
public class LoanMarketAppContoller {
	private static final Logger logger = Logger.getLogger(LoanMarketAppContoller.class);
	
	@Autowired
	private LoanMarketService loanMarketService;
	@Autowired
	private AppService appService;
	
	@RequestMapping("getFirstPageInfo")
	@ResponseBody
	public Map<String, Object> getFirstPageInfo(HttpServletRequest request, String cookie) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			int userId=-1;
			if(cookie!=null && appService.validateCookie(cookie)){
				userId = appService.getUserIdByCookie(cookie);
			}
			data = loanMarketService.getFirstPageInfo(userId);
			map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	@RequestMapping("getLoanPlatforms")
	@ResponseBody
	public Map<String, Object> getLoanPlatforms(HttpServletRequest request, String cookie, Integer page) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			int userId=-1;
			if(null == page) page=1;
			if(cookie!=null && appService.validateCookie(cookie)){
				userId = appService.getUserIdByCookie(cookie);
			}
			data = loanMarketService.getLoanPlatforms(userId, page);
			map = MapUtils.generateReturnMap(0, "成功", "", data);
			return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	@RequestMapping("getLoanPlatformList")
	@ResponseBody
	public Map<String, Object> getLoanPlatformList(HttpServletRequest request, String cookie, Integer moneyOption, Integer type,
			String userRole, String userMaterial, String userUsage, Integer page) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			if (page == null || page <= 0) {
				page = 1;
			}
			Integer minMoney = null;
			Integer maxMoney = null;
			if (moneyOption != null && moneyOption > 0) {
				if (moneyOption == 1) {
					minMoney = 0;
					maxMoney = 2000;
				} else if (moneyOption == 2) {
					minMoney = 2000;
					maxMoney = 5000;
				} else if (moneyOption == 3) {
					minMoney = 5000;
					maxMoney = 10000;
				} else {
					minMoney = 10000;
				}
			}
			
			Integer platformState = null;
			String ratioRate = null;
			String moneyRate = null;
			if (type != null && type > 0) {
				if (type == 3) {
					ratioRate = "1";
				} else if (type == 4) {
					moneyRate = "2";
				} else {
					platformState = type;
				}
			}
			
			if (StringUtil.isEmpty(userRole) || userRole.equals("不限")) {
				userRole = null;
			}
			String[] userMaterials = null;
			if (!StringUtil.isEmpty(userMaterial)) {
				userMaterials = userMaterial.split(",");
			}
			
			String[] userUsages = null;
			if (!StringUtil.isEmpty(userUsage)) {
				userUsages = userUsage.split(",");
			}
			
			data = loanMarketService.getLoanPlatformListByApp(page, minMoney, maxMoney, platformState, ratioRate, moneyRate, userRole, userMaterials, userUsages);
			map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	@RequestMapping("getLoanPlatform")
	@ResponseBody
	public Map<String, Object> getLoanPlatform(HttpServletRequest request, Integer loanPlatformId) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			if (loanPlatformId == null || loanPlatformId <= 0) {
				map = MapUtils.generateReturnMap(1, "参数异常", "", data);
	            return map;
			}
			
			LoanPlatform loanPlatform = loanMarketService.getLoanPlatform(loanPlatformId);
			
			data.put("loanPlatform", loanPlatform);
			map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @param cookie	
	 * @param loanPlatformId	平台id
	 * @param type	区分是浏览还是注册 0:浏览 1：注册
	 * @return
	 */
	@RequestMapping("visitPlatform")
	@ResponseBody
	public Map<String, Object> visitPlatform(HttpServletRequest request, String cookie,  Integer loanPlatformId, Integer type) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			if (loanPlatformId == null || loanPlatformId <= 0) {
				map = MapUtils.generateReturnMap(1, "参数异常", "", data);
	            return map;
			}
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			loanMarketService.visitPlatform(userId, loanPlatformId, type);
			map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	
	@RequestMapping("getVisitHistories")
	@ResponseBody
	public Map<String, Object> getVisitHistories(HttpServletRequest request, String cookie,  Integer page) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			if(null == page) page=1; 
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			data = loanMarketService.getVisitHistories(userId, page);
			map = MapUtils.generateReturnMap(0, "成功", "", data);
			return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	
	
	
}
