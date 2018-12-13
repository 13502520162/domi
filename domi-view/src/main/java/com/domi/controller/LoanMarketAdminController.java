package com.domi.controller;

import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domi.support.identification.StringUtil;
import com.domi.model.loanmarket.LoanPlatform;
import com.domi.model.loanmarket.PlatformInfo;
import com.domi.service.AdminService;
import com.domi.service.loanmarket.LoanMarketService;
import com.domi.service.util.Results;
import com.domi.support.utils.CookieUtil;
import com.domi.support.utils.DateUtil;
import com.domi.support.utils.ExcelUtils;
import com.domi.support.utils.ExceptionParamLog;
import com.domi.support.utils.MapUtils;

/**
 * 借款超市
 * @author
 *
 */
@Controller
@RequestMapping("admin/loanmarket")
public class LoanMarketAdminController {
	private static final Logger logger = Logger.getLogger(LoanMarketAdminController.class);
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private LoanMarketService loanMarketService;
	
	@RequestMapping("updateLoanPlatform")
	@ResponseBody
	public Map<String, Object> updateLoanPlatform(HttpServletRequest req, LoanPlatform loanPlatform) {
		Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            if (CookieUtil.getString(req, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(req, "loginKey"))) {
                map = MapUtils.generateReturnMap(2, "还没登录！", "", data);
                return map;
            }
            
            if (!StringUtil.isEmpty(loanPlatform.getSubscribeTimeStr())) {
            	loanPlatform.setSubscribeTime(DateUtil.formatDate(loanPlatform.getSubscribeTimeStr(), "yyyy-MM-dd HH:mm"));
            }
            
            int loanPlatformId = loanMarketService.updateLoanPlatform(loanPlatform);
            if (loanPlatformId <= 0) {
            	map = MapUtils.generateReturnMap(3, "失败", "", data);
                return map;
            }
            data.put("loanPlatformId", loanPlatformId);
            map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
        } catch (Exception e) {
        	logger.error("exception in:" + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req),e);
            map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, "", data);
            return map;
        }
	}
	
	@RequestMapping("getLoanPlatform")
	@ResponseBody
	public Map<String, Object> getLoanPlatform(HttpServletRequest req, Integer id) {
		Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            if (CookieUtil.getString(req, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(req, "loginKey"))) {
                map = MapUtils.generateReturnMap(2, "还没登录！", "", data);
                return map;
            }
            if (id == null || id <= 0) {
            	map = MapUtils.generateReturnMap(1, "参数出错", "", data);
                return map;
            }
            
            LoanPlatform loanPlatform = loanMarketService.getLoanPlatform(id);
            data.put("loanPlatform", loanPlatform);
            map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
        } catch (Exception e) {
        	logger.error("exception in:" + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req),e);
            map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, "", data);
            return map;
        }
	}
	
	@RequestMapping("getLoanPlatformList")
	@ResponseBody
	public Map<String, Object> getLoanPlatformList(HttpServletRequest req, Integer state, Integer page) {
		Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            if (CookieUtil.getString(req, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(req, "loginKey"))) {
                map = MapUtils.generateReturnMap(2, "还没登录！", "", data);
                return map;
            }
            if  (state == null || state < 0) {
            	map = MapUtils.generateReturnMap(1, "参数出错", "", data);
                return map;
            }
            if (page == null || page <= 0) {
            	page = 1;
            }
            
            data = loanMarketService.getLoanPlatformList(state, page);
            map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
        } catch (Exception e) {
        	logger.error("exception in:" + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req),e);
            map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, "", data);
            return map;
        }
	}
	
	@RequestMapping("updatePlatformInfo")
	@ResponseBody
	public Map<String, Object> updatePlatformInfo(HttpServletRequest req, PlatformInfo platformInfo) {
		Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            if (CookieUtil.getString(req, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(req, "loginKey"))) {
                map = MapUtils.generateReturnMap(2, "还没登录！", "", data);
                return map;
            }

            boolean result = loanMarketService.updatePlatformInfo(platformInfo);
            if (!result) {
            	map = MapUtils.generateReturnMap(3, "失败", "", data);
                return map;
            }
            map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
        } catch (Exception e) {
        	logger.error("exception in:" + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req),e);
            map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, "", data);
            return map;
        }
	}
	
	@RequestMapping("getPlatformInfo")
	@ResponseBody
	public Map<String, Object> getPlatformInfo(HttpServletRequest req, Integer loanPlatformId) {
		Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            if (CookieUtil.getString(req, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(req, "loginKey"))) {
                map = MapUtils.generateReturnMap(2, "还没登录！", "", data);
                return map;
            }

            if (loanPlatformId == null || loanPlatformId <= 0) {
            	map = MapUtils.generateReturnMap(1, "参数出错", "", data);
                return map;
            }
            data = loanMarketService.getPlatformInfo(loanPlatformId);            
            map = MapUtils.generateReturnMap(0, "成功", "", data);
            return map;
        } catch (Exception e) {
        	logger.error("exception in:" + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req),e);
            map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, "", data);
            return map;
        }
	}
	
	@RequestMapping("getPlatformData")
    @ResponseBody
    public Map<String, Object> queryAppData(HttpServletRequest request, String beginDate, String endDate, Integer state) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	Map<String, Object> data = new HashMap<>();
    	try {
    		if(null==state) state=1;
    		if(null == beginDate) beginDate="1970-01-01";
    		if(null == endDate) 
    			endDate = DateUtil.getDateString(new Date());
    		if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
    			map = adminService.generateReturnMap(2, "还没登录！", request.getRequestURI(), data);
    			return map;
    		}
    		data = loanMarketService.getPlatformData(state,beginDate, endDate);
    		map = adminService.generateReturnMap(0, "返回数据成功", request.getRequestURI(), data);
    		return map;
    		
    	} catch (Exception e) {
    		logger.error("exception in "+request.getRequestURI(), e);
    		return Results.results(10, "操作失效", "getBanners", Collections.EMPTY_MAP);
    	}
    }
	
	
	@RequestMapping("exportPlatformInfo")
	public void exportPlatformInfo(HttpServletRequest req, HttpServletResponse resp) {
		OutputStream out = null;
		try {
			out=resp.getOutputStream();  
			if(CookieUtil.getString(req, "loginKey")==null || !adminService.validateLoginKey(CookieUtil.getString(req, "loginKey"))){
				logger.error("还没登录");
				return;
			}
			
			List<PlatformInfo> list = loanMarketService.getAllPlatformInfo();
			
			String title = "借款平台信息-" + DateUtil.getDateString(new Date(), "yyyyMMdd");
			HSSFWorkbook workbook = new HSSFWorkbook();
			ExcelUtils.exportLoanPlatformInfo(workbook, list, title);
			
			//重置输出流  
			resp.reset();  
	        //设置导出Excel报表的导出形式  
			resp.setContentType("application/vnd.ms-excel");  
			resp.setHeader("Content-Disposition","attachment;filename=" + new String(title.getBytes("gb2312"),"ISO-8859-1") + ".xls");  
			workbook.write(out);
		} catch (Exception e) {
			logger.error("导出平台信息excel出错" + e,  e );
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (Exception e) {
					logger.error("关闭out出错" + e,  e );
				}
			}
		}
	}
	

}
