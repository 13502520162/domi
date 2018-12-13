package com.domi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domi.model.AccountBook;
import com.domi.service.AccountBookService;
import com.domi.service.AppService;
import com.domi.support.utils.ExceptionParamLog;
import com.domi.support.utils.MapUtils;

/**
 * 借款超市app
 * @author shexiao
 *
 */
@Controller
@RequestMapping("app/accountBook")
public class AccountBookContoller {
	private static final Logger logger = Logger.getLogger(AccountBookContoller.class);
	
	@Autowired
	private AccountBookService accountBookService;
	@Autowired
	private AppService appService;
	
	@RequestMapping("addAccountBook")
	@ResponseBody
	public Map<String, Object> addAccountBook(HttpServletRequest request, String cookie, AccountBook accountBook) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			accountBook.setUserId(userId);
			if(accountBookService.addAccountBook(accountBook)){
				map = MapUtils.generateReturnMap(0, "添加记录成功", request.getRequestURI(), data);
			}else {
				map = MapUtils.generateReturnMap(3, "添加记录失败", request.getRequestURI(), data);
			}
			return map;

		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	@RequestMapping("updateRemark")
	@ResponseBody
	public Map<String, Object> updateRemark(HttpServletRequest request, String cookie, int accountBookId, String remark) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			if(accountBookService.updateRemark(accountBookId, remark)){
				map = MapUtils.generateReturnMap(0, "修改备注成功", request.getRequestURI(), data);
			}else {
				map = MapUtils.generateReturnMap(3, "修改备注失败", request.getRequestURI(), data);
			}
			return map;
			
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	@RequestMapping("getAccountBooks")
	@ResponseBody
	public Map<String, Object> getAccountBooks(HttpServletRequest request, String cookie, Integer page) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			if(null == page) page=1;
			data = accountBookService.getAccountBooks(userId, page);			
			map = MapUtils.generateReturnMap(0, "成功", request.getRequestURI(), data);
            return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	@RequestMapping("getAccountBookInfo")
	@ResponseBody
	public Map<String, Object> getLoanPlatform(HttpServletRequest request,String cookie, Integer accountBookId) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			
			map =  accountBookService.getAccountBookInfo(userId, accountBookId,request);
			return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	@RequestMapping("delAccountBook")
	@ResponseBody
	public Map<String, Object> delAccountBook(HttpServletRequest request,String cookie, Integer accountBookId) {
		
		Map<String, Object> map = null;
		Map<String, Object> data = new HashMap<>();
		try {	
			boolean result =appService.validateCookie(cookie);
			if (!result) {
				map = appService.generateReturnMap(2, "cookie值不存在，请重新登录！", request.getRequestURI(), data);
				return map;
			}
			int userId = appService.getUserIdByCookie(cookie);
			
			map =  accountBookService.delAccountBook(userId, accountBookId,request);
			return map;
		} catch (Exception e) {
			logger.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
			return map;
		}
	}
	
	

	
	
}
